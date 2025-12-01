package com.smartexpense.model;

import jakarta.persistence.*;

@Embeddable
public class ExpenseShare {
    private Long userId;
    private Double amount;

    public ExpenseShare() {}

    public ExpenseShare(Long userId, Double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}