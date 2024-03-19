package com.example.proj1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordHashingService {

    private final BCryptPasswordEncoder passwordEncoder;

    protected String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    protected boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
