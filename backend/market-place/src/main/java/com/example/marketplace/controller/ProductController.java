package com.example.marketplace.controller;

import com.example.marketplace.dto.ProductDTO;
import com.example.marketplace.dto.ProductIdDTO;
import com.example.marketplace.dto.*;
import com.example.marketplace.model.Product2;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Isolation;
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
    @Autowired
    private UserRepository userRepository;

    // Add a new product to the database
    @PostMapping("/addNewProduct")
    public String addProduct(@RequestBody ProductDTO productdto) {

        //Convert productdto to product
        Product2 product = new Product2(productdto.name(), productdto.price(), productdto.yearOfProduction(), productdto.color(), productdto.condition(), productdto.category(), productdto.sellerId(), productdto.sellerUsername(), null, null, ProductState.AVAILABLE);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            String username = userDetails.getUsername();
            product.setSellerId(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")).getId());
            product.setSellerUsername(username);
        }


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
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        System.out.println(id);
        //token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = "";

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            username = userDetails.getUsername();
        }

        if (productService.deleteProduct(id, username)) {
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

    @GetMapping("/availableProducts")
    public List<Product2> getAvailableProductsForUser() {
        String username = "";
        //token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            username = userDetails.getUsername();
        }

        return productService.getAvailableProductsForUser(userRepository.findByUsername(username).orElseThrow().getId());
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
    public ResponseEntity<List<Product2>> submitProductOrder(@RequestBody ProductIdDTO productsToSubmit) {
        //token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            String username = userDetails.getUsername();

            List<Product2> updatedProducts = productService.submitProducts(productsToSubmit.id(), username);

            return ResponseEntity.ok(updatedProducts);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @GetMapping("/getOffersByUser")
    public List<Product2> getOffersByUser() {
        String username = "";
        //token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            username = userDetails.getUsername();
        }
        return productService.getOffers(username);
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
