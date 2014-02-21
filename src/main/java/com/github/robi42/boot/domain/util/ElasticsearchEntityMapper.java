package com.github.robi42.boot.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;

public class ElasticsearchEntityMapper implements EntityMapper {
    public static final String ISO_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private final ObjectMapper objectMapper;

    public ElasticsearchEntityMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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
