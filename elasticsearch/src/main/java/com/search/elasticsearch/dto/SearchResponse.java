package com.search.elasticsearch.dto;

import com.search.elasticsearch.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class SearchResponse {
    private Page<Product> products;
    private FacetResponse facets;
}
