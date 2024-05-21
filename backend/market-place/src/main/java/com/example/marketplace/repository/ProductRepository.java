package com.example.marketplace.repository;

import com.example.marketplace.model.Product;
import com.example.marketplace.model.ProductState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByName(String name);

    List<Product> findByCondition(String condition);

    public long count();
    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    List<Product> findByState(ProductState state);
    List<Product> findBySellerId(String sellerId);
    List<Product> findByBuyerId(String buyerId);
    List<Product> findAllById(String[] ids);

    @Query("{ 'sellerId': ?0, 'state': ?1 }")
    List<Product> findBySellerIdAndState(String sellerId, ProductState state);

    List<Product> findAllByState(ProductState state);
    List<Product> findBySellerUsername(String sellerUsername);
    List<Product> findByBuyerUsername(String buyerUsername);


}
