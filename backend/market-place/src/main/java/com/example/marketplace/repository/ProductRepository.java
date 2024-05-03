package com.example.marketplace.repository;

import com.example.marketplace.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    @Query("{ 'name' : '?0' }")
    Product findByName(String name);

    @Query("{ 'category' : '?0' }")
    List<Product> findAll(String category);

    public long count();

}
