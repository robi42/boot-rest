package com.github.robi42.boot.util;

import com.github.robi42.boot.domain.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

import static java.util.UUID.randomUUID;

@Service
public class TestFixtureFactory {

    public Message message(final String text) {
        return Message.builder()
                .id(randomUUID().toString())
                .body(text)
                .lastModifiedAt(new Date())
                .build();
    }
}
