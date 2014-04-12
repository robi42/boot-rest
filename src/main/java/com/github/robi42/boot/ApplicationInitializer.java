package com.github.robi42.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.robi42.boot.dao.RepositoryRoot;
import com.github.robi42.boot.domain.util.BeanValidator;
import com.github.robi42.boot.domain.util.BootBeanValidator;
import com.github.robi42.boot.domain.util.ElasticsearchEntityMapper;
import com.github.robi42.boot.rest.util.JerseyApplication;
import com.github.robi42.boot.rest.util.JerseySwaggerServlet;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.servlets.GzipFilter;
import org.elasticsearch.node.Node;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.servlet.Filter;
import javax.validation.Validator;
import javax.ws.rs.client.Client;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.glassfish.jersey.apache.connector.ApacheClientProperties.CONNECTION_MANAGER;
import static org.glassfish.jersey.client.ClientProperties.CONNECT_TIMEOUT;
import static org.glassfish.jersey.client.ClientProperties.READ_TIMEOUT;
import static org.glassfish.jersey.servlet.ServletProperties.JAXRS_APPLICATION_CLASS;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = RepositoryRoot.class)
@EnableAutoConfiguration(exclude =
        {SecurityAutoConfiguration.class, ManagementSecurityAutoConfiguration.class})
public class ApplicationInitializer extends SpringBootServletInitializer {
    @Value("${elasticsearch.clusterName}")
    protected String elasticsearchClusterName;
    @Value("${webClient.connectionPool.maxTotal}")
    protected int webClientConnectionPoolMaxTotal;
    @Value("${webClient.connectionPool.defaultMaxPerRoute}")
    protected int webClientConnectionPoolDefaultMaxPerRoute;

    @Bean
    public Filter gzipFilter() {
        return new GzipFilter();
    }

    @Bean
    public ServletRegistrationBean jaxrsServlet() {
        final JerseySwaggerServlet servlet = new JerseySwaggerServlet();
        final ServletRegistrationBean registrationBean = new ServletRegistrationBean(servlet, "/api/*");
        registrationBean.addInitParameter(JAXRS_APPLICATION_CLASS, JerseyApplication.class.getName());
        return registrationBean;
    }

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Auto-detect `JSR310Module`...
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // -> ISO string serialization
        return objectMapper;
    }

    @Bean
    public JacksonJsonProvider jacksonJsonProvider() {
        return new JacksonJsonProvider(objectMapper());
    }

    @Bean
    public Client webClient() {
        final PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(webClientConnectionPoolMaxTotal);
        poolingConnectionManager.setDefaultMaxPerRoute(webClientConnectionPoolDefaultMaxPerRoute);

        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.property(READ_TIMEOUT, 2000);
        clientConfig.property(CONNECT_TIMEOUT, 500);
        clientConfig.property(CONNECTION_MANAGER, poolingConnectionManager);
        clientConfig.connectorProvider(new ApacheConnectorProvider());

        return newClient(clientConfig).register(jacksonJsonProvider());
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        // Note: in a real prod env one would rather connect to a dedicated remote cluster instead of embedding locally
        final Node node = nodeBuilder()
                .settings(settingsBuilder().loadFromClasspath("elasticsearch.yml"))
                .clusterName(elasticsearchClusterName)
                .data(true).local(true)
                .node();
        return new ElasticsearchTemplate(node.client(), new ElasticsearchEntityMapper(objectMapper()));
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public BeanValidator beanValidator() {
        return new BootBeanValidator(validator());
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(ApplicationInitializer.class);
    }

    public static void main(final String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
}
