package com.github.robi42.boot.rest.util;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ws.rs.client.ClientBuilder.newClient;

public abstract class IntegrationTestBase {
    protected WebTarget restApi;

    private static Client webClient;
    private static int serverPort;
    private static ConfigurableApplicationContext applicationContext;

    @BeforeClass
    public static void init() throws InterruptedException, ExecutionException, TimeoutException {
        final Future<ConfigurableApplicationContext> applicationContextFuture = newSingleThreadExecutor()
                .submit(() -> {
                    System.setProperty("spring.profiles.active", "test");
                    return SpringApplication.run(TestApplicationInitializer.class);
                });
        applicationContext = applicationContextFuture.get(30, SECONDS);
        serverPort = applicationContext.getEnvironment()
                .getProperty("server.port", int.class);
        final JacksonJsonProvider jacksonJsonProvider = applicationContext.getBean(JacksonJsonProvider.class);
        webClient = newClient().register(jacksonJsonProvider);
    }

    @Before
    public void setUp() {
        restApi = webClient.target(String.format("http://localhost:%d/api", serverPort));
    }

    @AfterClass
    public static void cleanUp() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }
}
