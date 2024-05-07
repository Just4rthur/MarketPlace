package com.example.marketplace.repository;

import com.example.marketplace.model.Product2;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product2, String> {

    List<Product2> findByName(String name);

    List<Product2> findByCondition(String condition);

    public long count();
    List<Product2> findByPriceBetween(double minPrice, double maxPrice);

}
