package net.robi42.boot.util;

import net.robi42.boot.domain.Message;

import java.util.Date;

import static java.util.UUID.randomUUID;

public interface MessageFactory {

    default Message newMessage(String text) {
        return Message.builder()
                .id(randomUUID().toString())
                .lastModifiedAt(new Date())
                .body(text)
                .build();
    }

    class Impl implements MessageFactory {}
}
