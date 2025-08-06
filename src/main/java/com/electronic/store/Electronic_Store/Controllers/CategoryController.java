package com.electronic.store.Electronic_Store.Controllers;

import com.electronic.store.Electronic_Store.DTOs.CategoryDTO;
import com.electronic.store.Electronic_Store.DTOs.PaginatedResponseDTO;
import com.electronic.store.Electronic_Store.DTOs.ProductDTO;
import com.electronic.store.Electronic_Store.Services.CategoryService;
import com.electronic.store.Electronic_Store.Services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    FileStorageService fileStorageService;

    @PostMapping
    ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO catDTO){
        CategoryDTO categories = categoryService.createCategory(catDTO);
        return new ResponseEntity<>(categories, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<PaginatedResponseDTO<CategoryDTO>> getAllCategories(
            @RequestParam(value="pageNum", defaultValue = "0", required = false ) int pageNum,
            @RequestParam(value="pageSize", defaultValue = "3", required = false ) int pageSize,
            @RequestParam(value="sortBy", defaultValue = "title", required = false ) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc", required = false ) String sortDir
    ){
        return new ResponseEntity<>(categoryService.getAllCategory(pageNum,pageSize,sortBy,sortDir), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("catId") Long catId){
        return new ResponseEntity<>(categoryService.getCategoryById(catId), HttpStatus.OK);
    }

    @PatchMapping("/{catId}")
    ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable("catId") Long catId){
        return new ResponseEntity<>(categoryService.updateCategory(categoryDTO, catId), HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    ResponseEntity<CategoryDTO> deleteCategory(@PathVariable("catId") Long catId){
        fileStorageService.deleteUploadedImage(catId,"categories");
        return new ResponseEntity<>(categoryService.deleteCategory(catId), HttpStatus.OK);
    }

    @PostMapping(value="/uploadCategoryCoverImage/{catId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> uploadCategoryCoverImage(@RequestPart("file") MultipartFile file, @PathVariable("catId") Long catId){
        try{
            String filename = fileStorageService.uploadFile(file,"categories");
            categoryService.uploadCategoryCoverImage(filename, catId);
            return ResponseEntity.ok("Image uploaded: " + filename);
        }catch (Exception e) {
            e.printStackTrace();  // or use logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/getCategoryCoverImage/{catId}")
    public ResponseEntity<Resource> getProfileImage (@PathVariable("catId") Long catId) throws IOException {
        return fileStorageService.getUploadedImage(catId, "categories");
    }

    @PostMapping("/{cid}/products")
    public ResponseEntity<ProductDTO> createProductWithCategoryAssigned(@PathVariable("cid") Long cid, @RequestBody ProductDTO productDTO){
        return categoryService.createProductWithCategoryAssigned(cid, productDTO);
    }

    @PutMapping("/{cid}/products/{pid}")
    public ResponseEntity<ProductDTO> assignCategoryToProduct(@PathVariable("cid") Long cid, @PathVariable("pid") Long pid){
        return  categoryService.assignCategoryToProduct(cid, pid);
    }

    @GetMapping("/{cid}/products")
    public PaginatedResponseDTO<ProductDTO> getProductsByCategory(@PathVariable("cid") Long cid,
                                                                  @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
                                                                  @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                                                  @RequestParam(value = "sortBy", defaultValue = "productName", required = false) String sortBy,
                                                                  @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return categoryService.getProductsByCategory(cid, pageNum, pageSize, sortBy, sortDir);
    }

}
