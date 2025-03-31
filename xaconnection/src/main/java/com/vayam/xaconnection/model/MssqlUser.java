package com.vayam.xaconnection.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users_mssql")
@Data
public class MssqlUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

