package com.smartexpense.model;

import java.util.List;

public class SettlementPlan {
    private Long groupId;
    private List<SettlementSuggestion> suggestions;
    private Integer transactionCount;

    public SettlementPlan() {}

    public SettlementPlan(Long groupId, List<SettlementSuggestion> suggestions, Integer transactionCount) {
        this.groupId = groupId;
        this.suggestions = suggestions;
        this.transactionCount = transactionCount;
    }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public List<SettlementSuggestion> getSuggestions() { return suggestions; }
    public void setSuggestions(List<SettlementSuggestion> suggestions) { this.suggestions = suggestions; }

    public Integer getTransactionCount() { return transactionCount; }
    public void setTransactionCount(Integer transactionCount) { this.transactionCount = transactionCount; }
}