package com.example.expensesplit.service;

import com.example.expensesplit.algorithm.DebtSimplificationService;
import com.example.expensesplit.dto.BalanceDTO;
import com.example.expensesplit.dto.ExpenseRequest;
import com.example.expensesplit.model.*;
import com.example.expensesplit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final DebtSimplificationService debtSimplificationService;

    @Transactional
    public Expense createExpense(ExpenseRequest request, String userEmail) {
        User paidBy = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCurrency(request.getCurrency());
        expense.setPaidBy(paidBy);
        expense.setGroup(group);
        expense.setSplitType(request.getSplitType());

        expense = expenseRepository.save(expense);

        // Create splits based on split type
        Set<ExpenseSplit> splits = createSplits(expense, request, group);
        expense.setSplits(splits);

        return expenseRepository.save(expense);
    }

    private Set<ExpenseSplit> createSplits(Expense expense, ExpenseRequest request, Group group) {
        Set<ExpenseSplit> splits = new HashSet<>();

        switch (request.getSplitType()) {
            case EQUAL:
                BigDecimal splitAmount = expense.getAmount()
                        .divide(BigDecimal.valueOf(group.getMembers().size()), 2, RoundingMode.HALF_UP);

                for (User member : group.getMembers()) {
                    ExpenseSplit split = new ExpenseSplit();
                    split.setExpense(expense);
                    split.setUser(member);
                    split.setAmount(splitAmount);
                    split.setIsPaid(member.getId().equals(expense.getPaidBy().getId()));
                    splits.add(split);
                }
                break;

            case UNEQUAL:
                for (ExpenseRequest.SplitDetail detail : request.getSplits()) {
                    User user = userRepository.findById(detail.getUserId())
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    ExpenseSplit split = new ExpenseSplit();
                    split.setExpense(expense);
                    split.setUser(user);
                    split.setAmount(detail.getAmount());
                    split.setIsPaid(user.getId().equals(expense.getPaidBy().getId()));
                    splits.add(split);
                }
                break;

            case PERCENTAGE:
                for (ExpenseRequest.SplitDetail detail : request.getSplits()) {
                    User user = userRepository.findById(detail.getUserId())
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    BigDecimal amount = expense.getAmount()
                            .multiply(detail.getPercentage())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                    ExpenseSplit split = new ExpenseSplit();
                    split.setExpense(expense);
                    split.setUser(user);
                    split.setAmount(amount);
                    split.setIsPaid(user.getId().equals(expense.getPaidBy().getId()));
                    splits.add(split);
                }
                break;
        }

        return splits;
    }

    public List<BalanceDTO> getGroupBalances(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Map<Long, BigDecimal> balances = new HashMap<>();

        // Initialize balances for all members
        for (User member : group.getMembers()) {
            balances.put(member.getId(), BigDecimal.ZERO);
        }

        // Calculate balances from all expenses
        for (Expense expense : group.getExpenses()) {
            Long paidById = expense.getPaidBy().getId();

            for (ExpenseSplit split : expense.getSplits()) {
                Long userId = split.getUser().getId();
                BigDecimal amount = split.getAmount();

                if (!split.getIsPaid()) {
                    // User owes money
                    balances.put(userId, balances.get(userId).subtract(amount));
                    // Paid user is owed money
                    balances.put(paidById, balances.get(paidById).add(amount));
                }
            }
        }

        return balances.entrySet().stream()
                .map(entry -> {
                    User user = userRepository.findById(entry.getKey()).orElseThrow();
                    return new BalanceDTO(entry.getKey(), user.getName(), entry.getValue());
                })
                .collect(Collectors.toList());
    }

    public List<DebtSimplificationService.SimplifiedTransaction> getSimplifiedTransactions(Long groupId) {
        List<BalanceDTO> balances = getGroupBalances(groupId);
        return debtSimplificationService.simplifyDebts(balances);
    }
}