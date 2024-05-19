package com.example.marketplace.controller;

import com.example.marketplace.model.Product;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductController productController;
    @Autowired
    private ProductService productService;

    @GetMapping("/showPurchaseHistory")
    public List<Product> showPurchases() {

        String username = "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            username = userDetails.getUsername();
        }

        return productService.getPurchasedProductsForUser(userRepository.findByUsername(username).orElseThrow().getId());

    }

    @GetMapping("/showSaleHistory")
    public List<Product> showSales() {

        String username = "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            username = userDetails.getUsername();
        }

        return productService.getSoldProductsForUser(username);

    }

}
