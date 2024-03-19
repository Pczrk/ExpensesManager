package com.example.proj1.model;

import com.example.proj1.repository.entity.Expense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {
    List<ExpenseDto> expensesList;
}
