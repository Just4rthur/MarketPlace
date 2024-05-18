package com.example.marketplace.repository;

import com.example.marketplace.model.Product;
import com.example.marketplace.model.Product2;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product2, String> {
    Optional<Product2> findByName(String name);

    List<Product2> findByCondition(String condition);

    public long count();
    List<Product2> findByPriceBetween(double minPrice, double maxPrice);

    List<Product2> findByState(ProductState state);
    List<Product2> findBySellerId(String sellerId);
    List<Product2> findByBuyerId(String buyerId);
    List<Product2> findBySellerUsername(String sellerUsername);
    List<Product2> findByBuyerUsername(String buyerUsername);


}
