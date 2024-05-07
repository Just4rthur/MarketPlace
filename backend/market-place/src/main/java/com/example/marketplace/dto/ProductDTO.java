package com.example.marketplace.dto;

import com.example.marketplace.model.ProductState;
import com.example.marketplace.model.Color;
public record ProductDTO (String name, Double price, String yearOfProduction, Color color, String condition, ProductState state){
}

