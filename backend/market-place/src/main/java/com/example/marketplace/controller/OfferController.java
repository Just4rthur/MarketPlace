package com.example.marketplace.controller;

import com.example.marketplace.model.Product;
import com.example.marketplace.service.OfferService;
import com.example.marketplace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/offer")
public class OfferController {
    @Autowired
    private OfferService offerService;
    @Autowired
    private ProductService productService;

    @GetMapping("/getOffers")
    public List<Product> getOffers() {

        String username = "";

        //Token authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            username = userDetails.getUsername();
        }
        System.out.println(username);

        List<Product> productsToBeShown = offerService.getOffers(username);
        System.out.println(productsToBeShown.toString());
        return productsToBeShown;
    }

    @PutMapping("/acceptOffer/{id}")
    public ResponseEntity<String> acceptOffer(@PathVariable String id) {
        if (!offerService.acceptOffer(id)) {
            return ResponseEntity.badRequest().body("Offer not accepted");
        }

        return ResponseEntity.ok("Offer accepted");
    }

    @PutMapping("/rejectOffer/{id}")
    public ResponseEntity<String> rejectOffer(@PathVariable String id) {
        if (!offerService.rejectOffer(id)) {
            return ResponseEntity.badRequest().body("Offer not rejected");
        }

        return ResponseEntity.ok("Offer rejected");
    }
}
