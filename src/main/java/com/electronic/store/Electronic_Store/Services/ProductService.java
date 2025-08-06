package com.electronic.store.Electronic_Store.Services;

import com.electronic.store.Electronic_Store.DTOs.PaginatedResponseDTO;
import com.electronic.store.Electronic_Store.DTOs.ProductDTO;

public interface ProductService {
    //1. Create Product
    ProductDTO createProduct(ProductDTO productDTO);

    //2. Update Product
    ProductDTO updateProduct(ProductDTO productDTO, Long pid);

    //3. Delete Product
    ProductDTO deleteProduct(Long pid);

    //4. Get Product by ID
    ProductDTO getProductById(Long pid);

    //5. Get All Products (with pagination)
    PaginatedResponseDTO<ProductDTO> getAllProducts(int pageNum, int pageSize, String sortBy, String sortDir);

    //✅ Filtering / Sorting APIs (Optional but useful)
    //6. Get Products by inStock status
    PaginatedResponseDTO<ProductDTO> getProductByInStockStatus(boolean inStock, int pageNum, int pageSize, String sortBy, String sortDir);

    //7. Get Products by min/max price range
    PaginatedResponseDTO<ProductDTO> getProductInPriceRange(Double minPrice, Double maxPrice, int pageNum, int pageSize, String sortBy, String sortDir);

    //8. Search Products by name or short description (contains keyword)
    PaginatedResponseDTO<ProductDTO> SearchProductByKeyword(String keyword, int pageNum, int pageSize, String sortBy, String sortDir);

    //9. Upload Product Image
    void uploadProductImage(String filename, Long pid);

}
