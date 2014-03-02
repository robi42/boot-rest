package com.github.robi42.boot;

import com.github.robi42.boot.dao.RepositoryRoot;
import com.github.robi42.boot.domain.util.ElasticsearchEntityMapper;
import com.github.robi42.boot.rest.JerseyConfig;
import org.eclipse.jetty.servlets.GzipFilter;
import org.elasticsearch.node.Node;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.servlet.Filter;
import javax.validation.Validator;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.glassfish.jersey.servlet.ServletProperties.JAXRS_APPLICATION_CLASS;

@ComponentScan
@Configuration
@EnableElasticsearchRepositories(basePackageClasses = RepositoryRoot.class)
@EnableAutoConfiguration(exclude =
        {SecurityAutoConfiguration.class, ManagementSecurityAutoConfiguration.class})
public class ApplicationInitializer extends SpringBootServletInitializer {
    @Value("${elasticsearch.clusterName}")
    private String elasticsearchClusterName;

    @Bean
    public Filter gzipFilter() {
        return new GzipFilter();
    }

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        final ServletRegistrationBean registrationBean =
                new ServletRegistrationBean(new ServletContainer(), "/api/*");
        registrationBean.addInitParameter(JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        return registrationBean;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        // Note: in a real prod env one would rather connect to a dedicated remote cluster instead of embedding locally
        final Node node = nodeBuilder()
                .settings(settingsBuilder().loadFromClasspath("elasticsearch.yml"))
                .clusterName(elasticsearchClusterName)
                .data(true).local(true)
                .node();
        return new ElasticsearchTemplate(node.client(), new ElasticsearchEntityMapper());
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(ApplicationInitializer.class);
    }

    public static void main(final String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
}
