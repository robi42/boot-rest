package net.robi42.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider;
import com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider;
import net.robi42.boot.rest.MessageResource;
import net.robi42.boot.rest.ObjectMapperProvider;
import net.robi42.boot.service.MessageService;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

import static org.glassfish.jersey.server.ServerProperties.BV_SEND_ERROR_IN_RESPONSE;

@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig(MessageService messageService, ObjectMapper objectMapper) {
        registerInstances(
                new MessageResource(messageService),
                new ObjectMapperProvider(objectMapper)
        );
        registerClasses( // Swagger API Docs
                ApiListingResourceJSON.class, JerseyApiDeclarationProvider.class, JerseyResourceListingProvider.class
        );

        property(BV_SEND_ERROR_IN_RESPONSE, true);
    }
}
