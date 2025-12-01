package com.smartexpense.service;

import com.smartexpense.model.*;
import com.smartexpense.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private SettlementRepository settlementRepository;

    public List<UserBalance> getGroupBalances(Long groupId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (!groupOpt.isPresent()) {
            throw new IllegalArgumentException("Group not found");
        }
        
        Group group = groupOpt.get();
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        List<Settlement> settlements = settlementRepository.findByGroupId(groupId);
        
        Map<Long, Double> balances = new HashMap<>();
        
        // Initialize balances for all members
        for (Long memberId : group.getMemberIds()) {
            balances.put(memberId, 0.0);
        }
        
        // Calculate expense balances
        for (Expense expense : expenses) {
            balances.put(expense.getPaidByUserId(), 
                balances.get(expense.getPaidByUserId()) + expense.getAmount());
            
            if ("EQUAL".equals(expense.getSplitType())) {
                double sharePerPerson = expense.getAmount() / group.getMemberIds().size();
                for (Long memberId : group.getMemberIds()) {
                    balances.put(memberId, balances.get(memberId) - sharePerPerson);
                }
            } else if (expense.getShares() != null) {
                for (ExpenseShare share : expense.getShares()) {
                    balances.put(share.getUserId(), 
                        balances.get(share.getUserId()) - share.getAmount());
                }
            }
        }
        
        // Apply settlements
        for (Settlement settlement : settlements) {
            balances.put(settlement.getFromUserId(), 
                balances.get(settlement.getFromUserId()) - settlement.getAmount());
            balances.put(settlement.getToUserId(), 
                balances.get(settlement.getToUserId()) + settlement.getAmount());
        }
        
        return balances.entrySet().stream()
            .map(entry -> {
                User user = userRepository.findById(entry.getKey()).orElse(null);
                return new UserBalance(groupId, entry.getKey(), 
                    user != null ? user.getName() : "Unknown", entry.getValue());
            })
            .collect(Collectors.toList());
    }

    public SettlementPlan getSettlementPlan(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new IllegalArgumentException("Group not found");
        }
        
        List<UserBalance> balances = getGroupBalances(groupId);
        List<SettlementSuggestion> suggestions = new ArrayList<>();
        
        List<UserBalance> creditors = balances.stream()
            .filter(b -> b.getNetBalance() > 0.01)
            .sorted((a, b) -> Double.compare(b.getNetBalance(), a.getNetBalance()))
            .collect(Collectors.toList());
            
        List<UserBalance> debtors = balances.stream()
            .filter(b -> b.getNetBalance() < -0.01)
            .sorted((a, b) -> Double.compare(a.getNetBalance(), b.getNetBalance()))
            .collect(Collectors.toList());
        
        int i = 0, j = 0;
        while (i < creditors.size() && j < debtors.size()) {
            UserBalance creditor = creditors.get(i);
            UserBalance debtor = debtors.get(j);
            
            double amount = Math.min(creditor.getNetBalance(), -debtor.getNetBalance());
            
            suggestions.add(new SettlementSuggestion(
                debtor.getUserId(), debtor.getUserName(),
                creditor.getUserId(), creditor.getUserName(),
                amount
            ));
            
            creditor.setNetBalance(creditor.getNetBalance() - amount);
            debtor.setNetBalance(debtor.getNetBalance() + amount);
            
            if (Math.abs(creditor.getNetBalance()) < 0.01) i++;
            if (Math.abs(debtor.getNetBalance()) < 0.01) j++;
        }
        
        return new SettlementPlan(groupId, suggestions, suggestions.size());
    }
}