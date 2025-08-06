package com.electronic.store.Electronic_Store.Services.Implementations;

import com.electronic.store.Electronic_Store.DTOs.ProductDTO;
import com.electronic.store.Electronic_Store.DTOs.PaginatedResponseDTO;
import com.electronic.store.Electronic_Store.Entities.Category;
import com.electronic.store.Electronic_Store.Entities.Product;
import com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.electronic.store.Electronic_Store.Repositories.CategoryRepository;
import com.electronic.store.Electronic_Store.Repositories.ProductRepository;
import com.electronic.store.Electronic_Store.Services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
         Product product = modelMapper.map(productDTO, Product.class);
         Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long pid) {
        Product product = productRepository.findById(pid).orElseThrow(()-> new ResourceNotFoundException("Resource Not Found : Unable to find Product with id: "+pid));

        // 2. Manually copy only the allowed updatable fields
        if(productDTO.getProductName()!=null) product.setProductName(productDTO.getProductName());
        if(productDTO.getDescription()!=null) product.setDescription(productDTO.getDescription());
        if(productDTO.getShortDescription()!=null) product.setShortDescription(productDTO.getShortDescription());
        if(productDTO.getPrice()!=null) product.setPrice(productDTO.getPrice());
        if(productDTO.getQuantity()!=null) product.setQuantity(productDTO.getQuantity());
        if(productDTO.getInStock()!=null) product.setInStock(productDTO.getInStock());
        if(productDTO.getAvgRatings()!=null) product.setAvgRatings(product.getAvgRatings());
        if(productDTO.getNumberOfRatings()!=null) product.setNumberOfRatings(product.getNumberOfRatings());

        // 3. Save and return
        Product saveProduct= productRepository.save(product);
        return modelMapper.map(saveProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long pid) {
        Product product = productRepository.findById(pid).orElseThrow(()-> new ResourceNotFoundException("Resource Not Found : Unable to find product with id "+pid));
        productRepository.deleteById(pid);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO getProductById(Long pid) {
        Product product = productRepository.findById(pid).orElseThrow(()-> new ResourceNotFoundException("Resource Not Found : Unable to find product with id "+pid));
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public PaginatedResponseDTO<ProductDTO> getAllProducts(int pageNum, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Product> page = productRepository.findAll(pageable);
        List<Product> products = page.getContent();
        List<ProductDTO> productListDTO = products.stream().map((p)-> modelMapper.map(p, ProductDTO.class)).toList();
        return new PaginatedResponseDTO<>(
                productListDTO,
                pageNum,
                pageSize,
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public PaginatedResponseDTO<ProductDTO> getProductByInStockStatus(boolean inStock, int pageNum, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Product> page = productRepository.findAllByInStock(inStock, pageable);
        List<Product> products = page.getContent();
        List<ProductDTO> productListDTO = products.stream().map((p)-> modelMapper.map(p, ProductDTO.class)).toList();
        return new PaginatedResponseDTO<>(
                productListDTO,
                pageNum,
                pageSize,
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public PaginatedResponseDTO<ProductDTO> getProductInPriceRange(Double minPrice, Double maxPrice, int pageNum, int pageSize, String sortBy, String sortDir) {
        // Set default values if null
        if (minPrice == null) minPrice = 0.0;
        if (maxPrice == null) maxPrice = Double.MAX_VALUE;

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Product> page = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        List<Product> products = page.getContent();

        List<ProductDTO> productListDTO = products.stream().map((p)-> modelMapper.map(p, ProductDTO.class)).toList();
        return new PaginatedResponseDTO<>(
                productListDTO,
                pageNum,
                pageSize,
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public PaginatedResponseDTO<ProductDTO> SearchProductByKeyword(String keyword, int pageNum, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Product> page = productRepository.findByProductNameContainingIgnoreCaseOrShortDescriptionContainingIgnoreCase(keyword,keyword, pageable);
        List<Product> products = page.getContent();

        List<ProductDTO> productListDTO = products.stream().map((p)-> modelMapper.map(p, ProductDTO.class)).toList();
        return new PaginatedResponseDTO<>(
                productListDTO,
                pageNum,
                pageSize,
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public void uploadProductImage(String filename, Long pid) {
        Product product = productRepository.findById(pid).orElseThrow(()->new ResourceNotFoundException("Resource Not Found : Unable to fetch product with Id "+ pid));
        product.setProductImage(filename);
        productRepository.save(product);
    }
}
