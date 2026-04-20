package com.example.expensesplit.controller;

import com.example.expensesplit.algorithm.DebtSimplificationService;
import com.example.expensesplit.dto.BalanceDTO;
import com.example.expensesplit.dto.ExpenseRequest;
import com.example.expensesplit.model.Expense;
import com.example.expensesplit.repository.ExpenseRepository;
import com.example.expensesplit.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;

    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody ExpenseRequest request,
                                                 Authentication authentication) {
        Expense expense = expenseService.createExpense(request, authentication.getName());
        return ResponseEntity.ok(expense);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Expense>> getGroupExpenses(@PathVariable Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroupIdOrderByCreatedAtDesc(groupId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpense(@PathVariable Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow();
        return ResponseEntity.ok(expense);
    }

    @GetMapping("/group/{groupId}/balances")
    public ResponseEntity<List<BalanceDTO>> getGroupBalances(@PathVariable Long groupId) {
        List<BalanceDTO> balances = expenseService.getGroupBalances(groupId);
        return ResponseEntity.ok(balances);
    }

    @GetMapping("/group/{groupId}/simplified")
    public ResponseEntity<List<DebtSimplificationService.SimplifiedTransaction>> getSimplifiedTransactions(
            @PathVariable Long groupId) {
        List<DebtSimplificationService.SimplifiedTransaction> transactions =
                expenseService.getSimplifiedTransactions(groupId);
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}