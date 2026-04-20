package com.example.expensesplit.algorithm;

import com.example.expensesplit.dto.BalanceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DebtSimplificationService {

    @Data
    @AllArgsConstructor
    public static class SimplifiedTransaction {
        private Long fromUserId;
        private String fromUserName;
        private Long toUserId;
        private String toUserName;
        private BigDecimal amount;
    }

    /**
     * Implements Minimum Cash Flow algorithm to reduce transactions
     * Uses greedy approach with max creditor and max debtor
     */
    public List<SimplifiedTransaction> simplifyDebts(List<BalanceDTO> balances) {
        List<SimplifiedTransaction> transactions = new ArrayList<>();

        // Create lists of creditors (people who are owed) and debtors (people who owe)
        PriorityQueue<BalanceDTO> creditors = new PriorityQueue<>(
                (a, b) -> b.getAmount().compareTo(a.getAmount())
        );
        PriorityQueue<BalanceDTO> debtors = new PriorityQueue<>(
                Comparator.comparing(BalanceDTO::getAmount)
        );

        for (BalanceDTO balance : balances) {
            if (balance.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                creditors.offer(balance);
            } else if (balance.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                debtors.offer(balance);
            }
        }

        // Simplify debts using greedy algorithm
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            BalanceDTO maxCreditor = creditors.poll();
            BalanceDTO maxDebtor = debtors.poll();

            BigDecimal debtAmount = maxDebtor.getAmount().abs();
            BigDecimal creditAmount = maxCreditor.getAmount();

            BigDecimal transactionAmount = debtAmount.min(creditAmount);

            transactions.add(new SimplifiedTransaction(
                    maxDebtor.getUserId(),
                    maxDebtor.getUserName(),
                    maxCreditor.getUserId(),
                    maxCreditor.getUserName(),
                    transactionAmount
            ));

            // Update balances
            maxCreditor.setAmount(creditAmount.subtract(transactionAmount));
            maxDebtor.setAmount(maxDebtor.getAmount().add(transactionAmount));

            // Re-add to queues if not settled
            if (maxCreditor.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                creditors.offer(maxCreditor);
            }
            if (maxDebtor.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                debtors.offer(maxDebtor);
            }
        }

        return transactions;
    }
}