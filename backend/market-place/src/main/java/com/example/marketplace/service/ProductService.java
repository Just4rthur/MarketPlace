package com.example.marketplace.service;

import com.example.marketplace.dto.ProductDTO;
import com.example.marketplace.dto.ProductIdDTO;
import com.example.marketplace.model.Product2;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lägger till en ny produkt
    public void addProduct(Product2 product) {
        String oldName = product.getName();
        product.setName(oldName.toLowerCase());
        productRepository.save(product);
    }

    // Hämtar en produkt med på ID
    public Product2 getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Uppdaterar en befintlig produkt
    public void updateProduct(String id, Product2 updatedProduct) {
        Product2 product = getProductById(id);
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setYearOfProduction(updatedProduct.getYearOfProduction());
        product.setColor(updatedProduct.getColor());
        product.setCondition(updatedProduct.getCondition());
        productRepository.save(product);
    }

    // Tar bort en produkt
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    // Hämta alla produkter
    public List<Product2> getAllProducts() {
        return productRepository.findAll();
    }

   // Hämta produkter inom ett specifikt prisintervall
    public List<Product2> getProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // Hämta produkter baserat på namn
    public List<Product2> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    // Hämta produkter baserat på skick
    public List<Product2> getProductsByCondition(String condition) {
        return productRepository.findByCondition(condition);
    }
    public boolean changeStatesOfProductsToPending(List<ProductIdDTO> productIds){
        try {
            for (ProductIdDTO id : productIds) {
                Optional<Product2> productOpt = productRepository.findById(id.id());
                Product2 product = productOpt.get();
                product.setState(ProductState.PENDING);
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
}

