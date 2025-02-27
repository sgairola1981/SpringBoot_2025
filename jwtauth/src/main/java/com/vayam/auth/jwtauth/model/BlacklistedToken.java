package com.vayam.auth.jwtauth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "user_seq_black", sequenceName = "USER_SEQ_BLACK", allocationSize = 1)
public class BlacklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_black")
    private Long id;
    private String token;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
