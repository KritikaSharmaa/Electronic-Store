package com.electronic.store.Electronic_Store.Utils;

import com.electronic.store.Electronic_Store.Entities.Category;
import com.electronic.store.Electronic_Store.Entities.Product;
import com.electronic.store.Electronic_Store.Entities.User;
import com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.electronic.store.Electronic_Store.Repositories.CategoryRepository;
import com.electronic.store.Electronic_Store.Repositories.ProductRepository;
import com.electronic.store.Electronic_Store.Repositories.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageHelper {

    @Autowired
    userRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    public String getImageName(Long id, String entity){
        String imageName=null;
        // 1. Fetch the user & Get image name and path
        if(entity =="users"){
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID:.. " + id));
            imageName = user.getImage();
        }else if(entity == "categories"){
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID:.. " + id));
            imageName = category.getCoverImage();
        }else if(entity == "products"){
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID:.. " + id));
            imageName = product.getProductImage();
        }
        return imageName;
    }
}
