package net.robi42.boot.service;

import net.robi42.boot.domain.MessageEntity;

import java.util.Date;

import static java.util.UUID.randomUUID;

public class MessageEntityFactoryImpl implements MessageEntityFactory {

    public @Override MessageEntity newMessage(String text) {
        return MessageEntity.builder()
                .id(randomUUID().toString())
                .lastModifiedAt(new Date())
                .body(text)
                .build();
    }
}
