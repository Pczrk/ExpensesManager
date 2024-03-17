package com.example.proj1.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @Column(name = "mail", nullable = false, length = 320)
    private String mail;

    @Column(name = "hashed_password", nullable = false, length = 300)
    private String hashedPassword;
}