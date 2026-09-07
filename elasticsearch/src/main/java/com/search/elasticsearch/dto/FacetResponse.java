package com.search.elasticsearch.dto;

import lombok.Data;

import java.util.List;

@Data
public class FacetResponse {
    private List<FacetCount> brandFacets;
    private List<FacetCount> categoryFacets;
    private List<RangeFacetCount> priceRangeFacets;
    private List<FacetCount> ratingFacets;
}
