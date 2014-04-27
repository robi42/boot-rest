package com.github.robi42.boot.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchHitDto {
    private String id;
    private float score;
    private Map<String, Object> source;
}
