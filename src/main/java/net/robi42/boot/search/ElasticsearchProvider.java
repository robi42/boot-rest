package net.robi42.boot.search;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;

public interface ElasticsearchProvider {
    List<SearchHitDto> search(String indexName, QueryBuilder query);
}
