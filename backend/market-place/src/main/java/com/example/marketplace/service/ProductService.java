package com.example.marketplace.service;

import com.example.marketplace.dto.*;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.Product2;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.marketplace.repository.UserRepository;

import java.util.NoSuchElementException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    // Lägger till en ny produkt
    public void addProduct(Product2 product) {
        String oldName = product.getName();
        product.setName(oldName.toLowerCase());
        productRepository.save(product);
    }

    // Hämtar en produkt med på Product Name
    public Optional<Product2> getProductByName(ProductNameDTO dto) {
        return productRepository.findByName(dto.ProductName());
    }

    // Uppdaterar en befintlig produkt
    public boolean updateProduct(SearchProductDTO searchProductDTO) {
        Optional<Product2> optionalProduct = productRepository.findByName(searchProductDTO.userName());

        if (optionalProduct.isPresent()) {
            Product2 product = optionalProduct.get();
            product.setName(searchProductDTO.productName());
            product.setPrice(searchProductDTO.price());
            product.setYearOfProduction(searchProductDTO.yearOfProduction());
            product.setColor(searchProductDTO.color());
            product.setCondition(searchProductDTO.condition());
            productRepository.save(product);
            return true;
        } else {
            return false; // product not found
        }
    }

    // Tar bort en produkt
    public boolean deleteProduct(ProductNameDTO dto, String username) {
        Product2 product = productRepository.findByName(dto.ProductName()).orElseThrow(() -> new NoSuchElementException("Product not found"));

        if (product.getSeller() == userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"))) {
            productRepository.delete(product);
            return true;
        }

        System.out.println("User not authorized to delete product");
        return false;
    }

    // Hämta alla produkter
    public List<Product2> getAllProducts() {
        return productRepository.findByState(ProductState.AVAILABLE);
    }

    // Hämta produkter inom ett specifikt prisintervall
    public List<Product2> getProductsByPriceRange(PriceRangeDTO priceRangeDTO) {
        return productRepository.findByPriceBetween(priceRangeDTO.minPrice(), priceRangeDTO.maxPrice());
    }

    // Hämta produkter baserat på skick
    public List<Product2> getProductsByCondition(ConditionDTO conditionDTO) {
        return productRepository.findByCondition(conditionDTO.condition());
    }

    // Hämta produkter baserat på namn
    public Optional<Product2> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    // Hämta produkter baserat på skick
    public List<Product2> getProductsByCondition(String condition) {
        return productRepository.findByCondition(condition);
    }

    public boolean changeStatesOfProductsToPending(ProductIdDTO productIds, String username) {
        try {
            for (String id : productIds.id()) {
                Optional<Product2> productOpt = productRepository.findById(id);
                Product2 product = productOpt.get();
                product.setState(ProductState.PENDING);

                product.setBuyer(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")));

                productRepository.save(product);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Product2> getAvailableProductsForUser(String username) {
        List<Product2> products = productRepository.findBySeller(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")));
        List<Product2> availableProducts = new ArrayList<>();
        for (Product2 product : products) {
            if (product.getState() == ProductState.AVAILABLE) {
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }

    public boolean changeStateOfProductToAvailable(ProductIdDTO productIdDTO) {
        return false;
    }

    public boolean changeStateOfProductToAccept(ProductIdDTO productIdDTO) {
        return false;
    }
}
