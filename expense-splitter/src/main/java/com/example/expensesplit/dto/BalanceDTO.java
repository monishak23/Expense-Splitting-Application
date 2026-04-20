package com.example.expensesplit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO {
    private Long userId;
    private String userName;
    private BigDecimal amount; // Positive = owes you, Negative = you owe
}