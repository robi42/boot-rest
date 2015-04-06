package net.robi42.boot.util;

import net.robi42.boot.TestApplication;
import org.junit.Before;
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
@SpringApplicationConfiguration(classes = TestApplication.class)
@WebAppConfiguration @ActiveProfiles("test")
public abstract @IntegrationTest class IntegrationTestBase {
    @Value("${local.server.port}") int port;
    @Inject Client webClient;

    protected WebTarget restApi;

    @Before
    public void setUp() {
        restApi = webClient.target(String.format("http://localhost:%d/api", port));
    }
}
