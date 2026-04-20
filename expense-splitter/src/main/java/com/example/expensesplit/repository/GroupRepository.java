package com.example.expensesplit.repository;

import com.example.expensesplit.model.Group;
import com.example.expensesplit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("SELECT g FROM Group g JOIN g.members m WHERE m.id = :userId")
    List<Group> findByMemberId(@Param("userId") Long userId);

    @Query("SELECT g FROM Group g JOIN g.members m WHERE m.email = :email")
    List<Group> findByMemberEmail(@Param("email") String email);
}