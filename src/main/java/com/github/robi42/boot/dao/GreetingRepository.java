package com.github.robi42.boot.dao;

import com.github.robi42.boot.domain.Message;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
@Repository
public class GreetingRepository implements MessageRepository {
    private final String nameToGreet;

    @Inject
    public GreetingRepository(final @Value("${name:World}") String nameToGreet) {
        this.nameToGreet = nameToGreet;
    }

    @Override
    public List<Message> findAll() {
        final Message greeting = Message.builder()
                .body(String.format("Hello %s! The time is %s",
                        nameToGreet, LocalDateTime.now().format(ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))))
                .build();
        final List<Message> greetings = ImmutableList.of(greeting);

        if (log.isDebugEnabled()) {
            greetings.forEach(g -> log.debug("Yippie! {}", g.getBody()));
        }
        return greetings;
    }
}
