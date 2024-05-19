package com.example.marketplace.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Document(collection = "products")
public class Product {
    @JsonProperty("id")
    @Setter
    @Getter
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
    private Category category;

    @Getter
    @Setter
    private ProductState state;

    @Getter
    @Setter
    private String sellerId;

    @Getter
    @Setter
    private String sellerUsername;

    @Getter
    @Setter
    private String buyerId;

    @Getter
    @Setter
    private String buyerUsername;


    public Product(String name, double price, String yearOfProduction, Color color, String condition, Category category, String sellerId, String sellerUsername, String buyerId, String buyerUsername, ProductState state) {
        super();
        this.name = name;
        this.price = price;
        this.yearOfProduction = yearOfProduction;
        this.color = color;
        this.condition = condition;
        this.category = category;
        this.sellerId = sellerId;
        this.sellerUsername = sellerUsername;
        this.state = ProductState.AVAILABLE;
    }


}
