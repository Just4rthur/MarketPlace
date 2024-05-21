package com.example.marketplace.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Message {
    @Setter
    private String content;


    @Setter
    String timestamp;

    @Setter
    private boolean read;

    public Message(String content){
        this.content = content;
        this.timestamp = LocalDateTime.now().toString();
        this.read = false;
    }

    public void markAsRead() {
        this.read = true;
    }

    public void markAsUnread() {
        this.read = false;
    }

}
