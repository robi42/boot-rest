package net.robi42.boot.util;

import net.robi42.boot.domain.Message;
import org.springframework.stereotype.Service;

import java.util.Date;

import static java.util.UUID.randomUUID;

public @Service class TestFixtureFactory {

    public Message message(final String text) {
        return Message.builder()
                .id(randomUUID().toString())
                .body(text)
                .lastModifiedAt(new Date())
                .build();
    }
}
