package com.github.robi42.boot.dao;

import com.github.robi42.boot.domain.Message;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ElasticsearchCrudRepository<Message, String> {}
