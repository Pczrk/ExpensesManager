package com.example.proj1.repository.entity.id;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class MemberId implements Serializable {
    private Long userId;
    private Long crewId;
}
