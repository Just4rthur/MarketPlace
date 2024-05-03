package com.example.marketplace.model;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product {
    @Id
    private String id;

    @Getter
    private String name;
    private String description;
    private String category;
    private double price;
    @Getter
    private int quantity;

    public Product(String id, String name, String description, String category, double price, int quantity) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

}
