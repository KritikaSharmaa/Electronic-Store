package com.electronic.store.Electronic_Store.Services;

import com.electronic.store.Electronic_Store.DTOs.CategoryDTO;
import com.electronic.store.Electronic_Store.DTOs.PaginatedResponseDTO;
import com.electronic.store.Electronic_Store.DTOs.ProductDTO;
import org.springframework.http.ResponseEntity;


public interface CategoryService {
    //1. Create Category
    CategoryDTO createCategory(CategoryDTO catDTO);

    //2. Update Category
    CategoryDTO updateCategory(CategoryDTO catDTO, Long catId);

    //3. Delete Category
    CategoryDTO deleteCategory(Long catId);

    //4. Get All Category
    PaginatedResponseDTO<CategoryDTO> getAllCategory(int pageNum, int pageSize, String sortBy, String sortDir);

    //5. Get CategoryById
    CategoryDTO getCategoryById(Long catId);

    //6. Upload CategoryCoverImage
    void uploadCategoryCoverImage(String fileName, Long catId);

    //7. Create Product and assign a category to it [Category already exists]
    ResponseEntity<ProductDTO> createProductWithCategoryAssigned(Long catId , ProductDTO productDTO);

    //8. Assign category to already created product
    ResponseEntity<ProductDTO> assignCategoryToProduct(Long catId, Long pid);

    //9. Return all products that belong to the given category ID

    PaginatedResponseDTO<ProductDTO> getProductsByCategory(Long catId, int pageNum, int pageSize, String sortBy, String sortDir);
}
