package com.example.marketplace.dto;

import com.example.marketplace.model.ProductState;
import com.example.marketplace.model.Color;
import com.example.marketplace.model.User;

public record SearchProductDTO (String userName, String productName, double price, String yearOfProduction, Color color, String condition){
}
