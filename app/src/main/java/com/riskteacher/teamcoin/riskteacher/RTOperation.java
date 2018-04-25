package com.riskteacher.teamcoin.riskteacher;

import java.math.BigDecimal;

public class RTOperation {
    private String username;
    private String opType;
    private BigDecimal balance;
    private BigDecimal opProfit;
    private String opResult;
    private String opDate;

    public RTOperation(String username, String opType, BigDecimal balance, BigDecimal opProfit, String opResult, String opDate) {
        this.username = username;
        this.opType = opType;
        this.balance = balance;
        this.opProfit = opProfit;
        this.opResult = opResult;
        this.opDate = opDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getOpProfit() {
        return opProfit;
    }

    public void setOpProfit(BigDecimal opProfit) {
        this.opProfit = opProfit;
    }

    public String getOpResult() {
        return opResult;
    }

    public void setOpResult(String opResult) {
        this.opResult = opResult;
    }

    public String getOpDate() {
        return opDate;
    }

    public void setOpDate(String opDate) {
        this.opDate = opDate;
    }
}
