package com.example.checkscam.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "users")
@Entity
public class UserEntity {
    @Id
    private String id;
}
