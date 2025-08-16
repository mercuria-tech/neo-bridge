package com.neobridge.account.dto;

import com.neobridge.account.entity.Account.AccountType;
import com.neobridge.account.entity.Account.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for creating a new account in the NeoBridge platform.
 */
public class AccountCreateRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Account name is required")
    @Size(max = 100, message = "Account name must not exceed 100 characters")
    private String accountName;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Currency is required")
    private Currency currency;

    @DecimalMin(value = "0.01", message = "Initial deposit must be at least 0.01")
    private BigDecimal initialDeposit = BigDecimal.ZERO;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.01", message = "Daily limit must be at least 0.01")
    private BigDecimal dailyLimit;

    @DecimalMin(value = "0.01", message = "Monthly limit must be at least 0.01")
    private BigDecimal monthlyLimit;

    private Boolean isJointAccount = false;

    @Size(max = 500, message = "Joint account holders must not exceed 500 characters")
    private String jointAccountHolders;

    // Constructors
    public AccountCreateRequest() {}

    public AccountCreateRequest(UUID userId, String accountName, AccountType accountType, Currency currency) {
        this.userId = userId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.currency = currency;
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "AccountCreateRequest{" +
                "userId=" + userId +
                ", accountName='" + accountName + '\'' +
                ", accountType=" + accountType +
                ", currency=" + currency +
                ", initialDeposit=" + initialDeposit +
                ", description='" + description + '\'' +
                ", dailyLimit=" + dailyLimit +
                ", monthlyLimit=" + monthlyLimit +
                ", isJointAccount=" + isJointAccount +
                ", jointAccountHolders='" + jointAccountHolders + '\'' +
                '}';
    }
}
