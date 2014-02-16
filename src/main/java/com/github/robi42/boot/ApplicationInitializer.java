package com.github.robi42.boot;

import com.github.robi42.boot.rest.JerseyConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.glassfish.jersey.servlet.ServletProperties.JAXRS_APPLICATION_CLASS;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class ApplicationInitializer extends SpringBootServletInitializer {

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        final ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ServletContainer(), "/api/*");
        registrationBean.addInitParameter(JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        return registrationBean;
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(ApplicationInitializer.class);
    }

    public static void main(final String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
}
