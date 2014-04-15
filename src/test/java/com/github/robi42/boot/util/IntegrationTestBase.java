package com.github.robi42.boot.util;

import com.github.robi42.boot.TestApplicationInitializer;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationInitializer.class, loader = SpringApplicationContextLoader.class)
@ActiveProfiles("test")
public abstract class IntegrationTestBase {
    @Value("${server.port}")
    private int serverPort;
    @Inject
    private Client webClient;
    protected WebTarget restApi;

    @Before
    public void setUp() {
        restApi = webClient.target(String.format("http://localhost:%d/api", serverPort));
    }
}
