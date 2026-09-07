package com.search.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacetCount {
    private String value;
    private long count;
}
