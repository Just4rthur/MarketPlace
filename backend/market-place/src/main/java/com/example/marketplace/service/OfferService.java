package com.example.marketplace.service;

import com.example.marketplace.model.Product2;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OfferService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    public List<Product2> getOffers(String username) {
        List<Product2> products = productRepository.findByState(ProductState.PENDING);

        return products;

    }

    public boolean acceptOffer(String id){

        if (id == null) {
            return false;
        }

        Product2 product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        product.setState(ProductState.PURCHASE_CONFIRMED);
        productRepository.save(product);

        return true;
    }

    public boolean rejectOffer(String id){

        if (id == null) {
            return false;
        }

        Product2 product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        product.setState(ProductState.AVAILABLE);
        productRepository.save(product);

        return true;
    }
}
