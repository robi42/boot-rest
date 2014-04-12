package com.github.robi42.boot.rest.util;

import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class JaxrsServletContainer extends ServletContainer {

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

        // Set up Swagger REST API docs
        final SwaggerConfig swaggerConfig = new SwaggerConfig();
        swaggerConfig.setBasePath("http://localhost:8888/api");
        swaggerConfig.setApiVersion("0.1.0"); // TODO: don't hard-code
        ConfigFactory.setConfig(swaggerConfig);
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new DefaultJaxrsApiReader());
    }
}
