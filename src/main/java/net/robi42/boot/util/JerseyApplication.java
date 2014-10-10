package net.robi42.boot.util;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.wordnik.swagger.jaxrs.JaxrsApiReader;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResource;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.spring.bridge.api.SpringBridge;
import org.jvnet.hk2.spring.bridge.api.SpringIntoHK2Bridge;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.RequestContextFilter;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import static org.glassfish.jersey.server.ServerProperties.BV_SEND_ERROR_IN_RESPONSE;
import static org.glassfish.jersey.server.ServerProperties.MOXY_JSON_FEATURE_DISABLE;

public class JerseyApplication extends ResourceConfig {

    @Inject
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public JerseyApplication(final ServiceLocator serviceLocator) {
        // Register base package of REST providers & resources
        final boolean recursive = true;
        final String packageName = getClass().getPackage().getName();
        packages(recursive, packageName.replace(".util", ""));

        registerSwaggerRestApiDocs();

        // Set up Spring (**yay**) <-> HK2 (**nay**) DI bridge
        final BeanFactory beanFactory = setUpSpringBridge(serviceLocator);

        // Configure Jersey server
        property(BV_SEND_ERROR_IN_RESPONSE, true);
        property(MOXY_JSON_FEATURE_DISABLE, true);

        // Register Jackson JSON provider incl. JDK 8 `java.time.*` a.k.a. JSR-310 support...
        register(beanFactory.getBean(JacksonJsonProvider.class));
        // Register requests within Spring context
        register(RequestContextFilter.class);
    }

    private void registerSwaggerRestApiDocs() {
        register(JaxrsApiReader.class);
        register(ApiListingResource.class);
        register(ApiListingResourceJSON.class);
        register(ApiDeclarationProvider.class);
        register(ResourceListingProvider.class);
    }

    private BeanFactory setUpSpringBridge(final ServiceLocator serviceLocator) {
        SpringBridge.getSpringBridge().initializeSpringBridge(serviceLocator);

        final SpringIntoHK2Bridge springBridge = serviceLocator.getService(SpringIntoHK2Bridge.class);
        final ServletContext servletContext = serviceLocator.getService(ServletContext.class);
        final WebApplicationContext springWebAppContext = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);
        final BeanFactory beanFactory = springWebAppContext.getAutowireCapableBeanFactory();
        springBridge.bridgeSpringBeanFactory(beanFactory);

        return beanFactory;
    }
}
