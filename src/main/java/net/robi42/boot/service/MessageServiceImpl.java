package net.robi42.boot.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.robi42.boot.dao.MessageRepository;
import net.robi42.boot.domain.Message;
import net.robi42.boot.util.MessageFactory;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final @NonNull MessageRepository repository;
    private final @NonNull MessageFactory factory;

    public @Override Message create(String text) {
        val messageToSave = factory.newMessage(text);
        return repository.save(messageToSave);
    }

    public @Override Optional<Message> get(UUID id) {
        val message = repository.findOne(id.toString());
        return Optional.ofNullable(message);
    }

    public @Override List<Message> getAll() {
        return newArrayList(repository.findAll(new Sort(DESC, "lastModifiedAt")));
    }

    public @Override Optional<Message> update(UUID id, String text) {
        val messageToUpdate = repository.findOne(id.toString());
        return Optional.ofNullable(messageToUpdate)
                .map(message -> updateAndSave(text, message));
    }

    private Message updateAndSave(String text, Message message) {
        message.setBody(text);
        message.setLastModifiedAt(new Date());
        return repository.save(message);
    }

    public @Override void delete(UUID id) {
        repository.delete(id.toString());
    }

    public @Override List<Message> search(String term) {
        return repository.findByBodyLike(term);
    }
}
