package com.example.expensesplit.repository;

import com.example.expensesplit.model.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {
    List<ExpenseSplit> findByExpenseId(Long expenseId);
    List<ExpenseSplit> findByUserId(Long userId);
}