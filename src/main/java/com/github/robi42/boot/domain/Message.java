package com.github.robi42.boot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.github.robi42.boot.domain.util.ElasticsearchEntityMapper.ISO_DATE_TIME_FORMAT_PATTERN;
import static org.springframework.data.elasticsearch.annotations.FieldType.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "messages", type = "message")
public class Message {
    // Note: Spring Data ES doesn't support `UUID` typed IDs (yet?)
    private String id;
    @Field(type = Date, pattern = ISO_DATE_TIME_FORMAT_PATTERN)
    private LocalDateTime lastModifiedAt;
    private String body;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Input {
        @NotEmpty
        @Size(max = 140)
        private String body;
    }
}
