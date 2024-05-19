package com.example.marketplace.service;

import com.example.marketplace.dto.*;
import com.example.marketplace.model.Product2;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.marketplace.repository.UserRepository;
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
    public boolean deleteProduct(String id, String username) {
        Product2 product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        String userId = userRepository.findByUsername(username).get().getId();

        if (product.getSellerId().equals(userId)) {
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

    @Transactional
    public List<Product2> submitProducts(List<String> ids, String username) {
        List<Product2> productsToUpdate = productRepository.findAllById(ids);

        productsToUpdate.forEach(product -> {
            product.setState(ProductState.PENDING);
            product.setBuyerId(userRepository.findByUsername(username).get().getId());
            product.setBuyerUsername(username);
        });

        return productRepository.saveAll(productsToUpdate);
    }

    public List<Product2> getAvailableProductsForUser(String sellerId) {
        List<Product2> products = productRepository.findByState(ProductState.AVAILABLE);
        List<Product2> availableProducts = new ArrayList<>();

        for (Product2 product : products) {
            if (product.getSellerId().equals(sellerId)) {
                availableProducts.add(product);
                System.out.println(product.getName());
            }
        }

        return availableProducts;
    }

    public List<Product2> getPurchasedProductsForUser(String buyerId){
        List<Product2> products = productRepository.findByBuyerId(buyerId);
        List<Product2> purchasedProducts = new ArrayList<>();
        for (Product2 product : products) {
            if (product.getState() == ProductState.PURCHASE_CONFIRMED) {
                purchasedProducts.add(product);
            }
        }
        return purchasedProducts;
    }

    public List<Product2> getSoldProductsForUser(String username){
        List<Product2> products = productRepository.findBySellerId(username);
        List<Product2> soldProducts = new ArrayList<>();
        for (Product2 product : products) {
            if (product.getState() == ProductState.PURCHASE_CONFIRMED) {
                soldProducts.add(product);
            }
        }
        return soldProducts;

    }

    public List<Product2> getOffers(String username){
        //TO BE IMPLEMENTED
        List<Product2> offers = productRepository.findAll();
        return offers;
    }
    public boolean changeStateOfProductToAvailable(ProductIdDTO productIdDTO) {
        return false;
    }

    public boolean changeStateOfProductToAccept(ProductIdDTO productIdDTO) {
        return false;
    }
}
