package net.robi42.boot.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;

@RequiredArgsConstructor
public class ElasticsearchEntityMapper implements EntityMapper {
    private final @NonNull ObjectMapper objectMapper;

    public @Override String mapToString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public @Override <T> T mapToObject(String source, Class<T> clazz) throws IOException {
        return objectMapper.readValue(source, clazz);
    }
}
