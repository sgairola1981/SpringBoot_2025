package com.search.elasticsearch.service;

import com.search.elasticsearch.dto.FacetCount;
import com.search.elasticsearch.dto.FacetResponse;
import com.search.elasticsearch.dto.ProductSearchRequest;
import com.search.elasticsearch.dto.RangeFacetCount;
import com.search.elasticsearch.dto.SearchResponse;
import com.search.elasticsearch.entity.Product;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

```
    private final ElasticsearchOperations esTemplate;


    public SearchResponse search(
            ProductSearchRequest req,
            Pageable pageable) {

        NativeQuery query = buildQuery(req, pageable);

        SearchHits<Product> hits =
                esTemplate.search(query, Product.class);

        SearchPage<Product> searchPage =
                SearchHitSupport.searchPageFor(
                        hits,
                        pageable
                );

        FacetResponse facets =
                extractFacets(hits, req);

        Page<Product> products =
                (Page<Product>)
                        SearchHitSupport.unwrapSearchHits(
                                searchPage
                        );

        return new SearchResponse(
                products,
                facets
        );
    }


    private NativeQuery buildQuery(
            ProductSearchRequest req,
            Pageable pageable) {

        NativeQueryBuilder builder =
                NativeQuery.builder()
                        .withQuery(q -> q.bool(b -> {

                            /*
                             * Full Text Search
                             */
                            if (req.getQ() != null
                                    && !req.getQ().isBlank()) {

                                b.must(m -> m.multiMatch(mm -> mm
                                        .query(req.getQ())
                                        .fields(
                                                "title^2",
                                                "description"
                                        )
                                ));
                            }


                            /*
                             * Brand Filter
                             */
                            if (req.getBrand() != null
                                    && !req.getBrand().isBlank()) {

                                b.filter(f -> f.term(t -> t
                                        .field("brand.keyword")
                                        .value(req.getBrand())
                                ));
                            }


                            /*
                             * Category Filter
                             */
                            if (req.getCategory() != null
                                    && !req.getCategory().isBlank()) {

                                b.filter(f -> f.term(t -> t
                                        .field("category.keyword")
                                        .value(req.getCategory())
                                ));
                            }


                            /*
                             * In Stock Filter
                             */
                            if (req.getInStock() != null) {

                                b.filter(f -> f.term(t -> t
                                        .field("inStock")
                                        .value(req.getInStock())
                                ));
                            }


                            /*
                             * Price Filter
                             */
                            if (req.getMinPrice() != null
                                    || req.getMaxPrice() != null) {

                                b.filter(f -> f.range(r -> r
                                        .number(n -> {

                                            n.field("price");

                                            if (req.getMinPrice() != null) {
                                                n.gte(req.getMinPrice());
                                            }

                                            if (req.getMaxPrice() != null) {
                                                n.lte(req.getMaxPrice());
                                            }

                                            return n;
                                        })
                                ));
                            }


                            /*
                             * Rating Filter
                             */
                            if (req.getMinRating() != null) {

                                b.filter(f -> f.range(r -> r
                                        .number(n -> n
                                                .field("rating")
                                                .gte(req.getMinRating())
                                        )
                                ));
                            }

                            return b;
                        }))

                        /*
                         * Pagination
                         */
                        .withPageable(pageable);


        /*
         * Brand Facet
         */
        if (req.isFacetBrand()) {

            builder.withAggregation(
                    "brand",
                    Aggregation.of(a -> a
                            .terms(t -> t
                                    .field("brand.keyword")
                                    .size(20)
                            )
                    )
            );
        }


        /*
         * Category Facet
         */
        if (req.isFacetCategory()) {

            builder.withAggregation(
                    "category",
                    Aggregation.of(a -> a
                            .terms(t -> t
                                    .field("category.keyword")
                                    .size(30)
                            )
                    )
            );
        }


        /*
         * Price Range Facet
         */
        if (req.isFacetPriceRanges()) {

            builder.withAggregation(
                    "price",
                    Aggregation.of(a -> a
                            .range(r -> r
                                    .field("price")
                                    .ranges(
                                            rr -> rr.to("500"),
                                            rr -> rr.from("500").to("1000"),
                                            rr -> rr.from("1000").to("2000"),
                                            rr -> rr.from("2000")
                                    )
                            )
                    )
            );
        }


        /*
         * Rating Facet
         */
        if (req.isFacetRating()) {

            builder.withAggregation(
                    "rating",
                    Aggregation.of(a -> a
                            .terms(t -> t
                                    .field("rating")
                                    .size(10)
                            )
                    )
            );
        }


        return builder.build();
    }


    private FacetResponse extractFacets(
            SearchHits<Product> hits,
            ProductSearchRequest req) {

        FacetResponse facets =
                new FacetResponse();

        if (hits.getAggregations() == null) {
            return facets;
        }

        Map<String, Aggregate> aggregations =
                hits.getAggregations()
                        .aggregationsAsMap();

        if (aggregations == null
                || aggregations.isEmpty()) {

            return facets;
        }


        /*
         * Brand Facet
         */
        if (req.isFacetBrand()) {

            facets.setBrandFacets(
                    toStringTermFacet(
                            aggregations.get("brand")
                    )
            );
        }


        /*
         * Category Facet
         */
        if (req.isFacetCategory()) {

            facets.setCategoryFacets(
                    toStringTermFacet(
                            aggregations.get("category")
                    )
            );
        }


        /*
         * Price Range Facet
         */
        if (req.isFacetPriceRanges()) {

            facets.setPriceRangeFacets(
                    toRangeFacet(
                            aggregations.get("price")
                    )
            );
        }


        /*
         * Rating Facet
         */
        if (req.isFacetRating()) {

            facets.setRatingFacets(
                    toRatingFacet(
                            aggregations.get("rating")
                    )
            );
        }

        return facets;
    }


    /*
     * Brand and Category String Terms Facet
     */
    private List<FacetCount> toStringTermFacet(
            Aggregate aggregate) {

        if (aggregate == null
                || !aggregate.isSterms()) {

            return List.of();
        }

        List<FacetCount> result =
                new ArrayList<>();

        for (var bucket :
                aggregate.sterms()
                        .buckets()
                        .array()) {

            result.add(
                    new FacetCount(
                            bucket.key().stringValue(),
                            bucket.docCount()
                    )
            );
        }

        return result;
    }


    /*
     * Rating Facet
     *
     * Supports Long, Double and String values.
     */
    private List<FacetCount> toRatingFacet(
            Aggregate aggregate) {

        if (aggregate == null) {
            return List.of();
        }

        List<FacetCount> result =
                new ArrayList<>();


        /*
         * Long Terms
         */
        if (aggregate.isLterms()) {

            for (var bucket :
                    aggregate.lterms()
                            .buckets()
                            .array()) {

                result.add(
                        new FacetCount(
                                String.valueOf(bucket.key()),
                                bucket.docCount()
                        )
                );
            }

            return result;
        }


        /*
         * Double Terms
         */
        if (aggregate.isDterms()) {

            for (var bucket :
                    aggregate.dterms()
                            .buckets()
                            .array()) {

                result.add(
                        new FacetCount(
                                String.valueOf(bucket.key()),
                                bucket.docCount()
                        )
                );
            }

            return result;
        }


        /*
         * String Terms
         */
        if (aggregate.isSterms()) {

            for (var bucket :
                    aggregate.sterms()
                            .buckets()
                            .array()) {

                result.add(
                        new FacetCount(
                                bucket.key().stringValue(),
                                bucket.docCount()
                        )
                );
            }
        }

        return result;
    }


    /*
     * Price Range Facet
     */
    private List<RangeFacetCount> toRangeFacet(
            Aggregate aggregate) {

        if (aggregate == null
                || !aggregate.isRange()) {

            return List.of();
        }

        List<RangeFacetCount> result =
                new ArrayList<>();

        for (var bucket :
                aggregate.range()
                        .buckets()
                        .array()) {

            Double from = null;
            Double to = null;

            if (bucket.from() != null) {
                from =
                        Double.valueOf(
                                bucket.from()
                        );
            }

            if (bucket.to() != null) {
                to =
                        Double.valueOf(
                                bucket.to()
                        );
            }

            result.add(
                    new RangeFacetCount(
                            bucket.key(),
                            from,
                            to,
                            bucket.docCount()
                    )
            );
        }

        return result;
    }
```

}
