package com.example.marketplace.controller;

import com.example.marketplace.dto.InterestDTO;
import com.example.marketplace.dto.ProductDTO;
import com.example.marketplace.dto.userCredentialDTO;
import com.example.marketplace.model.Role;
import com.example.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.example.marketplace.model.AuthRequest;
import com.example.marketplace.model.User;
import com.example.marketplace.service.JwtService;
import com.example.marketplace.service.UserInfoService;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/auth")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JwtService jwtService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome, this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<?> addNewUser(@RequestBody userCredentialDTO userDTO) {

        Optional<User> userOptionalName = userRepository.findByUsername(userDTO.username());
        Optional<User> userOptionalEmail = userRepository.findByEmail(userDTO.email());
        if (userOptionalName.isEmpty() && userOptionalEmail.isEmpty()){
            userRepository.save(new User(userDTO.username(), userDTO.email(), userDTO.password(), Role.ROLE_USER));
            return ResponseEntity.ok().build();
        }
       return ResponseEntity.badRequest().body("this user name or email already exist");
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile(){
        return "Welcome to your profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile(){
        return "Welcome to your Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticationAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()){
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid User Request");
        }
    }



    public void notifyUserOfProduct(User user) {
        // a user can be interested to receive
        //messages whenever a MacBook Pro is added to be sold in the marketplace. If the
        //user is not connected to the system when the product is added, he/she must get
        //a message on her/his inbox once he/she is connected

    }
}
