package com.smartexpense.controller;

import com.smartexpense.model.Settlement;
import com.smartexpense.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/settlements")
public class SettlementController {
    @Autowired
    private SettlementService settlementService;

    @PostMapping
    public ResponseEntity<Settlement> addSettlement(@Valid @RequestBody Settlement settlement) {
        try {
            Settlement createdSettlement = settlementService.createSettlement(settlement);
            return new ResponseEntity<>(createdSettlement, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Settlement>> listSettlementsByGroup(@PathVariable Long groupId) {
        List<Settlement> settlements = settlementService.getSettlementsByGroup(groupId);
        return ResponseEntity.ok(settlements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Settlement> getSettlementById(@PathVariable Long id) {
        Optional<Settlement> settlement = settlementService.getSettlementById(id);
        return settlement.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSettlement(@PathVariable Long id) {
        boolean deleted = settlementService.deleteSettlement(id);
        return deleted ? ResponseEntity.noContent().build() 
                       : ResponseEntity.notFound().build();
    }
}