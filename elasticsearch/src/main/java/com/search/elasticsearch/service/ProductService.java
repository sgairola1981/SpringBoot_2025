package com.search.elasticsearch.service;

import com.search.elasticsearch.entity.Product;
import com.search.elasticsearch.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public void save(Product product) {
        if (product.getId() == null || product.getId().isBlank()) {
            product.setId(UUID.randomUUID().toString());
        }
        repository.save(product);
    }

    public void saveAll(List<Product> products) {
        List<List<Product>> chunks = chunkedList(products, 500);
        for (List<Product> chunk : chunks) {
            repository.saveAll(chunk);
        }
    }

    private List<List<Product>> chunkedList(List<Product> list, int size) {
        List<List<Product>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            chunks.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return chunks;
    }
}