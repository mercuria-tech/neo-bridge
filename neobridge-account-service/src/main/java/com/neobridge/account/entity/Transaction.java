package com.neobridge.account.entity;

import com.neobridge.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction entity representing financial transactions in NeoBridge accounts.
 * Tracks all money movements and account activities.
 */
@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_transactions_account_id", columnList = "account_id"),
    @Index(name = "idx_transactions_transaction_type", columnList = "transaction_type"),
    @Index(name = "idx_transactions_status", columnList = "status"),
    @Index(name = "idx_transactions_created_at", columnList = "created_at"),
    @Index(name = "idx_transactions_reference", columnList = "reference")
})
public class Transaction extends BaseEntity {

    @NotNull
    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @DecimalMin(value = "0.01", inclusive = true)
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "balance_before", precision = 19, scale = 4)
    private BigDecimal balanceBefore;

    @Column(name = "balance_after", precision = 19, scale = 4)
    private BigDecimal balanceAfter;

    @Size(max = 100)
    @Column(name = "reference")
    private String reference;

    @Size(max = 100)
    @Column(name = "external_reference")
    private String externalReference;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @Size(max = 100)
    @Column(name = "counterparty_name")
    private String counterpartyName;

    @Size(max = 100)
    @Column(name = "counterparty_account")
    private String counterpartyAccount;

    @Size(max = 100)
    @Column(name = "counterparty_bank")
    private String counterpartyBank;

    @Size(max = 50)
    @Column(name = "counterparty_swift")
    private String counterpartySwift;

    @Size(max = 50)
    @Column(name = "counterparty_iban")
    private String counterpartyIban;

    @Column(name = "exchange_rate", precision = 19, scale = 6)
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @Column(name = "original_amount", precision = 19, scale = 4)
    private BigDecimal originalAmount;

    @Column(name = "original_currency")
    private String originalCurrency;

    @Column(name = "fee_amount", precision = 19, scale = 4)
    private BigDecimal feeAmount = BigDecimal.ZERO;

    @Column(name = "fee_currency")
    private String feeCurrency;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @Column(name = "settlement_date")
    private LocalDateTime settlementDate;

    @Size(max = 1000)
    @Column(name = "metadata")
    private String metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    // Enums
    public enum TransactionType {
        DEPOSIT("Deposit"),
        WITHDRAWAL("Withdrawal"),
        TRANSFER("Transfer"),
        PAYMENT("Payment"),
        RECEIPT("Receipt"),
        INTEREST("Interest"),
        FEE("Fee"),
        CHARGEBACK("Chargeback"),
        REFUND("Refund"),
        EXCHANGE("Currency Exchange"),
        LOAN_DISBURSEMENT("Loan Disbursement"),
        LOAN_REPAYMENT("Loan Repayment"),
        INVESTMENT("Investment"),
        DIVIDEND("Dividend"),
        TAX("Tax"),
        INSURANCE("Insurance"),
        SUBSCRIPTION("Subscription"),
        PURCHASE("Purchase"),
        SALE("Sale"),
        ADJUSTMENT("Balance Adjustment");

        private final String displayName;

        TransactionType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum TransactionStatus {
        PENDING("Pending"),
        PROCESSING("Processing"),
        COMPLETED("Completed"),
        FAILED("Failed"),
        CANCELLED("Cancelled"),
        REVERSED("Reversed"),
        SUSPENDED("Suspended"),
        UNDER_REVIEW("Under Review");

        private final String displayName;

        TransactionStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public Transaction() {}

    public Transaction(UUID accountId, UUID userId, TransactionType transactionType, 
                      BigDecimal amount, String currency, String description) {
        this.accountId = accountId;
        this.userId = userId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
    }

    // Helper methods
    public boolean isDebit() {
        return transactionType == TransactionType.WITHDRAWAL ||
               transactionType == TransactionType.TRANSFER ||
               transactionType == TransactionType.PAYMENT ||
               transactionType == TransactionType.FEE ||
               transactionType == TransactionType.PURCHASE;
    }

    public boolean isCredit() {
        return transactionType == TransactionType.DEPOSIT ||
               transactionType == TransactionType.RECEIPT ||
               transactionType == TransactionType.INTEREST ||
               transactionType == TransactionType.REFUND ||
               transactionType == TransactionType.DIVIDEND ||
               transactionType == TransactionType.SALE;
    }

    public boolean isCompleted() {
        return status == TransactionStatus.COMPLETED;
    }

    public boolean isPending() {
        return status == TransactionStatus.PENDING;
    }

    public boolean isFailed() {
        return status == TransactionStatus.FAILED;
    }

    public BigDecimal getNetAmount() {
        return amount.subtract(feeAmount);
    }

    public void markAsCompleted() {
        this.status = TransactionStatus.COMPLETED;
        this.processingDate = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.status = TransactionStatus.FAILED;
        this.processingDate = LocalDateTime.now();
    }

    public void markAsProcessing() {
        this.status = TransactionStatus.PROCESSING;
    }

    // Getters and Setters
    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterpartyAccount() {
        return counterpartyAccount;
    }

    public void setCounterpartyAccount(String counterpartyAccount) {
        this.counterpartyAccount = counterpartyAccount;
    }

    public String getCounterpartyBank() {
        return counterpartyBank;
    }

    public void setCounterpartyBank(String counterpartyBank) {
        this.counterpartyBank = counterpartyBank;
    }

    public String getCounterpartySwift() {
        return counterpartySwift;
    }

    public void setCounterpartySwift(String counterpartySwift) {
        this.counterpartySwift = counterpartySwift;
    }

    public String getCounterpartyIban() {
        return counterpartyIban;
    }

    public void setCounterpartyIban(String counterpartyIban) {
        this.counterpartyIban = counterpartyIban;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getFeeCurrency() {
        return feeCurrency;
    }

    public void setFeeCurrency(String feeCurrency) {
        this.feeCurrency = feeCurrency;
    }

    public LocalDateTime getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(LocalDateTime processingDate) {
        this.processingDate = processingDate;
    }

    public LocalDateTime getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDateTime settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + getId() +
                ", accountId=" + accountId +
                ", userId=" + userId +
                ", transactionType=" + transactionType +
                ", status=" + status +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", reference='" + reference + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
