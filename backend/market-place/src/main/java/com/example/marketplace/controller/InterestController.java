package com.example.marketplace.controller;

import com.example.marketplace.dto.InterestDTO;
import com.example.marketplace.dto.UsernameDTO;
import com.example.marketplace.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.marketplace.service.UserInfoService;

import java.util.ArrayList;

@RestController
@RequestMapping("/interest")
public class InterestController {
    @Autowired
    private UserInfoService userInfoService;

    @PutMapping("/registerInterest")
    public ResponseEntity<String> registerInterestInProduct(@RequestBody InterestDTO interestDTO) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register interest");
    }

    @DeleteMapping("/deleteInterest")
    public ResponseEntity<String> deleteInterest(@RequestBody InterestDTO interestDTO) {
        if(userInfoService.deleteInterest(interestDTO)) {
            return ResponseEntity.ok("Interest deleted");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete interest");
    }

    @GetMapping("/getNotifications")
    public ArrayList<String> getNotifications(@RequestBody UsernameDTO usernameDTO) {
        return null;
    }
}
