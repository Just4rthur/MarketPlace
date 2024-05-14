package com.example.marketplace.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document
public class User {

    @Id
    private String id;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private Role roles;
    @Getter
    @Setter
    private ArrayList<String> listOfInterests;
    @Getter
    @Setter
    private ArrayList<String> notificationList;

    public User(String username, String email, String password, Role roles) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        listOfInterests = new ArrayList<>();
        notificationList = new ArrayList<>();
    }

}
