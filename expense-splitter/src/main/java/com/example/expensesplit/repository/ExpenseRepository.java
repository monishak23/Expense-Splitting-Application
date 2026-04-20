package com.example.expensesplit.repository;

import com.example.expensesplit.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByGroupIdOrderByCreatedAtDesc(Long groupId);

    @Query("SELECT e FROM Expense e WHERE e.paidBy.id = :userId ORDER BY e.createdAt DESC")
    List<Expense> findByPaidByIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT e FROM Expense e JOIN e.splits s WHERE s.user.id = :userId ORDER BY e.createdAt DESC")
    List<Expense> findByParticipantIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}