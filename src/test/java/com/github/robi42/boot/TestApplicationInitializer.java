package com.github.robi42.boot;

import com.github.robi42.boot.dao.MessageRepository;
import com.github.robi42.boot.dao.RepositoryRoot;
import com.github.robi42.boot.domain.Message;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;

@Configuration
@ComponentScan
@Profile("test")
@EnableElasticsearchRepositories(basePackageClasses = RepositoryRoot.class)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, ManagementSecurityAutoConfiguration.class})
public class TestApplicationInitializer extends ApplicationInitializer {

    @Bean
    @Override
    public InitializingBean populateMessageIndex(final MessageRepository repository) {
        return () -> {
            repository.deleteAll();
            repository.save(ImmutableList.of(message("Foo"), message("Bar"), message("Baz")));
        };
    }

    private Message message(final String text) {
        return Message.builder()
                .id(randomUUID().toString())
                .body(text)
                .lastModifiedAt(LocalDateTime.now())
                .build();
    }
}
