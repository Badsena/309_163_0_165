package com.smartexpense.service;

import com.smartexpense.model.Expense;
import com.smartexpense.model.ExpenseShare;
import com.smartexpense.repository.ExpenseRepository;
import com.smartexpense.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private GroupRepository groupRepository;

    public Expense createExpense(Expense expense) {
        if (!groupRepository.existsById(expense.getGroupId())) {
            throw new IllegalArgumentException("Group not found");
        }
        
        if ("EXACT".equals(expense.getSplitType()) && expense.getShares() != null) {
            double totalShares = expense.getShares().stream()
                .mapToDouble(ExpenseShare::getAmount)
                .sum();
            if (Math.abs(totalShares - expense.getAmount()) > 0.01) {
                throw new IllegalArgumentException("Share amounts don't match total expense");
            }
        }
        
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByGroup(Long groupId) {
        return expenseRepository.findByGroupId(groupId);
    }

    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    public Expense updateExpense(Long id, Expense expense) {
        if (expenseRepository.existsById(id)) {
            expense.setId(id);
            return expenseRepository.save(expense);
        }
        return null;
    }

    public boolean deleteExpense(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}