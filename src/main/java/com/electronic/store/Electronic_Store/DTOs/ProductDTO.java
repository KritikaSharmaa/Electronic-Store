package com.electronic.store.Electronic_Store.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    @Min(value = 10, message = "Min 10 characters allowed")
    private String description;
    @Min(value = 5, message = "Min 5 characters allowed")
    private String shortDescription;
    private Double price;
    private Integer quantity;
    private Boolean inStock;
    private Double avgRatings;
    private Integer numberOfRatings;
    private String productImage;
    private CategoryDTO category;
}
