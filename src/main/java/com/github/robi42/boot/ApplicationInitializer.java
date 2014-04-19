package com.github.robi42.boot;

import com.github.robi42.boot.dao.MessageRepository;
import com.github.robi42.boot.dao.RepositoryRoot;
import com.github.robi42.boot.domain.Message;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;

@Configuration
@Import({BeanConfig.class, SecurityConfig.class})
@EnableElasticsearchRepositories(basePackageClasses = RepositoryRoot.class)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, ManagementSecurityAutoConfiguration.class})
public class ApplicationInitializer extends SpringBootServletInitializer {

    @Bean
    public InitializingBean populateMessageIndex(final MessageRepository repository) {
        return () -> {
            if (repository.count() == 0) {
                repository.save(Message.builder()
                        .id(randomUUID().toString())
                        .body("Foo")
                        .lastModifiedAt(LocalDateTime.now())
                        .build());
            }
        };
    }

    @Bean
    public HealthIndicator<Object> messageIndexHealthIndicator(final ElasticsearchOperations elasticsearchTemplate) {
        return () -> elasticsearchTemplate.typeExists(Message.INDEX_NAME, Message.TYPE_NAME) ? "ok" : "error";
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(ApplicationInitializer.class);
    }

    public static void main(final String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
}
