package com.neobridge.payment.entity;

import com.neobridge.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payment entity representing financial payments in the NeoBridge platform.
 * Supports domestic and international payments with comprehensive tracking.
 */
@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payments_payment_id", columnList = "payment_id"),
    @Index(name = "idx_payments_user_id", columnList = "user_id"),
    @Index(name = "idx_payments_status", columnList = "status"),
    @Index(name = "idx_payments_payment_type", columnList = "payment_type"),
    @Index(name = "idx_payments_created_at", columnList = "created_at"),
    @Index(name = "idx_payments_reference", columnList = "reference")
})
public class Payment extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "payment_id", unique = true, nullable = false)
    private String paymentId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull
    @Column(name = "source_account_id", nullable = false)
    private UUID sourceAccountId;

    @Column(name = "destination_account_id")
    private UUID destinationAccountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private PaymentDirection direction;

    @DecimalMin(value = "0.01", inclusive = true)
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

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

    @Column(name = "total_amount", precision = 19, scale = 4)
    private BigDecimal totalAmount;

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

    @Size(max = 100)
    @Column(name = "counterparty_bic")
    private String counterpartyBic;

    @Size(max = 100)
    @Column(name = "counterparty_routing")
    private String counterpartyRouting;

    @Column(name = "priority")
    private PaymentPriority priority = PaymentPriority.NORMAL;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @Column(name = "settlement_date")
    private LocalDateTime settlementDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "failure_date")
    private LocalDateTime failureDate;

    @Size(max = 1000)
    @Column(name = "failure_reason")
    private String failureReason;

    @Size(max = 1000)
    @Column(name = "failure_code")
    private String failureCode;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "max_retries")
    private Integer maxRetries = 3;

    @Column(name = "next_retry_date")
    private LocalDateTime nextRetryDate;

    @Size(max = 1000)
    @Column(name = "metadata")
    private String metadata;

    @Size(max = 1000)
    @Column(name = "compliance_data")
    private String complianceData;

    @Column(name = "compliance_status")
    private ComplianceStatus complianceStatus = ComplianceStatus.PENDING;

    @Column(name = "fraud_score")
    private Integer fraudScore = 0;

    @Column(name = "risk_level")
    private RiskLevel riskLevel = RiskLevel.LOW;

    @Column(name = "is_urgent")
    private Boolean isUrgent = false;

    @Column(name = "is_batch_payment")
    private Boolean isBatchPayment = false;

    @Column(name = "batch_id")
    private String batchId;

    @Column(name = "batch_sequence")
    private Integer batchSequence;

    // Enums
    public enum PaymentType {
        DOMESTIC_TRANSFER("Domestic Transfer"),
        INTERNATIONAL_TRANSFER("International Transfer"),
        SEPA_TRANSFER("SEPA Transfer"),
        SWIFT_TRANSFER("SWIFT Transfer"),
        CARD_PAYMENT("Card Payment"),
        CRYPTO_PAYMENT("Crypto Payment"),
        BILL_PAYMENT("Bill Payment"),
        LOAN_PAYMENT("Loan Payment"),
        INVESTMENT_PAYMENT("Investment Payment"),
        SUBSCRIPTION_PAYMENT("Subscription Payment"),
        RECURRING_PAYMENT("Recurring Payment"),
        INSTANT_PAYMENT("Instant Payment"),
        BATCH_PAYMENT("Batch Payment");

        private final String displayName;

        PaymentType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum PaymentMethod {
        BANK_TRANSFER("Bank Transfer"),
        CARD("Card"),
        CRYPTO("Cryptocurrency"),
        CASH("Cash"),
        CHECK("Check"),
        WIRE_TRANSFER("Wire Transfer"),
        ACH("ACH"),
        SEPA("SEPA"),
        SWIFT("SWIFT"),
        MOBILE_PAYMENT("Mobile Payment"),
        DIGITAL_WALLET("Digital Wallet");

        private final String displayName;

        PaymentMethod(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum PaymentStatus {
        PENDING("Pending"),
        PROCESSING("Processing"),
        AUTHORIZED("Authorized"),
        COMPLETED("Completed"),
        FAILED("Failed"),
        CANCELLED("Cancelled"),
        REVERSED("Reversed"),
        REFUNDED("Refunded"),
        SUSPENDED("Suspended"),
        UNDER_REVIEW("Under Review"),
        COMPLIANCE_CHECK("Compliance Check"),
        FRAUD_CHECK("Fraud Check");

        private final String displayName;

        PaymentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum PaymentDirection {
        OUTBOUND("Outbound"),
        INBOUND("Inbound");

        private final String displayName;

        PaymentDirection(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum PaymentPriority {
        LOW("Low"),
        NORMAL("Normal"),
        HIGH("High"),
        URGENT("Urgent");

        private final String displayName;

        PaymentPriority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum ComplianceStatus {
        PENDING("Pending"),
        APPROVED("Approved"),
        REJECTED("Rejected"),
        UNDER_REVIEW("Under Review"),
        ESCALATED("Escalated");

        private final String displayName;

        ComplianceStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum RiskLevel {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        CRITICAL("Critical");

        private final String displayName;

        RiskLevel(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public Payment() {}

    public Payment(UUID userId, UUID sourceAccountId, PaymentType paymentType, 
                   PaymentMethod paymentMethod, PaymentDirection direction,
                   BigDecimal amount, String currency, String description) {
        this.userId = userId;
        this.sourceAccountId = sourceAccountId;
        this.paymentType = paymentType;
        this.paymentMethod = paymentMethod;
        this.direction = direction;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.totalAmount = amount.add(feeAmount);
    }

    // Helper methods
    public boolean isCompleted() {
        return status == PaymentStatus.COMPLETED;
    }

    public boolean isFailed() {
        return status == PaymentStatus.FAILED;
    }

    public boolean isPending() {
        return status == PaymentStatus.PENDING;
    }

    public boolean isProcessing() {
        return status == PaymentStatus.PROCESSING;
    }

    public boolean canRetry() {
        return retryCount < maxRetries && status == PaymentStatus.FAILED;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }

    public void markAsProcessing() {
        this.status = PaymentStatus.PROCESSING;
        this.processingDate = LocalDateTime.now();
    }

    public void markAsCompleted() {
        this.status = PaymentStatus.COMPLETED;
        this.completionDate = LocalDateTime.now();
    }

    public void markAsFailed(String reason, String code) {
        this.status = PaymentStatus.FAILED;
        this.failureDate = LocalDateTime.now();
        this.failureReason = reason;
        this.failureCode = code;
    }

    public void markAsCancelled() {
        this.status = PaymentStatus.CANCELLED;
    }

    public void markAsUnderReview() {
        this.status = PaymentStatus.UNDER_REVIEW;
    }

    public void markAsComplianceCheck() {
        this.status = PaymentStatus.COMPLIANCE_CHECK;
    }

    public void markAsFraudCheck() {
        this.status = PaymentStatus.FRAUD_CHECK;
    }

    public BigDecimal getNetAmount() {
        return amount.subtract(feeAmount);
    }

    public boolean isInternational() {
        return paymentType == PaymentType.INTERNATIONAL_TRANSFER || 
               paymentType == PaymentType.SWIFT_TRANSFER;
    }

    public boolean isDomestic() {
        return paymentType == PaymentType.DOMESTIC_TRANSFER || 
               paymentType == PaymentType.SEPA_TRANSFER;
    }

    public boolean isInstant() {
        return paymentType == PaymentType.INSTANT_PAYMENT;
    }

    public boolean isRecurring() {
        return paymentType == PaymentType.RECURRING_PAYMENT;
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(UUID sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public UUID getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(UUID destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentDirection getDirection() {
        return direction;
    }

    public void setDirection(PaymentDirection direction) {
        this.direction = direction;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getCounterpartyBic() {
        return counterpartyBic;
    }

    public void setCounterpartyBic(String counterpartyBic) {
        this.counterpartyBic = counterpartyBic;
    }

    public String getCounterpartyRouting() {
        return counterpartyRouting;
    }

    public void setCounterpartyRouting(String counterpartyRouting) {
        this.counterpartyRouting = counterpartyRouting;
    }

    public PaymentPriority getPriority() {
        return priority;
    }

    public void setPriority(PaymentPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
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

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public LocalDateTime getFailureDate() {
        return failureDate;
    }

    public void setFailureDate(LocalDateTime failureDate) {
        this.failureDate = failureDate;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(String failureCode) {
        this.failureCode = failureCode;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public LocalDateTime getNextRetryDate() {
        return nextRetryDate;
    }

    public void setNextRetryDate(LocalDateTime nextRetryDate) {
        this.nextRetryDate = nextRetryDate;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getComplianceData() {
        return complianceData;
    }

    public void setComplianceData(String complianceData) {
        this.complianceData = complianceData;
    }

    public ComplianceStatus getComplianceStatus() {
        return complianceStatus;
    }

    public void setComplianceStatus(ComplianceStatus complianceStatus) {
        this.complianceStatus = complianceStatus;
    }

    public Integer getFraudScore() {
        return fraudScore;
    }

    public void setFraudScore(Integer fraudScore) {
        this.fraudScore = fraudScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public Boolean getIsBatchPayment() {
        return isBatchPayment;
    }

    public void setIsBatchPayment(Boolean isBatchPayment) {
        this.isBatchPayment = isBatchPayment;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getBatchSequence() {
        return batchSequence;
    }

    public void setBatchSequence(Integer batchSequence) {
        this.batchSequence = batchSequence;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + getId() +
                ", paymentId='" + paymentId + '\'' +
                ", userId=" + userId +
                ", paymentType=" + paymentType +
                ", status=" + status +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", reference='" + reference + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
