package com.example.marketplace.service;

import com.example.marketplace.dto.ProductDTO;
import com.example.marketplace.model.Category;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    public void notifyUsers(ProductDTO product, String seller) {
        List<User> users = userRepository.findAll();
    }
}
