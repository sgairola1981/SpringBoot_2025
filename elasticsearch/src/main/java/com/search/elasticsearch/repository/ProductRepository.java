package com.search.elasticsearch.repository;

import com.search.elasticsearch.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    Page<Product> findByCategory(String category, Pageable pageable);

    Page<Product> findByBrandAndInStock(String brand, boolean inStock, Pageable pageable);
}
