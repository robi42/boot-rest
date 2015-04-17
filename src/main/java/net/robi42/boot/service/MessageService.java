package net.robi42.boot.service;

import net.robi42.boot.domain.MessageDto;
import net.robi42.boot.search.ElasticsearchProvider.SearchHitDto;
import net.robi42.boot.util.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(String text);

    MessageDto get(UUID id) throws NotFoundException;

    List<MessageDto> getAll();

    MessageDto update(UUID id, String text) throws NotFoundException;

    void delete(UUID id);

    List<SearchHitDto> search(String term);
}
