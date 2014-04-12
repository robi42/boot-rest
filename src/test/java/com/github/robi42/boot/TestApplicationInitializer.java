package com.github.robi42.boot;

import com.github.robi42.boot.dao.RepositoryRoot;
import com.github.robi42.boot.domain.util.ElasticsearchEntityMapper;
import org.elasticsearch.node.Node;
import org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Configuration
@ComponentScan
@EnableElasticsearchRepositories(basePackageClasses = RepositoryRoot.class)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, ManagementSecurityAutoConfiguration.class})
public class TestApplicationInitializer extends ApplicationInitializer {

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        final Node node = nodeBuilder()
                .settings(settingsBuilder().loadFromClasspath("elasticsearch-test.yml"))
                .clusterName(elasticsearchClusterName)
                .data(true).local(true)
                .node();
        return new ElasticsearchTemplate(node.client(), new ElasticsearchEntityMapper(objectMapper()));
    }
}
