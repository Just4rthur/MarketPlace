package com.example.marketplace.dto;

import com.example.marketplace.model.Product2;

import java.util.List;

public record SubmitOrderDTO(List<Product2> products) {
}
