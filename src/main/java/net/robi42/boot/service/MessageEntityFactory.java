package net.robi42.boot.service;

import net.robi42.boot.domain.MessageEntity;

import java.util.Date;

import static java.util.UUID.randomUUID;

public interface MessageEntityFactory {

    default MessageEntity newMessage(String text) {
        return MessageEntity.builder()
                .id(randomUUID().toString())
                .lastModifiedAt(new Date())
                .body(text)
                .build();
    }

    class DefaultImpl implements MessageEntityFactory {}
}
