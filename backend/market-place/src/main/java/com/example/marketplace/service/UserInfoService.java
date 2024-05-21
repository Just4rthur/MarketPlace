package com.example.marketplace.service;

import com.example.marketplace.dto.CategoryDTO;
import com.example.marketplace.dto.InterestDTO;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByUsername(username);

        //Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found --> " + username));

    }

    public String addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User added successfully";
    }

    public boolean subscribeToCategory(User user, CategoryDTO categoryDTO) {

        if (user.getListOfInterests().contains(categoryDTO.category())) {
            System.out.println("User already subscribed to this category");
            return false;
        }

        user.getListOfInterests().add(categoryDTO.category());
        System.out.println("User subscribed to category: " + categoryDTO.category());

        return true;
    }

    public boolean unsubscribeFromCategory(User user, CategoryDTO categoryDTO) {

        if (user.getListOfInterests().contains(categoryDTO.category())) {
            user.getListOfInterests().remove(categoryDTO.category());
            System.out.println("User has unsubscribed from category: " + categoryDTO.category());

            userRepository.save(user);
            return true;
        }

        System.out.println("User is not subscribed to this category");
        return false;
    }

    public boolean deleteInterest(InterestDTO interestDTO) {
        return false;
    }

    public void addNotification(Product product) {

    }
}
