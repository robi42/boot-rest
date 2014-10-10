package com.github.robi42.boot.util;

import com.github.robi42.boot.ApplicationInitializer;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class JerseySwaggerServlet extends ServletContainer {

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

        // Set up Swagger REST API docs
        final SwaggerConfig swaggerConfig = new SwaggerConfig();
        swaggerConfig.setBasePath("/");
        swaggerConfig.setApiVersion(ApplicationInitializer.version());
        ConfigFactory.setConfig(swaggerConfig);
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new DefaultJaxrsApiReader());
    }
}
