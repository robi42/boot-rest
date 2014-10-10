package net.robi42.boot.util;

import net.robi42.boot.TestApplicationInitializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplicationInitializer.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles("test")
public abstract class IntegrationTestBase {
    @Value("${server.port}")
    private int serverPort;
    @Inject
    private Client webClient;
    protected WebTarget restApi;

    @BeforeClass
    public static void init() {
        System.setProperty("spring.profiles.active", "test");
    }

    @Before
    public void setUp() {
        restApi = webClient.target(String.format("http://localhost:%d/api", serverPort));
    }
}
