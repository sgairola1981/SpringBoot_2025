package com.search.elasticsearch.controller;

import com.search.elasticsearch.dto.ProductSearchRequest;
import com.search.elasticsearch.dto.SearchResponse;
import com.search.elasticsearch.entity.Product;
import com.search.elasticsearch.service.ProductSearchService;
import com.search.elasticsearch.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductWebController {

    private final ProductSearchService searchService;
    private final ProductService productService;

    @GetMapping
    public String search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "relevance") String sort,
            Model model
    ) {
        ProductSearchRequest req = new ProductSearchRequest();
        req.setQ(q);
        req.setBrand(brand);
        req.setCategory(category);
        req.setInStock(inStock);
        req.setMinPrice(minPrice);
        req.setMaxPrice(maxPrice);
        req.setMinRating(minRating);

        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        SearchResponse response = searchService.search(req, pageable);

        model.addAttribute("products", response.getProducts());
        model.addAttribute("facets", response.getFacets());
        model.addAttribute("request", req);
        model.addAttribute("page", response.getProducts());
        model.addAttribute("sort", sort);

        return "products/search";
    }

    @GetMapping("/new")
    public String showNewProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/product-form";
    }

    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "products/product-form";
        }

        if (product.getId() == null || product.getId().isBlank()) {
            product.setId(java.util.UUID.randomUUID().toString());
        }

        productService.save(product);
        return "redirect:/products";
    }

    private Sort parseSort(String sort) {
        return switch (sort) {
            case "price_asc" -> Sort.by(Sort.Order.asc("price"));
            case "price_desc" -> Sort.by(Sort.Order.desc("price"));
            case "rating_desc" -> Sort.by(Sort.Order.desc("rating"));
            default -> Sort.unsorted();
        };
    }
}