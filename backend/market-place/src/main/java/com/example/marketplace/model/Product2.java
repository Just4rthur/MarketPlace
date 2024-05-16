package com.example.marketplace.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product2 {
    @JsonProperty("id")
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

    @Getter
    @Setter
    private ProductState state;

    @Getter
    @Setter
    private User seller;

    @Getter
    @Setter
    private User buyer;



    public Product2(String name, double price, String yearOfProduction, Color color, String condition, User seller, User buyer, ProductState state) {
        super();
        this.name = name;
        this.price = price;
        this.yearOfProduction = yearOfProduction;
        this.color = color;
        this.condition = condition;
        this.seller = seller;
        this.state = ProductState.AVAILABLE;
    }


}
