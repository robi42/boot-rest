package net.robi42.boot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor
@Document(indexName = Message.INDEX_NAME, type = Message.TYPE_NAME)
public @Data @Builder class Message {
    public static final String INDEX_NAME = "messages";
    public static final String TYPE_NAME = "message";

    // Note: Spring Data ES doesn't support `UUID` typed IDs (yet?)
    private String id;
    private Date lastModifiedAt;
    private String body;

    public MessageDto toDto() {
        val dto = new MessageDto();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }
}
