package com.smartexpense.model;

public class UserBalance {
    private Long groupId;
    private Long userId;
    private String userName;
    private Double netBalance;

    public UserBalance() {}

    public UserBalance(Long groupId, Long userId, String userName, Double netBalance) {
        this.groupId = groupId;
        this.userId = userId;
        this.userName = userName;
        this.netBalance = netBalance;
    }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Double getNetBalance() { return netBalance; }
    public void setNetBalance(Double netBalance) { this.netBalance = netBalance; }
}