package com.electronic.store.Electronic_Store.Services.Implementations;

import com.electronic.store.Electronic_Store.DTOs.CategoryDTO;
import com.electronic.store.Electronic_Store.DTOs.PaginatedResponseDTO;
import com.electronic.store.Electronic_Store.DTOs.ProductDTO;
import com.electronic.store.Electronic_Store.Entities.Category;
import com.electronic.store.Electronic_Store.Entities.Product;
import com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions.UserNotFoundException;
import com.electronic.store.Electronic_Store.Repositories.CategoryRepository;
import com.electronic.store.Electronic_Store.Repositories.ProductRepository;
import com.electronic.store.Electronic_Store.Services.CategoryService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO catDTO) {
        Category category = modelMapper.map(catDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO catDTO, Long catId) {
        //1. Fetch User
        Category category = categoryRepository.findById(catId).orElseThrow(()-> new ResourceNotFoundException("Resource Not Found : Unable to find category with id: "+catId));

        // 2. Manually copy only the allowed updatable fields
        //if (catDTO.getName() != null) user.setName(userDto.getName());
        if(catDTO.getTitle()!=null) category.setTitle(catDTO.getTitle());
        if(catDTO.getDescription()!=null) category.setDescription(catDTO.getDescription());

        // 3. Save and return
        Category savedcategory= categoryRepository.save(category);
        return modelMapper.map(savedcategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(()-> new ResourceNotFoundException("Resource Not Found : Unable to find category with id "+catId));
        categoryRepository.deleteById(catId);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public PaginatedResponseDTO<CategoryDTO> getAllCategory(int pageNum, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Category> page = categoryRepository.findAll(pageable);
        List<Category> categories = page.getContent();
        List<CategoryDTO> catDTO = categories.stream().map((x)-> modelMapper.map(x, CategoryDTO.class)).toList();
        return new PaginatedResponseDTO<>(
                catDTO,
                pageNum,
                pageSize,
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public CategoryDTO getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(()-> new ResourceNotFoundException("Resource Not Found : Unable to find category with id "+catId));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public void uploadCategoryCoverImage(String fileName, Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(()->new ResourceNotFoundException("Resource Not Found : Unable to fetch category with Id "+ catId));
        category.setCoverImage(fileName);
        categoryRepository.save(category);
    }

    @Override
    public ResponseEntity<ProductDTO> createProductWithCategoryAssigned(Long cid, ProductDTO productDTO) {
        Category category = categoryRepository.findById(cid).orElseThrow(()-> new UserNotFoundException("Resource not found: Unable to fetch category with id "+cid));
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        Product savedproduct = productRepository.save(product);
        return new ResponseEntity<>(modelMapper.map(savedproduct, ProductDTO.class), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductDTO> assignCategoryToProduct(Long cid, Long pid) {
        Category category = categoryRepository.findById(cid).orElseThrow(()-> new UserNotFoundException("Resource not found: Unable to fetch category with id "+cid));
        Product product = productRepository.findById(pid).orElseThrow(()-> new UserNotFoundException("Resource not found: Unable to fetch product with id "+pid));
        product.setCategory(category);
        productRepository.save(product);

        return new ResponseEntity<>(modelMapper.map(product, ProductDTO.class), HttpStatus.OK);
    }

    @Override
    public PaginatedResponseDTO<ProductDTO> getProductsByCategory(Long cid, int pageNum, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(cid)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + cid));

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Product> page = productRepository.findByCategory(category, pageable);

        List<ProductDTO> content = page.getContent().stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .toList();

        return new PaginatedResponseDTO<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
