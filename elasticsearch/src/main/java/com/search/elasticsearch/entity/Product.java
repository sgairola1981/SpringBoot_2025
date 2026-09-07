package com.search.elasticsearch.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Document(indexName = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    @Id
    private String id;

    @NotBlank(message = "Title is required")
    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @NotBlank(message = "Brand is required")
    @Field(type = FieldType.Keyword)
    private String brand;

    @NotBlank(message = "Category is required")
    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Double)
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be >= 0")
    private Double price;

    @Field(type = FieldType.Integer)
    @Min(value = 0) @Max(value = 5)
    private Integer rating;

    @Field(type = FieldType.Boolean)
    private Boolean inStock = true;
}