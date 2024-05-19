package com.example.marketplace.service;

import com.example.marketplace.model.Category;
import com.example.marketplace.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseUpdateService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void updateProducts(){
        //Fetch all products
        List<Product> products = mongoTemplate.findAll(Product.class);

        for (Product product : products) {
            if (product.getSellerId() != null || product.getBuyerId() != null || product.getCategory() != null) {
                // Skip this iteration if they are
                continue;
            }
            //Add new fields
            product.setSellerId(null);
            product.setBuyerId(null);
            product.setCategory(Category.ELECTRONIC);

            mongoTemplate.save(product);
        }
    }
}
