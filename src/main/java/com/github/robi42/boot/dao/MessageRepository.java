package com.github.robi42.boot.dao;

import com.github.robi42.boot.domain.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    List<Message> findAll();

    Optional<Message> findById(UUID id);
}
