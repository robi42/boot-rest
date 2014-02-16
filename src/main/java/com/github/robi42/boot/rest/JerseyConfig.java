package com.github.robi42.boot.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.spring.bridge.api.SpringBridge;
import org.jvnet.hk2.spring.bridge.api.SpringIntoHK2Bridge;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.RequestContextFilter;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import static org.glassfish.jersey.server.ServerProperties.BV_SEND_ERROR_IN_RESPONSE;
import static org.glassfish.jersey.server.ServerProperties.MOXY_JSON_FEATURE_DISABLE;

public class JerseyConfig extends ResourceConfig {

    @Inject
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public JerseyConfig(final ServiceLocator serviceLocator) {
        // Register base package of REST resources
        packages(true, getClass().getPackage().getName());

        // Set up Spring (**yay**) <-> HK2 (**nay**) DI bridge
        SpringBridge.getSpringBridge().initializeSpringBridge(serviceLocator);
        final SpringIntoHK2Bridge springBridge = serviceLocator.getService(SpringIntoHK2Bridge.class);
        final ServletContext servletContext = serviceLocator.getService(ServletContext.class);
        final WebApplicationContext springWebAppContext = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);
        springBridge.bridgeSpringBeanFactory(springWebAppContext.getAutowireCapableBeanFactory());

        // Configure Jersey server
        property(BV_SEND_ERROR_IN_RESPONSE, true);
        property(MOXY_JSON_FEATURE_DISABLE, true);

        // Register Jackson JSON provider (incl. JDK 8 `java.time.*` a.k.a. JSR-310 support)
        final JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // -> ISO string serialization
        jsonProvider.setMapper(objectMapper);
        register(jsonProvider);

        // Register requests within Spring context
        register(RequestContextFilter.class);
    }
}
