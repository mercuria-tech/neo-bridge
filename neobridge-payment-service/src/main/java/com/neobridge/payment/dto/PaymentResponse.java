package com.neobridge.payment.dto;

import com.neobridge.payment.entity.Payment;
import com.neobridge.payment.entity.Payment.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for payment responses in the NeoBridge platform.
 */
public class PaymentResponse {

    private UUID id;
    private String paymentId;
    private UUID userId;
    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private PaymentType paymentType;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private PaymentDirection direction;
    private BigDecimal amount;
    private String currency;
    private BigDecimal exchangeRate;
    private BigDecimal originalAmount;
    private String originalCurrency;
    private BigDecimal feeAmount;
    private String feeCurrency;
    private BigDecimal totalAmount;
    private String reference;
    private String externalReference;
    private String description;
    private String counterpartyName;
    private String counterpartyAccount;
    private String counterpartyBank;
    private String counterpartySwift;
    private String counterpartyIban;
    private String counterpartyBic;
    private String counterpartyRouting;
    private PaymentPriority priority;
    private LocalDateTime scheduledDate;
    private LocalDateTime processingDate;
    private LocalDateTime settlementDate;
    private LocalDateTime completionDate;
    private LocalDateTime failureDate;
    private String failureReason;
    private String failureCode;
    private Integer retryCount;
    private Integer maxRetries;
    private LocalDateTime nextRetryDate;
    private String metadata;
    private String complianceData;
    private ComplianceStatus complianceStatus;
    private Integer fraudScore;
    private RiskLevel riskLevel;
    private Boolean isUrgent;
    private Boolean isBatchPayment;
    private String batchId;
    private Integer batchSequence;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Static factory method
    public static PaymentResponse fromPayment(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setPaymentId(payment.getPaymentId());
        response.setUserId(payment.getUserId());
        response.setSourceAccountId(payment.getSourceAccountId());
        response.setDestinationAccountId(payment.getDestinationAccountId());
        response.setPaymentType(payment.getPaymentType());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setDirection(payment.getDirection());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setExchangeRate(payment.getExchangeRate());
        response.setOriginalAmount(payment.getOriginalAmount());
        response.setOriginalCurrency(payment.getOriginalCurrency());
        response.setFeeAmount(payment.getFeeAmount());
        response.setFeeCurrency(payment.getFeeCurrency());
        response.setTotalAmount(payment.getTotalAmount());
        response.setReference(payment.getReference());
        response.setExternalReference(payment.getExternalReference());
        response.setDescription(payment.getDescription());
        response.setCounterpartyName(payment.getCounterpartyName());
        response.setCounterpartyAccount(payment.getCounterpartyAccount());
        response.setCounterpartyBank(payment.getCounterpartyBank());
        response.setCounterpartySwift(payment.getCounterpartySwift());
        response.setCounterpartyIban(payment.getCounterpartyIban());
        response.setCounterpartyBic(payment.getCounterpartyBic());
        response.setCounterpartyRouting(payment.getCounterpartyRouting());
        response.setPriority(payment.getPriority());
        response.setScheduledDate(payment.getScheduledDate());
        response.setProcessingDate(payment.getProcessingDate());
        response.setSettlementDate(payment.getSettlementDate());
        response.setCompletionDate(payment.getCompletionDate());
        response.setFailureDate(payment.getFailureDate());
        response.setFailureReason(payment.getFailureReason());
        response.setFailureCode(payment.getFailureCode());
        response.setRetryCount(payment.getRetryCount());
        response.setMaxRetries(payment.getMaxRetries());
        response.setNextRetryDate(payment.getNextRetryDate());
        response.setMetadata(payment.getMetadata());
        response.setComplianceData(payment.getComplianceData());
        response.setComplianceStatus(payment.getComplianceStatus());
        response.setFraudScore(payment.getFraudScore());
        response.setRiskLevel(payment.getRiskLevel());
        response.setIsUrgent(payment.getIsUrgent());
        response.setIsBatchPayment(payment.getIsBatchPayment());
        response.setBatchId(payment.getBatchId());
        response.setBatchSequence(payment.getBatchSequence());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        return response;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
        return "PaymentResponse{" +
                "id=" + id +
                ", paymentId='" + paymentId + '\'' +
                ", userId=" + userId +
                ", paymentType=" + paymentType +
                ", status=" + status +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", reference='" + reference + '\'' +
                ", description='" + description + '\'' +
                ", counterpartyName='" + counterpartyName + '\'' +
                '}';
    }
}
