package net.robi42.boot.dao;

import net.robi42.boot.domain.MessageEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

public interface MessageRepository extends ElasticsearchCrudRepository<MessageEntity, String> {
    List<MessageEntity> findByBodyLike(String term);
}
