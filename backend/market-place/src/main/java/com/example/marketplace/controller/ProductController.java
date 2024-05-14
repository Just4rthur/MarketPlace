package com.example.marketplace.controller;

import com.example.marketplace.dto.InterestDTO;
import com.example.marketplace.dto.ProductIdDTO;
import com.example.marketplace.model.Product2;
import com.example.marketplace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.marketplace.service.UserInfoService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    // Lägga till en ny produkt
    @PostMapping("/addNewProduct")
    public String addProduct(@RequestBody Product2 product) {
        productService.addProduct(product);
        return "New product added successfully";
    }

    // Hämta en produkt med ID
    @GetMapping("/{id}")
    public Product2 getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    // Uppdatera en befintlig produkt
    @PutMapping("/update/{id}")
    public String updateProduct(@PathVariable String id, @RequestBody Product2 product) {
        productService.updateProduct(id, product);
        return "Product updated successfully";
    }

    // Ta bort en produkt
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }

    // Lista alla produkter
    @GetMapping("/listAll")
    public List<Product2> getAllProducts() {
        return productService.getAllProducts();
    }

    // Lista produkter inom ett specifikt prisintervall
    @GetMapping("/search/byPriceRange")
    public List<Product2> getProductsByPriceRange(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }

    // Lista produkter baserat på namn
    @GetMapping("/search/byName")
    public List<Product2> getProductsByName(@RequestParam String name) {
        return productService.getProductsByName(name);
    }

    // Lista produkter baserat på skick
    @GetMapping("/search/byCondition")
    public List<Product2> getProductsByCondition(@RequestParam String condition) {
        return productService.getProductsByCondition(condition);
    }

    //Ändra product state till pending
    @PutMapping("/submitProductOrder")
    public ResponseEntity<String> submitProductOrder(@RequestBody List<ProductIdDTO> productsToSubmit) {
        if (productService.changeStatesOfProductsToPending(productsToSubmit)) {
            return ResponseEntity.ok("Order submitted");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add resource");
        }
    }

    @PutMapping("/denyProductOffer")
    public ResponseEntity<String> denyProductOffer(@RequestBody ProductIdDTO productIdDTO) {
        if(productService.changeStateOfProductToAvailable(productIdDTO)) {
            return ResponseEntity.ok("Offer canceled");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add resource");
        }
    }

    @PutMapping("/acceptOffer")
    public ResponseEntity<String> acceptOffer(@RequestBody ProductIdDTO productIdDTO) {
        if(productService.changeStateOfProductToAccept(productIdDTO)) {
            return ResponseEntity.ok("Offer accepted");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add resource");
        }
    }


}
