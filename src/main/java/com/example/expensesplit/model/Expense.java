package com.example.expensesplit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency = "INR";

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private User paidBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private Group group;

    @Enumerated(EnumType.STRING)
    private SplitType splitType = SplitType.EQUAL;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ExpenseSplit> splits = new HashSet<>();

    public enum SplitType {
        EQUAL, UNEQUAL, PERCENTAGE
    }
}