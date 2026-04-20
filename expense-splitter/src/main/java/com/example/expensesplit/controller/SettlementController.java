package com.example.expensesplit.controller;

import com.example.expensesplit.model.Settlement;
import com.example.expensesplit.service.SettlementService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping
    public ResponseEntity<Settlement> recordSettlement(@RequestBody SettlementRequest request) {
        Settlement settlement = settlementService.recordSettlement(
                request.getGroupId(),
                request.getFromUserId(),
                request.getToUserId(),
                request.getAmount(),
                request.getNote()
        );
        return ResponseEntity.ok(settlement);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Settlement>> getGroupSettlements(@PathVariable Long groupId) {
        List<Settlement> settlements = settlementService.getGroupSettlements(groupId);
        return ResponseEntity.ok(settlements);
    }

    @Data
    static class SettlementRequest {
        private Long groupId;
        private Long fromUserId;
        private Long toUserId;
        private BigDecimal amount;
        private String note;
    }
}