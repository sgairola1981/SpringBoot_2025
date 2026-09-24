package com.gairola.authserver.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "APP_USERS")
public class AppUser {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_users_seq"
    )
    @SequenceGenerator(
            name = "app_users_seq",
            sequenceName = "APP_USERS_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(
            name = "USERNAME",
            nullable = false,
            unique = true,
            length = 100
    )
    private String username;

    @Column(
            name = "PASSWORD",
            nullable = false,
            length = 255
    )
    private String password;

    @Column(
            name = "ROLE",
            nullable = false,
            length = 50
    )
    private String role = "USER";

    @Column(
            name = "ENABLED",
            nullable = false
    )
    private Boolean enabled = true;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @PrePersist
    public void prePersist() {

        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }

        if (role == null || role.isBlank()) {
            role = "USER";
        }

        if (enabled == null) {
            enabled = true;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}