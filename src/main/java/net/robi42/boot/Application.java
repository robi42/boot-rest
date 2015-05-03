package net.robi42.boot;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.robi42.boot.dao.RepositoryRoot;
import net.robi42.boot.util.Manifest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.ws.rs.core.MediaType;

import static org.springframework.boot.context.embedded.MimeMappings.DEFAULT;

@Import(BeanConfig.class) @EnableElasticsearchRepositories(basePackageClasses = RepositoryRoot.class)
public @SpringBootApplication @Slf4j class Application implements EmbeddedServletContainerCustomizer {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("Version: {}", Manifest.version());
    }

    public @Override void customize(ConfigurableEmbeddedServletContainer container) {
        val mappings = new MimeMappings(DEFAULT);
        mappings.add("html", MediaType.TEXT_HTML + "; charset=UTF-8");
        mappings.add("woff", "application/font-woff; charset=UTF-8");
        mappings.add("woff2", "application/font-woff2; charset=UTF-8");
        container.setMimeMappings(mappings);
    }
}
