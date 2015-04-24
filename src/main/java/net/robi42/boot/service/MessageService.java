package net.robi42.boot.service;

import net.robi42.boot.domain.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message create(String text);

    Optional<Message> get(UUID id);

    List<Message> getAll();

    Optional<Message> update(UUID id, String text);

    void delete(UUID id);

    List<Message> search(String term);
}
