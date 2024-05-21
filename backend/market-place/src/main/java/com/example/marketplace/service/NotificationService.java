package com.example.marketplace.service;

import com.example.marketplace.dto.ProductDTO;
import com.example.marketplace.model.Category;
import com.example.marketplace.model.Message;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    public void notifyUsers(ProductDTO productDTO, String seller) {
        List<User> usersSubscribedToCategory = userRepository.findByInterest(productDTO.category()).orElseThrow(() -> new RuntimeException("No users found"));
        System.out.println(usersSubscribedToCategory.toString());
        User sellerUser = userRepository.findByUsername(seller).orElseThrow(() -> new RuntimeException("Seller not found"));

        //Seller does not want to be notified about his own product
        usersSubscribedToCategory.remove(sellerUser);

        for (User user : usersSubscribedToCategory) {
            //Send notification
            Message message = new Message("A new product of your interest [" + productDTO.category() + "] has been added --> " + productDTO.name());
            user.getNotificationList().add(message);

            userRepository.save(user);
        }
    }

    public List<Message> getNotifications(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getNotificationList();
    }
}
