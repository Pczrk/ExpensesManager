package com.example.proj1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewDto {
    private Long crewId;
    private String accessKey;
    private String name;
    private UserDto leader;
    private List<MemberDto> members;
}