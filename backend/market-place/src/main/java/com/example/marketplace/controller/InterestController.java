package com.example.marketplace.controller;

import com.example.marketplace.dto.InterestDTO;
import com.example.marketplace.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.marketplace.service.UserInfoService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interest")
public class InterestController {
    @Autowired
    private UserInfoService userInfoService;

    @PutMapping("/registerInterest")
    public ResponseEntity<String> registerInterestInProduct(@RequestBody InterestDTO interestDTO) {
        System.out.println("Checkpoint 1");

        if(userInfoService.registerInterestInProduct(interestDTO)) {
            return ResponseEntity.ok("Interest added");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register interest");
    }

}
