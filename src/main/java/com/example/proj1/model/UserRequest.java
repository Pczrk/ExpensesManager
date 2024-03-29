package com.example.proj1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private UserLoginDto userLogin;
    private UserRegisterDto userRegister;
}
