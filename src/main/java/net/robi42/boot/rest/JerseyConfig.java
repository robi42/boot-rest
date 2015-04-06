package net.robi42.boot.rest;

import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResource;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

import static org.glassfish.jersey.server.ServerProperties.BV_SEND_ERROR_IN_RESPONSE;

@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        final boolean recursive = true;
        packages(recursive, getClass().getPackage().getName());

        registerSwaggerRestApiDocs();
        property(BV_SEND_ERROR_IN_RESPONSE, true);
    }

    private void registerSwaggerRestApiDocs() {
        register(ApiListingResource.class);
        register(ApiListingResourceJSON.class);
        register(ApiDeclarationProvider.class);
        register(ResourceListingProvider.class);
    }
}
