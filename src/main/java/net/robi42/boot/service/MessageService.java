package net.robi42.boot.service;

import net.robi42.boot.domain.Message;
import net.robi42.boot.search.ElasticsearchProvider.SearchHitDto;
import net.robi42.boot.util.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String text);

    Message get(UUID id) throws NotFoundException;

    List<Message> getAll();

    Message update(UUID id, String text) throws NotFoundException;

    void delete(UUID id);

    List<SearchHitDto> search(String term);
}
