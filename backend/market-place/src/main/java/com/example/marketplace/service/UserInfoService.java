package com.example.marketplace.service;

import com.example.marketplace.dto.InterestDTO;
import com.example.marketplace.dto.ProductDTO;
import com.example.marketplace.dto.userCredentialDTO;
import com.example.marketplace.model.Product2;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public boolean registerInterestInProduct(InterestDTO interestDTO) {
        System.out.println("Checkpoint 2");
        try {

            Optional<User> userToUpdate = userRepository.findByUsername(interestDTO.username());
            if(userToUpdate.isPresent()) {
                User user = userToUpdate.get();
                ArrayList<String> listOfInterests = user.getListOfInterests();
                listOfInterests.add(interestDTO.productType());
                user.setListOfInterests(listOfInterests);
                userRepository.save(user);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
