package com.example.marketplace.controller;

import java.util.Optional;

import com.example.marketplace.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.marketplace.dto.userCredentialDTO;
import com.example.marketplace.model.Role;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.JwtService;
import com.example.marketplace.service.UserInfoService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/auth")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        if (userOptionalName.isEmpty() && userOptionalEmail.isEmpty()) {
            userInfoService.addUser(new User(userDTO.username(), userDTO.email(), userDTO.password(), Role.ROLE_USER));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("this user name or email already exist");
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to your profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to your Admin Profile";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody userCredentialDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.email()).orElseThrow(() -> new UsernameNotFoundException("User not found --> " + userDTO.email()));
        if (passwordEncoder.matches(userDTO.password(), user.getPassword())) {
            String token = jwtService.generateToken(user.getUsername());
            System.out.println("Token: \n" + token);

            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(403).body("Invalid username or password");
    }

    public void generateToken(@RequestBody userCredentialDTO userCredentialDTO) {
        if (userCredentialDTO.username() == null) {
            throw new UsernameNotFoundException("Invalid User Request");
        }

        String userToken = jwtService.generateToken(userCredentialDTO.username());

        //Logs the token generated
        System.out.println(userCredentialDTO.username() + " --> token generated");
        System.out.println("User token: \n" + userToken);
    }

    @PutMapping("/subscribeToCategory")
    public ResponseEntity<String> subscribeToCategory(@RequestBody CategoryDTO categoryDTO){
        //Check token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = "";

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            username = userDetails.getUsername();
        }

        //Get user from the database
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userInfoService.subscribeToCategory(user, categoryDTO)){

            userRepository.save(user);
            return new ResponseEntity<String>("User subscribed to category successfully", HttpStatus.OK);

        }

        return new ResponseEntity<>("User already subscribed to this category", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/unsubscribeFromCategory")
    public ResponseEntity<String> unsubscribeFromCategory(@RequestBody CategoryDTO categoryDTO){
        //Check token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = "";

        //Check if the principal is a UserDetails object
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            //Get the username from the UserDetails object
            username = userDetails.getUsername();
        }

        //Get user from the database
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userInfoService.unsubscribeFromCategory(user, categoryDTO)){

            userRepository.save(user);
            return new ResponseEntity<String>("User unsubscribed from category successfully", HttpStatus.OK);

        }

        return new ResponseEntity<>("User is not subscribed to this category", HttpStatus.BAD_REQUEST);

    }

    public void notifyUserOfProduct(User user) {
        // a user can be interested to receive
        //messages whenever a MacBook Pro is added to be sold in the marketplace. If the
        //user is not connected to the system when the product is added, he/she must get
        //a message on her/his inbox once he/she is connected


    }
}
