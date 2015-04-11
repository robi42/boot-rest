package net.robi42.boot.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.robi42.boot.dao.MessageRepository;
import net.robi42.boot.domain.Message;
import net.robi42.boot.search.ElasticsearchProvider;
import net.robi42.boot.search.ElasticsearchProvider.SearchHitDto;
import net.robi42.boot.util.NotFoundException;
import org.elasticsearch.ElasticsearchException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;

@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final @NonNull MessageRepository repository;
    private final @NonNull ElasticsearchProvider searchProvider;

    public @Override Message create(String text) {
        val message = Message.builder()
                .id(randomUUID().toString())
                .lastModifiedAt(new Date())
                .body(text)
                .build();
        return repository.save(message);
    }

    public @Override Message get(UUID id) {
        val message = repository.findOne(id.toString());
        return Optional.ofNullable(message).orElseThrow(() ->
                new NotFoundException(String.format("Message with ID '%s' not found", id)));
    }

    public @Override List<Message> getAll() {
        return newArrayList(repository.findAll());
    }

    public @Override Message update(UUID id, String text) {
        val message = get(id);
        message.setBody(text);
        message.setLastModifiedAt(new Date());
        return repository.save(message);
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
