package com.example.marketplace.service;

import com.example.marketplace.model.Product2;
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
        List<Product2> products = mongoTemplate.findAll(Product2.class);

        for (Product2 product : products) {
            if (product.getSeller() != null || product.getBuyer() != null || product.getCategory() != null) {
                // Skip this iteration if they are
                continue;
            }
            //Add new fields
            product.setSeller(null);
            product.setBuyer(null);
            product.setCategory(null);

            mongoTemplate.save(product);
        }
    }
}
