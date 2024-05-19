package com.example.marketplace.controller;

import com.example.marketplace.service.NotificationService;
import com.example.marketplace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ProductService productService;

    @PostMapping("/notifyUsers")
    public ResponseEntity<String> notifyUsers() {
        notificationService.notifyUsers(null);

        return ResponseEntity.ok("Users notified");
    }

}
