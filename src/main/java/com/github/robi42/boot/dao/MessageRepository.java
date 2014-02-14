package com.github.robi42.boot.dao;

import com.github.robi42.boot.domain.Message;

import java.util.List;

public interface MessageRepository {
    List<Message> findAll();
}
