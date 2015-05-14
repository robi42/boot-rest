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
import net.robi42.boot.domain.Message;
import net.robi42.boot.service.MessageService;
import net.robi42.boot.service.MessageServiceImpl;
import net.robi42.boot.util.CustomEntityMapper;
import net.robi42.boot.util.Manifest;
import net.robi42.boot.util.MessageFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.inject.Inject;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Configuration class BeanConfig {
    @Inject ObjectMapper objectMapper;
    @Inject MessageRepository messageRepository;

    @Bean JerseyConfig jerseyConfig() {
        return new JerseyConfig(messageService(), objectMapper);
    }

    @Bean CommandLineRunner swaggerConfig() {
        val swaggerConfig = new SwaggerConfig();
        swaggerConfig.setBasePath("/");
        swaggerConfig.setApiVersion(Manifest.version());

        return args -> {
            ConfigFactory.setConfig(swaggerConfig);
            ScannerFactory.setScanner(new DefaultJaxrsScanner());
            ClassReaders.setReader(new JerseyApiReader());
        };
    }

    @Bean CustomEntityMapper entityMapper() {
        return new CustomEntityMapper(objectMapper);
    }

    @Bean ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeBuilder().local(true).node().client(), entityMapper());
    }

    @Bean MessageFactory messageFactory() {
        return new MessageFactory.Impl();
    }

    @Bean MessageService messageService() {
        return new MessageServiceImpl(messageRepository, messageFactory());
    }

    @Bean HealthIndicator messageIndexHealthIndicator(ElasticsearchOperations elasticsearchTemplate) {
        return () -> elasticsearchTemplate.typeExists(Message.INDEX_NAME, Message.TYPE_NAME)
                ? Health.up().build() : Health.down().build();
    }
}
