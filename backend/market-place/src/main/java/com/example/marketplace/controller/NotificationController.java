package com.example.marketplace.controller;

import com.example.marketplace.dto.ProductDTO;
import com.example.marketplace.model.Message;
import com.example.marketplace.service.NotificationService;
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
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ProductService productService;

    @PostMapping("/notifyUsers")
    public ResponseEntity<String> notifyUsers( @RequestBody ProductDTO productDTO){

        String username = "";

        //Check token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        username = "";

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            username = userDetails.getUsername();
        }

        notificationService.notifyUsers(productDTO, username);

        return ResponseEntity.ok("Users notified");
    }

    @GetMapping("/getNotifications")
    public List<Message> getNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = "";

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            username = userDetails.getUsername();
        }

        return notificationService.getNotifications(username);
    }

}
