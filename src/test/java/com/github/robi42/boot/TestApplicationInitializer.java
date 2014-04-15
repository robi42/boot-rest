package com.github.robi42.boot;

import com.github.robi42.boot.dao.MessageRepository;
import com.github.robi42.boot.dao.RepositoryRoot;
import com.github.robi42.boot.domain.Message;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;

@Configuration
@ComponentScan
@Import(TestBeanConfig.class)
@EnableElasticsearchRepositories(basePackageClasses = RepositoryRoot.class)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, ManagementSecurityAutoConfiguration.class})
public class TestApplicationInitializer extends ApplicationInitializer {

    @Bean
    @Override
    public InitializingBean populateMessageIndex(final MessageRepository repository) {
        return () -> {
            repository.deleteAll();
            repository.save(Message.builder()
                    .id(randomUUID().toString())
                    .body("Foo")
                    .lastModifiedAt(LocalDateTime.now())
                    .build());
        };
    }
}
