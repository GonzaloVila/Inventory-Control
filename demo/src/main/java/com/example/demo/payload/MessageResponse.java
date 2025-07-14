package com.example.demo.payload;

public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    // Getter y Setter
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
