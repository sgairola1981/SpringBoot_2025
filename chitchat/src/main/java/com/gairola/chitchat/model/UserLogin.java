package com.gairola.chitchat.model;

public class UserLogin {
    private String username;

    public UserLogin() {}

    public UserLogin(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}