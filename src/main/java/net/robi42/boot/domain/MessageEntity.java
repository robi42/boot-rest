package net.robi42.boot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor
@Document(indexName = MessageEntity.INDEX_NAME, type = MessageEntity.TYPE_NAME)
public @Data @Builder class MessageEntity {
    public static final String INDEX_NAME = "messages";
    public static final String TYPE_NAME = "message";

    // Note: Spring Data ES doesn't support `UUID` typed IDs (yet?)
    private String id;
    private Date lastModifiedAt;
    private String body;
}
