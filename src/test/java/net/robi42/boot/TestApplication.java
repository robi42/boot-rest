package net.robi42.boot;

import net.robi42.boot.dao.RepositoryRoot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Import(TestBeanConfig.class) @EnableElasticsearchRepositories(basePackageClasses = RepositoryRoot.class)
public @SpringBootApplication class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
