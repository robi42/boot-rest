package net.robi42.boot;

import com.google.common.collect.ImmutableList;
import net.robi42.boot.dao.MessageRepository;
import net.robi42.boot.dao.RepositoryRoot;
import net.robi42.boot.util.TestFixtureFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.inject.Inject;

@Configuration
@ComponentScan
@Profile("test")
@EnableElasticsearchRepositories(basePackageClasses = RepositoryRoot.class)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, ManagementSecurityAutoConfiguration.class})
public class TestApplicationInitializer extends ApplicationInitializer {
    @Inject
    private TestFixtureFactory fixtures;

    @Bean
    @Override
    public InitializingBean populateMessageIndex(final MessageRepository repository) {
        return () -> {
            repository.deleteAll();
            repository.save(ImmutableList.of(
                    fixtures.message("Foo"),
                    fixtures.message("Bar"),
                    fixtures.message("Baz")));
        };
    }
}
