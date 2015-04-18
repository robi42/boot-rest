package net.robi42.boot.service;

import net.robi42.boot.domain.MessageDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    MessageDto create(String text);

    Optional<MessageDto> get(UUID id);

    List<MessageDto> getAll();

    Optional<MessageDto> update(UUID id, String text);

    void delete(UUID id);

    List<MessageDto> search(String term);
}
