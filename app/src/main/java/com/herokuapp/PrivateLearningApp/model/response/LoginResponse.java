package com.herokuapp.PrivateLearningApp.model.response;

public class LoginResponse {
    private String message, token;
    private int id;

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

    public LoginResponse(String message, String token, int id) {
        this.message = message;
        this.token = token;
        this.id = id;
    }
}
