package net.robi42.boot;

import com.google.common.collect.ImmutableList;
import net.robi42.boot.dao.MessageRepository;
import net.robi42.boot.rest.ObjectMapperProvider;
import net.robi42.boot.util.MessageFactory;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.ws.rs.client.Client;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static org.glassfish.jersey.client.ClientProperties.CONNECT_TIMEOUT;
import static org.glassfish.jersey.client.ClientProperties.READ_TIMEOUT;

@Configuration class TestBeanConfig extends BeanConfig {
    @Inject MessageFactory fixtures;

    @Bean CommandLineRunner populateMessageIndex(MessageRepository repository) {
        return args -> {
            repository.deleteAll();
            repository.save(ImmutableList.of(
                    fixtures.newMessage("Foo"),
                    fixtures.newMessage("Bar")));
            Thread.sleep(1000);
            repository.save(fixtures.newMessage("Baz"));
        };
    }

    @Bean Client webClient() {
        return newClient(new ClientConfig()
                .property(READ_TIMEOUT, 2000)
                .property(CONNECT_TIMEOUT, 500)
                .connectorProvider(new ApacheConnectorProvider()))
                .register(new ObjectMapperProvider(objectMapper));
    }
}
