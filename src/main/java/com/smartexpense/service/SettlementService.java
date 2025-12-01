package com.smartexpense.service;

import com.smartexpense.model.Settlement;
import com.smartexpense.repository.SettlementRepository;
import com.smartexpense.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SettlementService {
    @Autowired
    private SettlementRepository settlementRepository;
    
    @Autowired
    private GroupRepository groupRepository;

    public Settlement createSettlement(Settlement settlement) {
        if (!groupRepository.existsById(settlement.getGroupId())) {
            throw new IllegalArgumentException("Group not found");
        }
        
        if (settlement.getFromUserId().equals(settlement.getToUserId())) {
            throw new IllegalArgumentException("From and To users cannot be the same");
        }
        
        return settlementRepository.save(settlement);
    }

    public List<Settlement> getSettlementsByGroup(Long groupId) {
        return settlementRepository.findByGroupId(groupId);
    }

    public Optional<Settlement> getSettlementById(Long id) {
        return settlementRepository.findById(id);
    }

    public boolean deleteSettlement(Long id) {
        if (settlementRepository.existsById(id)) {
            settlementRepository.deleteById(id);
            return true;
        }
        return false;
    }
}