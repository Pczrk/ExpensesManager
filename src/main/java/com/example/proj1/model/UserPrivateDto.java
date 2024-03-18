package com.example.proj1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserPrivateDto {
    private Long userId;
    private String username;
    private String mail;
}
