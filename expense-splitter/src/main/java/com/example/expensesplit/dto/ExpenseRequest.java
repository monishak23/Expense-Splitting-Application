package com.example.expensesplit.dto;

import com.example.expensesplit.model.Expense;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ExpenseRequest {
    @NotBlank
    private String description;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String currency = "INR";

    @NotNull
    private Long groupId;

    @NotNull
    private Expense.SplitType splitType;

    private List<SplitDetail> splits;

    @Data
    public static class SplitDetail {
        private Long userId;
        private BigDecimal amount; // For UNEQUAL
        private BigDecimal percentage; // For PERCENTAGE
    }
}