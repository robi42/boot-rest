package net.robi42.boot.search;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ElasticsearchProviderImpl implements ElasticsearchProvider {
    private final @NonNull ElasticsearchOperations template;

    public @Override List<SearchHitDto> search(@NonNull QueryBuilder query) {
        return template.query(new NativeSearchQuery(query), response ->
                newArrayList(response.getHits()).stream().map(this::convert).collect(toList()));
    }

    private SearchHitDto convert(SearchHit hit) {
        return SearchHitDto.builder()
                .id(hit.id())
                .score(hit.score())
                .source(hit.getSource())
                .build();
    }
}
