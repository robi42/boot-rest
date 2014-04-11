package com.github.robi42.boot.rest;

import com.github.robi42.boot.dao.MessageRepository;
import com.github.robi42.boot.domain.util.BeanValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
public class RestBeanConfig {
    @Inject
    private MessageRepository messageRepository;
    @Inject
    private BeanValidator beanValidator;

    @Bean
    public MessageResource messageResource() {
        return new MessageResource(messageRepository, beanValidator);
    }
}
