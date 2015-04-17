package net.robi42.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jersey.JerseyApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import lombok.val;
import net.robi42.boot.dao.MessageRepository;
import net.robi42.boot.domain.MessageEntity;
import net.robi42.boot.rest.JerseyConfig;
import net.robi42.boot.rest.ObjectMapperProvider;
import net.robi42.boot.search.ElasticsearchEntityMapper;
import net.robi42.boot.search.ElasticsearchProviderImpl;
import net.robi42.boot.service.MessageDtoConverter;
import net.robi42.boot.service.MessageEntityFactory;
import net.robi42.boot.service.MessageEntityFactoryImpl;
import net.robi42.boot.service.MessageService;
import net.robi42.boot.service.MessageServiceImpl;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.inject.Inject;
import javax.ws.rs.client.Client;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.glassfish.jersey.apache.connector.ApacheClientProperties.CONNECTION_MANAGER;
import static org.glassfish.jersey.client.ClientProperties.CONNECT_TIMEOUT;
import static org.glassfish.jersey.client.ClientProperties.READ_TIMEOUT;

@Configuration class BeanConfig {
    @Value("${web-client.connection-pool.max-total}") int webClientConnectionPoolMaxTotal;
    @Value("${web-client.connection-pool.default-max-per-route}") int webClientConnectionPoolDefaultMaxPerRoute;

    @Inject ObjectMapper objectMapper;

    @Bean JerseyConfig jerseyConfig() {
        return new JerseyConfig();
    }

    @Bean CommandLineRunner swaggerConfig() {
        val swaggerConfig = new SwaggerConfig();
        swaggerConfig.setBasePath("/");
        swaggerConfig.setApiVersion(Application.version());

        return args -> {
            ConfigFactory.setConfig(swaggerConfig);
            ScannerFactory.setScanner(new DefaultJaxrsScanner());
            ClassReaders.setReader(new JerseyApiReader());
        };
    }

    @Bean Client webClient() {
        val poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(webClientConnectionPoolMaxTotal);
        poolingConnectionManager.setDefaultMaxPerRoute(webClientConnectionPoolDefaultMaxPerRoute);

        return newClient(new ClientConfig()
                .property(READ_TIMEOUT, 2000)
                .property(CONNECT_TIMEOUT, 500)
                .property(CONNECTION_MANAGER, poolingConnectionManager)
                .connectorProvider(new ApacheConnectorProvider()))
                .register(new ObjectMapperProvider(objectMapper));
    }

    @Bean ElasticsearchEntityMapper elasticsearchEntityMapper() {
        return new ElasticsearchEntityMapper(objectMapper);
    }

    @Bean ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeBuilder().local(true).node().client(), elasticsearchEntityMapper());
    }

    @Bean MessageEntityFactory messageEntityFactory() {
        return new MessageEntityFactoryImpl();
    }

    @Bean MessageService messageService(MessageRepository repository) {
        val searchProvider = new ElasticsearchProviderImpl(elasticsearchTemplate());
        return new MessageServiceImpl(repository, messageEntityFactory(), new MessageDtoConverter(), searchProvider);
    }

    @Bean HealthIndicator messageIndexHealthIndicator(ElasticsearchOperations elasticsearchTemplate) {
        return () -> elasticsearchTemplate.typeExists(MessageEntity.INDEX_NAME, MessageEntity.TYPE_NAME)
                ? Health.up().build() : Health.down().build();
    }
}
