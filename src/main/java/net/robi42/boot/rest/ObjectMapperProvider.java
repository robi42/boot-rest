package net.robi42.boot.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public @Provider class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    private final @NonNull ObjectMapper objectMapper;

    public @Override ObjectMapper getContext(Class<?> aClass) {
        return objectMapper;
    }
}
