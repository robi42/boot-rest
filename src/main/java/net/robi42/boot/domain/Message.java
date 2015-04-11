package net.robi42.boot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.Size;
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

    @NoArgsConstructor @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static @Data class Input {
        private @NotEmpty @Size(max = 140) String body;
    }
}
