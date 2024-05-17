package com.example.marketplace.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class Message {
    @Setter
    @Getter
    private String content;

    @Setter
    @Getter
    LocalDateTime timestamp;

    @Setter
    @Getter
    private boolean read;

    public Message(String content){
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }
}
