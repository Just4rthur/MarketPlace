package com.example.marketplace.dto;

import com.example.marketplace.model.Category;
import com.example.marketplace.model.ProductState;
import com.example.marketplace.model.Color;
import com.example.marketplace.model.User;

public record ProductDTO (String name, Double price, String yearOfProduction, Color color, String condition, Category category, ProductState state, User seller, User buyer){
}

