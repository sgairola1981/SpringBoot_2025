package com.vayam.xaconnection.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users_test")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    // Getters and Setters
}
