package com.example.proj1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripDto {
    private Long tripId;
    private Long crewId;
    private LocalDate tripDate;
}
