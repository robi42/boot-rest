package com.github.robi42.boot;

import com.github.robi42.boot.domain.util.ElasticsearchEntityMapper;
import org.elasticsearch.node.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Configuration
public class TestBeanConfig extends BeanConfig {

    @Bean
    @Override
    public ElasticsearchOperations elasticsearchTemplate() {
        final Node node = nodeBuilder()
                .settings(settingsBuilder().loadFromClasspath("elasticsearch-test.yml"))
                .clusterName(elasticsearchClusterName)
                .data(true).local(true)
                .node();
        return new ElasticsearchTemplate(node.client(), new ElasticsearchEntityMapper(objectMapper()));
    }
}
