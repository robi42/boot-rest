package net.robi42.boot.dao;

import net.robi42.boot.domain.Message;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

public interface MessageRepository extends ElasticsearchCrudRepository<Message, String> {
    List<Message> findByBodyLike(String term);
}
