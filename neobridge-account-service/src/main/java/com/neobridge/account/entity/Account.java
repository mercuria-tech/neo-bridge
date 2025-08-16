package com.neobridge.account.entity;

import com.neobridge.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Account entity representing a bank account in the NeoBridge platform.
 * Supports multiple account types and currencies.
 */
@Entity
@Table(name = "accounts", indexes = {
    @Index(name = "idx_accounts_user_id", columnList = "user_id"),
    @Index(name = "idx_accounts_account_number", columnList = "account_number"),
    @Index(name = "idx_accounts_account_type", columnList = "account_type"),
    @Index(name = "idx_accounts_currency", columnList = "currency"),
    @Index(name = "idx_accounts_status", columnList = "status")
})
public class Account extends BaseEntity {

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @NotBlank
    @Size(max = 100)
    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "available_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "reserved_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal reservedBalance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "daily_limit", nullable = false, precision = 19, scale = 4)
    private BigDecimal dailyLimit = BigDecimal.valueOf(10000.00);

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "monthly_limit", nullable = false, precision = 19, scale = 4)
    private BigDecimal monthlyLimit = BigDecimal.valueOf(100000.00);

    @Column(name = "daily_transactions_count", nullable = false)
    private Integer dailyTransactionsCount = 0;

    @Column(name = "monthly_transactions_count", nullable = false)
    private Integer monthlyTransactionsCount = 0;

    @Column(name = "daily_transactions_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal dailyTransactionsAmount = BigDecimal.ZERO;

    @Column(name = "monthly_transactions_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal monthlyTransactionsAmount = BigDecimal.ZERO;

    @Column(name = "last_transaction_date")
    private LocalDateTime lastTransactionDate;

    @Column(name = "interest_rate", precision = 5, scale = 4)
    private BigDecimal interestRate = BigDecimal.ZERO;

    @Column(name = "last_interest_calculation")
    private LocalDateTime lastInterestCalculation;

    @Column(name = "overdraft_limit", precision = 19, scale = 4)
    private BigDecimal overdraftLimit = BigDecimal.ZERO;

    @Column(name = "overdraft_used", precision = 19, scale = 4)
    private BigDecimal overdraftUsed = BigDecimal.ZERO;

    @Column(name = "is_joint_account", nullable = false)
    private Boolean isJointAccount = false;

    @Column(name = "joint_account_holders")
    private String jointAccountHolders;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    // Enums
    public enum AccountType {
        CURRENT("Current Account"),
        SAVINGS("Savings Account"),
        FIXED_DEPOSIT("Fixed Deposit"),
        BUSINESS("Business Account"),
        STUDENT("Student Account"),
        SENIOR_CITIZEN("Senior Citizen Account"),
        PREMIUM("Premium Account"),
        BASIC("Basic Account");

        private final String displayName;

        AccountType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Currency {
        USD("US Dollar"),
        EUR("Euro"),
        GBP("British Pound"),
        CHF("Swiss Franc"),
        JPY("Japanese Yen"),
        AUD("Australian Dollar"),
        CAD("Canadian Dollar"),
        SEK("Swedish Krona"),
        NOK("Norwegian Krone"),
        DKK("Danish Krone"),
        PLN("Polish Zloty"),
        CZK("Czech Koruna"),
        HUF("Hungarian Forint"),
        RON("Romanian Leu"),
        BGN("Bulgarian Lev"),
        HRK("Croatian Kuna"),
        RUB("Russian Ruble"),
        TRY("Turkish Lira"),
        BRL("Brazilian Real"),
        MXN("Mexican Peso"),
        INR("Indian Rupee"),
        CNY("Chinese Yuan"),
        KRW("South Korean Won"),
        SGD("Singapore Dollar"),
        HKD("Hong Kong Dollar"),
        AED("UAE Dirham"),
        SAR("Saudi Riyal"),
        QAR("Qatari Riyal"),
        KWD("Kuwaiti Dinar"),
        BHD("Bahraini Dinar"),
        OMR("Omani Rial");

        private final String displayName;

        Currency(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum AccountStatus {
        ACTIVE("Active"),
        SUSPENDED("Suspended"),
        BLOCKED("Blocked"),
        CLOSED("Closed"),
        PENDING("Pending"),
        UNDER_REVIEW("Under Review");

        private final String displayName;

        AccountStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public Account() {}

    public Account(UUID userId, String accountNumber, String accountName, AccountType accountType, Currency currency) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountType = accountType;
        this.currency = currency;
    }

    // Helper methods
    public boolean hasSufficientBalance(BigDecimal amount) {
        return availableBalance.compareTo(amount) >= 0;
    }

    public boolean hasSufficientDailyLimit(BigDecimal amount) {
        return dailyTransactionsAmount.add(amount).compareTo(dailyLimit) <= 0;
    }

    public boolean hasSufficientMonthlyLimit(BigDecimal amount) {
        return monthlyTransactionsAmount.add(amount).compareTo(monthlyLimit) <= 0;
    }

    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        this.availableBalance = this.availableBalance.subtract(amount);
        updateTransactionCounts(amount);
    }

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.availableBalance = this.availableBalance.add(amount);
        updateTransactionCounts(amount);
    }

    public void reserve(BigDecimal amount) {
        this.availableBalance = this.availableBalance.subtract(amount);
        this.reservedBalance = this.reservedBalance.add(amount);
    }

    public void releaseReservation(BigDecimal amount) {
        this.reservedBalance = this.reservedBalance.subtract(amount);
        this.availableBalance = this.availableBalance.add(amount);
    }

    private void updateTransactionCounts(BigDecimal amount) {
        this.dailyTransactionsCount++;
        this.monthlyTransactionsCount++;
        this.dailyTransactionsAmount = this.dailyTransactionsAmount.add(amount);
        this.monthlyTransactionsAmount = this.monthlyTransactionsAmount.add(amount);
        this.lastTransactionDate = LocalDateTime.now();
    }

    public void resetDailyLimits() {
        this.dailyTransactionsCount = 0;
        this.dailyTransactionsAmount = BigDecimal.ZERO;
    }

    public void resetMonthlyLimits() {
        this.monthlyTransactionsCount = 0;
        this.monthlyTransactionsAmount = BigDecimal.ZERO;
    }

    public BigDecimal getTotalBalance() {
        return balance.add(overdraftLimit).subtract(overdraftUsed);
    }

    public boolean isOverdraftAvailable() {
        return overdraftUsed.compareTo(overdraftLimit) < 0;
    }

    public BigDecimal getRemainingOverdraft() {
        return overdraftLimit.subtract(overdraftUsed);
    }

    // Getters and Setters
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + getId() +
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
