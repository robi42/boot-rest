package net.robi42.boot.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@RequiredArgsConstructor
public @Provider class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    private final @NonNull ObjectMapper objectMapper;

    public @Override ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}
