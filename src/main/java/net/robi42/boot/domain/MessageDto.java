package net.robi42.boot.domain;

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
    private @NotEmpty @Size(max = 140) String body;
}
