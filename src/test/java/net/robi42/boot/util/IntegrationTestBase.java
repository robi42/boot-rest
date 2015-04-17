package net.robi42.boot.util;

import net.robi42.boot.TestApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

@RunWith(SpringJUnit4ClassRunner.class) @ActiveProfiles("test")
@SpringApplicationConfiguration(classes = TestApplication.class)
public abstract @WebIntegrationTest class IntegrationTestBase {
    @Value("${local.server.port}") int port;
    @Inject Client webClient;

    protected WebTarget restApi;

    @Before
    public void setUp() {
        restApi = webClient.target(String.format("http://localhost:%d/api", port));
    }
}
