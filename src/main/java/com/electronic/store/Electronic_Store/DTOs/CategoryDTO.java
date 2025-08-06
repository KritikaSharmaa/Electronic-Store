package com.electronic.store.Electronic_Store.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    @Min(value = 30, message = "Title must be less then 30 letters")
    private String title;
    @Min(value = 50, message = "Description must be less then 50 letters")
    private String description;
    private String coverImage;
}
