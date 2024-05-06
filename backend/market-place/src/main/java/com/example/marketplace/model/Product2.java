package com.example.marketplace.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product2 {
    @Id
    private String id;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private double price;
    @Getter
    @Setter
    private String yearOfProduction;
    @Getter
    @Setter
    private Color color;
    @Getter
    @Setter
    private String condition;


    public Product2(String id, String name, double price, String yearOfProduction, Color color, String condition) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
        this.yearOfProduction = yearOfProduction;
        this.color = color;
        this.condition = condition;
    }


}
