package com.neobridge.account.dto;

import com.neobridge.account.entity.Account;
import com.neobridge.account.entity.Account.AccountStatus;
import com.neobridge.account.entity.Account.AccountType;
import com.neobridge.account.entity.Account.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for account responses in the NeoBridge platform.
 */
public class AccountResponse {

    private UUID id;
    private UUID userId;
    private String accountNumber;
    private String accountName;
    private AccountType accountType;
    private Currency currency;
    private AccountStatus status;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private BigDecimal reservedBalance;
    private BigDecimal dailyLimit;
    private BigDecimal monthlyLimit;
    private Integer dailyTransactionsCount;
    private Integer monthlyTransactionsCount;
    private BigDecimal dailyTransactionsAmount;
    private BigDecimal monthlyTransactionsAmount;
    private LocalDateTime lastTransactionDate;
    private BigDecimal interestRate;
    private LocalDateTime lastInterestCalculation;
    private BigDecimal overdraftLimit;
    private BigDecimal overdraftUsed;
    private Boolean isJointAccount;
    private String jointAccountHolders;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Static factory method
    public static AccountResponse fromAccount(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setUserId(account.getUserId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountName(account.getAccountName());
        response.setAccountType(account.getAccountType());
        response.setCurrency(account.getCurrency());
        response.setStatus(account.getStatus());
        response.setBalance(account.getBalance());
        response.setAvailableBalance(account.getAvailableBalance());
        response.setReservedBalance(account.getReservedBalance());
        response.setDailyLimit(account.getDailyLimit());
        response.setMonthlyLimit(account.getMonthlyLimit());
        response.setDailyTransactionsCount(account.getDailyTransactionsCount());
        response.setMonthlyTransactionsCount(account.getMonthlyTransactionsCount());
        response.setDailyTransactionsAmount(account.getDailyTransactionsAmount());
        response.setMonthlyTransactionsAmount(account.getMonthlyTransactionsAmount());
        response.setLastTransactionDate(account.getLastTransactionDate());
        response.setInterestRate(account.getInterestRate());
        response.setLastInterestCalculation(account.getLastInterestCalculation());
        response.setOverdraftLimit(account.getOverdraftLimit());
        response.setOverdraftUsed(account.getOverdraftUsed());
        response.setIsJointAccount(account.getIsJointAccount());
        response.setJointAccountHolders(account.getJointAccountHolders());
        response.setDescription(account.getDescription());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getReservedBalance() {
        return reservedBalance;
    }

    public void setReservedBalance(BigDecimal reservedBalance) {
        this.reservedBalance = reservedBalance;
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public BigDecimal getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public Integer getDailyTransactionsCount() {
        return dailyTransactionsCount;
    }

    public void setDailyTransactionsCount(Integer dailyTransactionsCount) {
        this.dailyTransactionsCount = dailyTransactionsCount;
    }

    public Integer getMonthlyTransactionsCount() {
        return monthlyTransactionsCount;
    }

    public void setMonthlyTransactionsCount(Integer monthlyTransactionsCount) {
        this.monthlyTransactionsCount = monthlyTransactionsCount;
    }

    public BigDecimal getDailyTransactionsAmount() {
        return dailyTransactionsAmount;
    }

    public void setDailyTransactionsAmount(BigDecimal dailyTransactionsAmount) {
        this.dailyTransactionsAmount = dailyTransactionsAmount;
    }

    public BigDecimal getMonthlyTransactionsAmount() {
        return monthlyTransactionsAmount;
    }

    public void setMonthlyTransactionsAmount(BigDecimal monthlyTransactionsAmount) {
        this.monthlyTransactionsAmount = monthlyTransactionsAmount;
    }

    public LocalDateTime getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(LocalDateTime lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDateTime getLastInterestCalculation() {
        return lastInterestCalculation;
    }

    public void setLastInterestCalculation(LocalDateTime lastInterestCalculation) {
        this.lastInterestCalculation = lastInterestCalculation;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public BigDecimal getOverdraftUsed() {
        return overdraftUsed;
    }

    public void setOverdraftUsed(BigDecimal overdraftUsed) {
        this.overdraftUsed = overdraftUsed;
    }

    public Boolean getIsJointAccount() {
        return isJointAccount;
    }

    public void setIsJointAccount(Boolean isJointAccount) {
        this.isJointAccount = isJointAccount;
    }

    public String getJointAccountHolders() {
        return jointAccountHolders;
    }

    public void setJointAccountHolders(String jointAccountHolders) {
        this.jointAccountHolders = jointAccountHolders;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                "id=" + id +
                ", userId=" + userId +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountType=" + accountType +
                ", currency=" + currency +
                ", status=" + status +
                ", balance=" + balance +
                ", availableBalance=" + availableBalance +
                ", dailyLimit=" + dailyLimit +
                ", monthlyLimit=" + monthlyLimit +
                '}';
    }
}
