package com.example.marketplace.service;

import com.example.marketplace.dto.*;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.marketplace.repository.UserRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    // Hämtar en produkt med på Product Name
    public Optional<Product> getProductByName(ProductNameDTO dto) {
        return productRepository.findByName(dto.ProductName());
    }

    // Uppdaterar en befintlig produkt
    public boolean updateProduct(SearchProductDTO searchProductDTO) {
        Optional<Product> optionalProduct = productRepository.findByName(searchProductDTO.userName());

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
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
    public boolean deleteProduct(String id, String username) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        String userId = userRepository.findByUsername(username).get().getId();

        if (product.getSellerId().equals(userId)) {
            productRepository.delete(product);
            return true;
        }

        System.out.println("User not authorized to delete product");
        return false;

    }

    // Hämta alla produkter
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public List<Product> getAllProducts() {
        return productRepository.findByState(ProductState.AVAILABLE);
    }

    // Hämta produkter inom ett specifikt prisintervall
    public List<Product> getProductsByPriceRange(PriceRangeDTO priceRangeDTO) {
        return productRepository.findByPriceBetween(priceRangeDTO.minPrice(), priceRangeDTO.maxPrice());
    }

    // Hämta produkter baserat på skick
    public List<Product> getProductsByCondition(ConditionDTO conditionDTO) {
        return productRepository.findByCondition(conditionDTO.condition());
    }

    // Hämta produkter baserat på namn
    public Optional<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    // Hämta produkter baserat på skick
    public List<Product> getProductsByCondition(String condition) {
        return productRepository.findByCondition(condition);
    }

    public List<Product> submitProducts(String[] id, String username) {

        User user = userRepository.findByUsername(username).orElseThrow();
        String buyerId = user.getId();
        String buyerUsername = user.getUsername();
        List<Product> products = new ArrayList<>();

        for (String productId : id) {
            Product product = productRepository.findById(productId).orElseThrow();
            product.setBuyerId(buyerId);
            product.setBuyerUsername(buyerUsername);
            product.setState(ProductState.PENDING);
            products.add(product);
            productRepository.save(product);
        }

        return products;

    }

    public List<Product> getAvailableProductsForUser(String sellerId) {
        List<Product> products = productRepository.findByState(ProductState.AVAILABLE);
        List<Product> availableProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.getSellerId().equals(sellerId)) {
                availableProducts.add(product);
                System.out.println(product.getName());
            }
        }

        return availableProducts;
    }

    public List<Product> getPurchasedProductsForUser(String buyerId){
        List<Product> products = productRepository.findByBuyerId(buyerId);
        List<Product> purchasedProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getState() == ProductState.PURCHASE_CONFIRMED) {
                purchasedProducts.add(product);
            }
        }
        return purchasedProducts;
    }

    public List<Product> getSoldProductsForUser(String username){
        List<Product> products = productRepository.findBySellerId(username);
        List<Product> soldProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getState() == ProductState.PURCHASE_CONFIRMED) {
                soldProducts.add(product);
            }
        }
        return soldProducts;

    }

    public boolean changeStateOfProductToAvailable(ProductIdDTO productIdDTO) {
        return false;
    }

    public boolean changeStateOfProductToAccept(ProductIdDTO productIdDTO) {
        return false;
    }
}
