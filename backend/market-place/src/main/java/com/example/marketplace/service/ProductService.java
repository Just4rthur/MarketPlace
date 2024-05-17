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
    public boolean deleteProduct(ProductNameDTO dto) {
        Optional<Product2> product = productRepository.findByName(dto.ProductName());
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return true;
        }
        return false;
    }

    // Hämta alla produkter
    public List<Product2> getAllProducts() {
        return productRepository.findAll();
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

    public boolean changeStatesOfProductsToPending(List<ProductIdDTO> productIds) {
        try {
            for (ProductIdDTO id : productIds) {
                Optional<Product2> productOpt = productRepository.findById(id.id());
                Product2 product = productOpt.get();
                product.setState(ProductState.PENDING);

                for (ProductIdDTO productIdDTO : productIds) {
                    setBuyer(productIdDTO);
                }

                productRepository.save(product);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean changeStateOfProductToAvailable(ProductIdDTO productIdDTO) {
        try {
            Optional<Product2> productOpt = productRepository.findById(productIdDTO.id());
            Product2 product = productOpt.get();
            product.setState(ProductState.AVAILABLE);
            productRepository.save(product);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean changeStateOfProductToAccept(ProductIdDTO productIdDTO) {
        try {
            Optional<Product2> productOpt = productRepository.findById(productIdDTO.id());
            Product2 product = productOpt.get();
            product.setState(ProductState.PURCHASE_CONFIRMED);
            productRepository.save(product);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setBuyer(ProductIdDTO product) {
        Optional<Product2> productOpt = productRepository.findById(product.id());
        Product2 product2 = productOpt.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            String username = userDetails.getUsername();
            product2.setBuyer(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")));
        }
    }
}
