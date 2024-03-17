package com.example.proj1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRegisterDto {
    private String username;
    private String mail;
    private String password;
    private String passwordAgain;
}