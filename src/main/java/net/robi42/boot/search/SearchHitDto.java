package net.robi42.boot.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor @AllArgsConstructor
public @Data @Builder class SearchHitDto {
    private String id;
    private float score;
    private Map<String, Object> source;
}
