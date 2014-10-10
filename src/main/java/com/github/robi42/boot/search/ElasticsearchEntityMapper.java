package com.github.robi42.boot.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;

public class ElasticsearchEntityMapper implements EntityMapper {
    public static final String ISO_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private final ObjectMapper objectMapper;

    public ElasticsearchEntityMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String mapToString(final Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    @Override
    public <T> T mapToObject(final String source, final Class<T> clazz) throws IOException {
        return objectMapper.readValue(source, clazz);
    }
}