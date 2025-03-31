package com.vayam.xaconnection.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class OracleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}