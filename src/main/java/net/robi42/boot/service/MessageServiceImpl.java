package net.robi42.boot.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.robi42.boot.dao.MessageRepository;
import net.robi42.boot.domain.MessageDto;
import net.robi42.boot.domain.MessageEntity;
import net.robi42.boot.search.ElasticsearchProvider;
import net.robi42.boot.search.ElasticsearchProvider.SearchHitDto;
import net.robi42.boot.util.MessageEntityFactory;
import org.elasticsearch.ElasticsearchException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;

@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final @NonNull MessageRepository repository;
    private final @NonNull MessageEntityFactory factory;
    private final @NonNull DtoConverter<MessageEntity, MessageDto> dtoConverter;
    private final @NonNull ElasticsearchProvider searchProvider;

    public @Override MessageDto create(String text) {
        val messageToSave = factory.newMessage(text);
        val savedMessage = repository.save(messageToSave);
        return dtoConverter.convert(savedMessage).get();
    }

    public @Override Optional<MessageDto> get(UUID id) {
        val entity = repository.findOne(id.toString());
        return dtoConverter.convert(entity);
    }

    public @Override List<MessageDto> getAll() {
        val entities = repository.findAll();
        return dtoConverter.convert(entities);
    }

    public @Override Optional<MessageDto> update(UUID id, String text) {
        val messageToUpdate = repository.findOne(id.toString());
        if (messageToUpdate == null) {
            return Optional.empty();
        }
        messageToUpdate.setBody(text);
        messageToUpdate.setLastModifiedAt(new Date());
        val updatedMessage = repository.save(messageToUpdate);
        return dtoConverter.convert(updatedMessage);
    }

    public @Override void delete(UUID id) {
        repository.delete(id.toString());
    }

    public @Override List<SearchHitDto> search(String term) {
        try {
            return searchProvider.search(matchPhrasePrefixQuery("body", term));
        } catch (ElasticsearchException e) {
            return emptyList();
        }
    }
}
