package com.example.proj1.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserLoginDto {
    private String mail;
    private String password;
}
