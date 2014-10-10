package com.github.robi42.boot;

import com.github.robi42.boot.dao.MessageRepository;
import com.github.robi42.boot.dao.RepositoryRoot;
import com.github.robi42.boot.domain.Message;
import com.google.common.io.CharStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.UUID.randomUUID;

@Slf4j
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
                        .lastModifiedAt(new Date())
                        .build());
            }
        };
    }

    @Bean
    public HealthIndicator indexHealthIndicator(final ElasticsearchOperations elasticsearchTemplate) {
        return () -> elasticsearchTemplate.typeExists(Message.INDEX_NAME, Message.TYPE_NAME)
                ? Health.up().build() : Health.down().build();
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(ApplicationInitializer.class);
    }

    public static void main(final String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
        log.info("Running application version {}", version());
    }

    public static String version() {
        try (final InputStream in = new ClassPathResource("version.txt").getInputStream();
             final InputStreamReader readable = new InputStreamReader(in, UTF_8.name())) {
            return CharStreams.toString(readable).trim();
        } catch (final IOException e) {
            log.warn("Couldn't load application version from file");
            return "0.1.0";
        }
    }
}
