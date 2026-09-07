package com.search.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RangeFacetCount {
    private String key;
    private Double from;
    private Double to;
    private long count;
}
