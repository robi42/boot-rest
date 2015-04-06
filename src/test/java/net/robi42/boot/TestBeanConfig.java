package net.robi42.boot;

import com.google.common.collect.ImmutableList;
import net.robi42.boot.dao.MessageRepository;
import net.robi42.boot.util.TestFixtureFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration class TestBeanConfig extends BeanConfig {
    @Inject TestFixtureFactory fixtures;

    @Bean CommandLineRunner populateMessageIndex(MessageRepository repository) {
        return args -> {
            repository.deleteAll();
            repository.save(ImmutableList.of(
                    fixtures.message("Foo"),
                    fixtures.message("Bar"),
                    fixtures.message("Baz")));
        };
    }
}
