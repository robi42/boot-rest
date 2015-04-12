package net.robi42.boot;

import lombok.val;
import net.robi42.boot.dao.RepositoryRoot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.ws.rs.core.MediaType;

import static com.google.common.base.MoreObjects.firstNonNull;
import static org.springframework.boot.context.embedded.MimeMappings.DEFAULT;

@Import({BeanConfig.class}) @EnableElasticsearchRepositories(basePackageClasses = RepositoryRoot.class)
public @SpringBootApplication class Application implements EmbeddedServletContainerCustomizer {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public static String version() {
        val implementationVersion = Application.class.getPackage().getImplementationVersion();
        return firstNonNull(implementationVersion, "DEV-SNAPSHOT");
    }

    public @Override void customize(ConfigurableEmbeddedServletContainer container) {
        val mappings = new MimeMappings(DEFAULT);
        mappings.add("html", MediaType.TEXT_HTML + "; charset=UTF-8");
        mappings.add("woff", "application/font-woff; charset=UTF-8");
        container.setMimeMappings(mappings);
    }
}
