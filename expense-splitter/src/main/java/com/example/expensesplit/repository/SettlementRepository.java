package com.example.expensesplit.repository;

import com.example.expensesplit.model.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    List<Settlement> findByGroupIdOrderBySettledAtDesc(Long groupId);

    @Query("SELECT s FROM Settlement s WHERE (s.fromUser.id = :userId OR s.toUser.id = :userId) ORDER BY s.settledAt DESC")
    List<Settlement> findByUserIdOrderBySettledAtDesc(@Param("userId") Long userId);
}