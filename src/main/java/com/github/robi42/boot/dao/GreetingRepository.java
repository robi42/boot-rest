package com.github.robi42.boot.dao;

import com.github.robi42.boot.domain.Message;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.UUID.randomUUID;

@Slf4j
@Repository
public class GreetingRepository implements MessageRepository {
    private final String nameToGreet;
    private final List<Message> greetings;

    @Inject
    public GreetingRepository(final @Value("${name:world}") String nameToGreet) {
        this.nameToGreet = nameToGreet;
        greetings = ImmutableList.of(greeting(), greeting());
    }

    @Override
    public List<Message> findAll() {
        if (log.isDebugEnabled()) {
            greetings.forEach(greeting -> log.debug("Yippie! {}", greeting.getBody()));
        }
        return greetings;
    }

    @Override
    public Optional<Message> findById(final UUID id) {
        return greetings.parallelStream()
                .filter(greeting -> greeting.getId().equals(id))
                .findAny();
    }

    private Message greeting() {
        final LocalDateTime now = LocalDateTime.now();
        return Message.builder()
                .id(randomUUID())
                .lastModifiedAt(now)
                .body(String.format("Hello, %s! The time is: %s",
                        nameToGreet, now.format(ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))))
                .build();
    }
}
