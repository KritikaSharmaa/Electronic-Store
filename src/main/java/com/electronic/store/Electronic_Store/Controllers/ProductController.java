package com.electronic.store.Electronic_Store.Controllers;

import com.electronic.store.Electronic_Store.DTOs.ProductDTO;
import com.electronic.store.Electronic_Store.DTOs.PaginatedResponseDTO;
import com.electronic.store.Electronic_Store.Services.FileStorageService;
import com.electronic.store.Electronic_Store.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    FileStorageService fileStorageService;

    @PostMapping
    ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<PaginatedResponseDTO<ProductDTO>> getAllProducts(
            @RequestParam(value="pageNum", defaultValue = "0", required = false ) int pageNum,
            @RequestParam(value="pageSize", defaultValue = "3", required = false ) int pageSize,
            @RequestParam(value="sortBy", defaultValue = "productName", required = false ) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc", required = false ) String sortDir
    ){
        return new ResponseEntity<>(productService.getAllProducts(pageNum,pageSize,sortBy,sortDir), HttpStatus.OK);
    }

    @GetMapping("/{pid}")
    ResponseEntity<ProductDTO> getProductById(@PathVariable("pid") Long pid){
        return new ResponseEntity<>(productService.getProductById(pid), HttpStatus.OK);
    }

    @PatchMapping("/{pid}")
    ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable("pid") Long pid){
        return new ResponseEntity<>(productService.updateProduct(productDTO, pid), HttpStatus.OK);
    }

    @DeleteMapping("/{pid}")
    ResponseEntity<ProductDTO> deleteProduct(@PathVariable("pid") Long pid){
        fileStorageService.deleteUploadedImage(pid, "products");
        return new ResponseEntity<>(productService.deleteProduct(pid), HttpStatus.OK);
    }

    @GetMapping("/inStock/{inStock}")
    ResponseEntity<PaginatedResponseDTO<ProductDTO>> getProductByInStockValue(@PathVariable("inStock") boolean inStock,
                                              @RequestParam(value="pageNum", defaultValue = "0", required = false ) int pageNum,
                                              @RequestParam(value="pageSize", defaultValue = "3", required = false ) int pageSize,
                                              @RequestParam(value="sortBy", defaultValue = "productName", required = false ) String sortBy,
                                              @RequestParam(value="sortDir", defaultValue = "asc", required = false ) String sortDir
                                              ){
        return new ResponseEntity<>(productService.getProductByInStockStatus(inStock, pageNum, pageSize, sortBy,sortDir), HttpStatus.OK);
    }

    @GetMapping("/priceInRange")
    ResponseEntity<PaginatedResponseDTO<ProductDTO>> getProductInPriceRange(  @RequestParam(value = "minPrice", required = false) Double minPrice,
                                                                              @RequestParam(value = "maxPrice", required = false) Double maxPrice,
                                                                              @RequestParam(value="pageNum", defaultValue = "0", required = false ) int pageNum,
                                                                              @RequestParam(value="pageSize", defaultValue = "3", required = false ) int pageSize,
                                                                              @RequestParam(value="sortBy", defaultValue = "productName", required = false ) String sortBy,
                                                                              @RequestParam(value="sortDir", defaultValue = "asc", required = false ) String sortDir
    ){
        return new ResponseEntity<>(productService.getProductInPriceRange(minPrice, maxPrice, pageNum, pageSize, sortBy,sortDir), HttpStatus.OK);
    }

    @GetMapping("/searchProducts")
    ResponseEntity<PaginatedResponseDTO<ProductDTO>> SearchProductByKeyword(  @RequestParam(value = "keyword", required = false) String keyword,
                                                                              @RequestParam(value="pageNum", defaultValue = "0", required = false ) int pageNum,
                                                                              @RequestParam(value="pageSize", defaultValue = "3", required = false ) int pageSize,
                                                                              @RequestParam(value="sortBy", defaultValue = "productName", required = false ) String sortBy,
                                                                              @RequestParam(value="sortDir", defaultValue = "asc", required = false ) String sortDir
    ){
        return new ResponseEntity<>(productService.SearchProductByKeyword(keyword, pageNum, pageSize, sortBy,sortDir), HttpStatus.OK);
    }

    @PostMapping(value="/uploadProductImage/{pid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> uploadProductImage(@RequestPart("file") MultipartFile file, @PathVariable("pid") Long pid){
        try{
            String filename = fileStorageService.uploadFile(file,"products");
            productService.uploadProductImage(filename, pid);
            return ResponseEntity.ok("Image uploaded: " + filename);
        }catch (Exception e) {
            e.printStackTrace();  // or use logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/serveProductImage/{pid}")
    public ResponseEntity<Resource> serveProductImage (@PathVariable("pid") Long pid) throws IOException {
        return fileStorageService.getUploadedImage(pid, "products");
    }
}
