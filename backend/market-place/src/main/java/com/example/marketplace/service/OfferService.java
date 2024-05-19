package com.example.marketplace.service;

import com.example.marketplace.model.Product;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public List<Product> getOffers(String username) {
        List<Product> pendingProducts = productRepository.findByState(ProductState.PENDING);
        User seller = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        for (Product product : pendingProducts) {
            if (product.getState().equals(ProductState.PENDING) && product.getSellerId().equals(seller.getId())){
                pendingProducts.add(product);
            }
        }
        System.out.println("OfferService.getOffers(): " + pendingProducts);
        return pendingProducts;
    }

    public boolean acceptOffer(String id){

        if (id == null) {
            return false;
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        product.setState(ProductState.PURCHASE_CONFIRMED);
        productRepository.save(product);

        return true;
    }

    public boolean rejectOffer(String id){

        if (id == null) {
            return false;
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        product.setState(ProductState.AVAILABLE);
        productRepository.save(product);

        return true;
    }
}
