package com.example.marketplace.controller;

import com.example.marketplace.dto.ProductDTO;
import com.example.marketplace.dto.ProductIdDTO;
import com.example.marketplace.dto.*;
import com.example.marketplace.model.Product2;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.marketplace.service.UserInfoService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserInfoService userInfoService;

    // Add a new product to the database
    @PostMapping("/addNewProduct")
    public String addProduct(@RequestBody ProductDTO productdto) {

        //Convert productdto to product
        Product2 product = new Product2(productdto.name(), productdto.price(), productdto.yearOfProduction(), productdto.color(), productdto.condition(), productdto.category(), productdto.seller(), null, ProductState.AVAILABLE);

        //Add product to the database
        productService.addProduct(product);

        //Notify users
        userInfoService.addNotification(product);
        return new ResponseEntity<String>("Product added successfully", HttpStatus.OK).getBody();
    }

    // Hämta en produkt med Namn
    @GetMapping("/getProduct")
    public ResponseEntity<Product2> getProductByName(@RequestBody ProductNameDTO dto) {
        return productService.getProductByName(dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Uppdatera en befintlig produkt
    @PutMapping("/updateProduct")
    public ResponseEntity<String> updateProduct(@RequestBody SearchProductDTO searchProductDTO) {
        try {
            productService.updateProduct(searchProductDTO);
            return ResponseEntity.ok("Product updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    // Ta bort en produkt
    @DeleteMapping("/deleteProduct")
    public ResponseEntity<String> deleteProduct(@RequestBody ProductNameDTO dto) {
        if (productService.deleteProduct(dto)) {
            return ResponseEntity.ok("Product deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Lista alla produkter
    @GetMapping("/listAll")
    public List<Product2> getAllProducts() {
        return productService.getAllProducts();
    }

    // Lista produkter inom ett specifikt prisintervall
    @GetMapping("/search/byPriceRange")
    public ResponseEntity<List<Product2>> getProductsByPriceRange(@RequestBody PriceRangeDTO priceRangeDTO) {
        List<Product2> products = productService.getProductsByPriceRange(priceRangeDTO);
        if (!products.isEmpty()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Lista produkter baserat på skick
    @GetMapping("/search/byCondition")
    public ResponseEntity<List<Product2>> getProductsByCondition(@RequestBody ConditionDTO conditionDTO) {
        List<Product2> products = productService.getProductsByCondition(conditionDTO);
        if (!products.isEmpty()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.notFound().build();
        }
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
