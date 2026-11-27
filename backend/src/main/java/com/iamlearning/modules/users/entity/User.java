package com.iamlearning.modules.users.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 254)
    private String email;

    protected User() {
    }

    private User(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static User create(String name, String email) {
        return new User(UUID.randomUUID(), name, email);
    }

    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
