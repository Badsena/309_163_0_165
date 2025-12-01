package com.smartexpense.controller;

import com.smartexpense.model.SettlementPlan;
import com.smartexpense.model.UserBalance;
import com.smartexpense.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/groups/{groupId}/balances")
    public ResponseEntity<List<UserBalance>> getGroupBalances(@PathVariable Long groupId) {
        try {
            List<UserBalance> balances = reportService.getGroupBalances(groupId);
            return ResponseEntity.ok(balances);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/groups/{groupId}/settlement-plan")
    public ResponseEntity<SettlementPlan> getSettlementPlan(@PathVariable Long groupId) {
        try {
            SettlementPlan plan = reportService.getSettlementPlan(groupId);
            return ResponseEntity.ok(plan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}