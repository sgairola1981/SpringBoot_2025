package com.search.elasticsearch.dto;

import com.search.elasticsearch.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ProductSearchRequest {
    private String q;
    private String brand;
    private String category;
    private Boolean inStock;
    private Double minPrice;
    private Double maxPrice;
    private Integer minRating;

    private boolean facetBrand = true;
    private boolean facetCategory = true;
    private boolean facetPriceRanges = true;
    private boolean facetRating = true;
}

