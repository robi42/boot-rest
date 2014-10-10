package net.robi42.boot.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

public class BootElasticsearchProvider implements ElasticsearchProvider {
    private final Client client;

    public BootElasticsearchProvider(final Client client) {
        this.client = client;
    }

    @Override
    public List<SearchHitDto> search(final String indexName, final QueryBuilder query) {
        final SearchResponse response = client.prepareSearch(indexName)
                .setQuery(query).execute()
                .actionGet(10, SECONDS);
        final SearchHits hits = response.getHits();
        return newArrayList(hits).parallelStream()
                .map(this::convert).collect(toList());
    }

    private SearchHitDto convert(final SearchHit hit) {
        return SearchHitDto.builder()
                .id(hit.id())
                .score(hit.score())
                .source(hit.getSource())
                .build();
    }
}
