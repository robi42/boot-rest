package net.robi42.boot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor @AllArgsConstructor
public @Data @Builder class MessageDto {
    private String id;
    private Date lastModifiedAt;
    private String body;

    @NoArgsConstructor @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static @Data class Input {
        private @NotEmpty @Size(max = 140) String body;
    }
}
