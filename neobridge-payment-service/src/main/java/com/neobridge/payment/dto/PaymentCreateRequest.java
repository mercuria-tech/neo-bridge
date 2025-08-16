package com.neobridge.payment.dto;

import com.neobridge.payment.entity.Payment.PaymentDirection;
import com.neobridge.payment.entity.Payment.PaymentMethod;
import com.neobridge.payment.entity.Payment.PaymentPriority;
import com.neobridge.payment.entity.Payment.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for creating a new payment in the NeoBridge platform.
 */
public class PaymentCreateRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Source account ID is required")
    private UUID sourceAccountId;

    private UUID destinationAccountId;

    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment direction is required")
    private PaymentDirection direction;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(max = 3, message = "Currency must be 3 characters")
    private String currency;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Size(max = 100, message = "Reference must not exceed 100 characters")
    private String reference;

    @Size(max = 100, message = "External reference must not exceed 100 characters")
    private String externalReference;

    // Counterparty information
    @Size(max = 100, message = "Counterparty name must not exceed 100 characters")
    private String counterpartyName;

    @Size(max = 100, message = "Counterparty account must not exceed 100 characters")
    private String counterpartyAccount;

    @Size(max = 100, message = "Counterparty bank must not exceed 100 characters")
    private String counterpartyBank;

    @Size(max = 50, message = "Counterparty SWIFT must not exceed 50 characters")
    private String counterpartySwift;

    @Size(max = 50, message = "Counterparty IBAN must not exceed 50 characters")
    private String counterpartyIban;

    @Size(max = 100, message = "Counterparty BIC must not exceed 100 characters")
    private String counterpartyBic;

    @Size(max = 100, message = "Counterparty routing must not exceed 100 characters")
    private String counterpartyRouting;

    // Payment options
    private PaymentPriority priority = PaymentPriority.NORMAL;

    private LocalDateTime scheduledDate;

    private Boolean isUrgent = false;

    private Boolean isBatchPayment = false;

    private String batchId;

    private Integer batchSequence;

    // Fee information
    private BigDecimal feeAmount;

    private String feeCurrency;

    // Exchange rate information
    private BigDecimal exchangeRate;

    private String originalCurrency;

    // Metadata
    @Size(max = 1000, message = "Metadata must not exceed 1000 characters")
    private String metadata;

    // Constructors
    public PaymentCreateRequest() {}

    public PaymentCreateRequest(UUID userId, UUID sourceAccountId, PaymentType paymentType,
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
    }

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "PaymentCreateRequest{" +
                "userId=" + userId +
                ", sourceAccountId=" + sourceAccountId +
                ", paymentType=" + paymentType +
                ", paymentMethod=" + paymentMethod +
                ", direction=" + direction +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", reference='" + reference + '\'' +
                ", counterpartyName='" + counterpartyName + '\'' +
                ", priority=" + priority +
                '}';
    }
}
