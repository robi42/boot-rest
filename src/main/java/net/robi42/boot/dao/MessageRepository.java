package net.robi42.boot.dao;

import net.robi42.boot.domain.Message;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface MessageRepository extends ElasticsearchCrudRepository<Message, String> {}
