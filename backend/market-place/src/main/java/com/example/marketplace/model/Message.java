package com.example.marketplace.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Message {
    @Setter
    private String content;

    @Setter
    private Product2 product;

    @Setter
    LocalDateTime timestamp;

    @Setter
    private boolean read;

    public Message(String content, Product2 product){
        this.content = content;
        this.product = product;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    public void markAsRead() {
        this.read = true;
    }

    public void markAsUnread() {
        this.read = false;
    }

}
