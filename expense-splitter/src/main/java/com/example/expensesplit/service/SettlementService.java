package com.example.expensesplit.service;

import com.example.expensesplit.model.Group;
import com.example.expensesplit.model.Settlement;
import com.example.expensesplit.model.User;
import com.example.expensesplit.repository.GroupRepository;
import com.example.expensesplit.repository.SettlementRepository;
import com.example.expensesplit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Transactional
    public Settlement recordSettlement(Long groupId, Long fromUserId, Long toUserId,
                                       BigDecimal amount, String note) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("From user not found"));

        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("To user not found"));

        Settlement settlement = new Settlement();
        settlement.setGroup(group);
        settlement.setFromUser(fromUser);
        settlement.setToUser(toUser);
        settlement.setAmount(amount);
        settlement.setNote(note);

        return settlementRepository.save(settlement);
    }

    public List<Settlement> getGroupSettlements(Long groupId) {
        return settlementRepository.findByGroupIdOrderBySettledAtDesc(groupId);
    }

    public List<Settlement> getUserSettlements(Long userId) {
        return settlementRepository.findByUserIdOrderBySettledAtDesc(userId);
    }
}