package com.example.proj1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDto {
    private Long expenseId;
    private Long tripId;
    private BigDecimal value;
    private String name;
    private String description;
    private Long payedBy;
}
