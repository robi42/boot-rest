package net.robi42.boot.service;

import net.robi42.boot.domain.MessageEntity;

public interface MessageEntityFactory {
    MessageEntity newMessage(String text);
}
