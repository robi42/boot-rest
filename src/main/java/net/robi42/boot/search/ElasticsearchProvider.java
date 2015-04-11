package net.robi42.boot.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;
import java.util.Map;

public interface ElasticsearchProvider {
    List<SearchHitDto> search(QueryBuilder query);

    @NoArgsConstructor @AllArgsConstructor
    @Data @Builder class SearchHitDto {
        private String id;
        private float score;
        private Map<String, Object> source;
    }
}
