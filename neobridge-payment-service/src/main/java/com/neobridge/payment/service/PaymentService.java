package com.neobridge.payment.service;

import com.neobridge.payment.dto.PaymentCreateRequest;
import com.neobridge.payment.dto.PaymentResponse;
import com.neobridge.payment.entity.Payment;
import com.neobridge.payment.exception.PaymentNotFoundException;
import com.neobridge.payment.exception.PaymentProcessingException;
import com.neobridge.payment.exception.InvalidPaymentRequestException;
import com.neobridge.payment.repository.PaymentRepository;
import com.neobridge.payment.util.PaymentIdGenerator;
import com.neobridge.payment.util.PaymentFeeCalculator;
import com.neobridge.payment.util.PaymentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing payments in the NeoBridge platform.
 * Handles payment creation, processing, settlement, and compliance.
 */
@Service
@Transactional
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentIdGenerator paymentIdGenerator;

    @Autowired
    private PaymentFeeCalculator feeCalculator;

    @Autowired
    private PaymentValidator paymentValidator;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Create a new payment.
     */
    public PaymentResponse createPayment(PaymentCreateRequest request) {
        logger.info("Creating payment for user: {}, amount: {} {}", 
                   request.getUserId(), request.getAmount(), request.getCurrency());

        // Validate payment request
        paymentValidator.validatePaymentRequest(request);

        // Generate unique payment ID
        String paymentId = paymentIdGenerator.generatePaymentId(request.getPaymentType());

        // Calculate fees
        BigDecimal feeAmount = feeCalculator.calculateFee(request.getPaymentType(), request.getAmount(), request.getCurrency());
        String feeCurrency = request.getFeeCurrency() != null ? request.getFeeCurrency() : request.getCurrency();

        // Create payment entity
        Payment payment = new Payment(
            request.getUserId(),
            request.getSourceAccountId(),
            request.getPaymentType(),
            request.getPaymentMethod(),
            request.getDirection(),
            request.getAmount(),
            request.getCurrency(),
            request.getDescription()
        );

        // Set payment ID
        payment.setPaymentId(paymentId);

        // Set additional properties
        if (request.getDestinationAccountId() != null) {
            payment.setDestinationAccountId(request.getDestinationAccountId());
        }
        if (request.getReference() != null) {
            payment.setReference(request.getReference());
        }
        if (request.getExternalReference() != null) {
            payment.setExternalReference(request.getExternalReference());
        }
        if (request.getCounterpartyName() != null) {
            payment.setCounterpartyName(request.getCounterpartyName());
        }
        if (request.getCounterpartyAccount() != null) {
            payment.setCounterpartyAccount(request.getCounterpartyAccount());
        }
        if (request.getCounterpartyBank() != null) {
            payment.setCounterpartyBank(request.getCounterpartyBank());
        }
        if (request.getCounterpartySwift() != null) {
            payment.setCounterpartySwift(request.getCounterpartySwift());
        }
        if (request.getCounterpartyIban() != null) {
            payment.setCounterpartyIban(request.getCounterpartyIban());
        }
        if (request.getCounterpartyBic() != null) {
            payment.setCounterpartyBic(request.getCounterpartyBic());
        }
        if (request.getCounterpartyRouting() != null) {
            payment.setCounterpartyRouting(request.getCounterpartyRouting());
        }
        if (request.getPriority() != null) {
            payment.setPriority(request.getPriority());
        }
        if (request.getScheduledDate() != null) {
            payment.setScheduledDate(request.getScheduledDate());
        }
        if (request.getIsUrgent() != null) {
            payment.setIsUrgent(request.getIsUrgent());
        }
        if (request.getIsBatchPayment() != null) {
            payment.setIsBatchPayment(request.getIsBatchPayment());
        }
        if (request.getBatchId() != null) {
            payment.setBatchId(request.getBatchId());
        }
        if (request.getBatchSequence() != null) {
            payment.setBatchSequence(request.getBatchSequence());
        }
        if (request.getExchangeRate() != null) {
            payment.setExchangeRate(request.getExchangeRate());
        }
        if (request.getOriginalCurrency() != null) {
            payment.setOriginalCurrency(request.getOriginalCurrency());
            payment.setOriginalAmount(request.getAmount());
        }
        if (request.getMetadata() != null) {
            payment.setMetadata(request.getMetadata());
        }

        // Set fee information
        payment.setFeeAmount(feeAmount);
        payment.setFeeCurrency(feeCurrency);
        payment.setTotalAmount(request.getAmount().add(feeAmount));

        // Set compliance and risk assessment
        payment.setComplianceStatus(Payment.ComplianceStatus.PENDING);
        payment.setRiskLevel(assessRiskLevel(request));
        payment.setFraudScore(0);

        // Save payment
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment created successfully: {}", paymentId);

        // Publish payment created event
        publishPaymentCreatedEvent(savedPayment);

        return PaymentResponse.fromPayment(savedPayment);
    }

    /**
     * Get payment by ID.
     */
    @Cacheable(value = "payments", key = "#paymentId")
    public PaymentResponse getPaymentById(UUID paymentId) {
        logger.debug("Fetching payment by ID: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));
        
        return PaymentResponse.fromPayment(payment);
    }

    /**
     * Get payment by payment ID.
     */
    @Cacheable(value = "payments", key = "#paymentId")
    public PaymentResponse getPaymentByPaymentId(String paymentId) {
        logger.debug("Fetching payment by payment ID: {}", paymentId);
        
        Payment payment = paymentRepository.findByPaymentId(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found with payment ID: " + paymentId));
        
        return PaymentResponse.fromPayment(payment);
    }

    /**
     * Get all payments for a user.
     */
    public List<PaymentResponse> getUserPayments(UUID userId) {
        logger.debug("Fetching payments for user: {}", userId);
        
        List<Payment> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
            .map(PaymentResponse::fromPayment)
            .collect(Collectors.toList());
    }

    /**
     * Get payments with pagination.
     */
    public Page<PaymentResponse> getPayments(Pageable pageable) {
        logger.debug("Fetching payments with pagination");
        
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(PaymentResponse::fromPayment);
    }

    /**
     * Process payment.
     */
    @CacheEvict(value = "payments", key = "#paymentId")
    public PaymentResponse processPayment(UUID paymentId) {
        logger.info("Processing payment: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));
        
        if (!payment.isPending()) {
            throw new PaymentProcessingException("Payment is not in pending status: " + payment.getStatus());
        }
        
        try {
            // Mark as processing
            payment.markAsProcessing();
            
            // Perform compliance check
            if (payment.getComplianceStatus() == Payment.ComplianceStatus.PENDING) {
                performComplianceCheck(payment);
            }
            
            // Perform fraud check
            if (payment.getFraudScore() == 0) {
                performFraudCheck(payment);
            }
            
            // Process payment based on type
            processPaymentByType(payment);
            
            // Mark as completed
            payment.markAsCompleted();
            payment.setSettlementDate(LocalDateTime.now());
            
            Payment updatedPayment = paymentRepository.save(payment);
            
            // Publish payment completed event
            publishPaymentCompletedEvent(updatedPayment);
            
            logger.info("Payment processed successfully: {}", paymentId);
            return PaymentResponse.fromPayment(updatedPayment);
            
        } catch (Exception e) {
            logger.error("Error processing payment: {}", e.getMessage(), e);
            payment.markAsFailed("Processing failed: " + e.getMessage(), "PROCESSING_ERROR");
            paymentRepository.save(payment);
            throw new PaymentProcessingException("Failed to process payment: " + e.getMessage());
        }
    }

    /**
     * Cancel payment.
     */
    @CacheEvict(value = "payments", key = "#paymentId")
    public PaymentResponse cancelPayment(UUID paymentId) {
        logger.info("Cancelling payment: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));
        
        if (payment.isCompleted()) {
            throw new PaymentProcessingException("Cannot cancel completed payment");
        }
        
        payment.markAsCancelled();
        Payment updatedPayment = paymentRepository.save(payment);
        
        // Publish payment cancelled event
        publishPaymentCancelledEvent(updatedPayment);
        
        logger.info("Payment cancelled successfully: {}", paymentId);
        return PaymentResponse.fromPayment(updatedPayment);
    }

    /**
     * Retry failed payment.
     */
    @CacheEvict(value = "payments", key = "#paymentId")
    public PaymentResponse retryPayment(UUID paymentId) {
        logger.info("Retrying payment: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));
        
        if (!payment.canRetry()) {
            throw new PaymentProcessingException("Payment cannot be retried");
        }
        
        payment.incrementRetryCount();
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setNextRetryDate(LocalDateTime.now().plusMinutes(5));
        
        Payment updatedPayment = paymentRepository.save(payment);
        
        logger.info("Payment retry scheduled: {}", paymentId);
        return PaymentResponse.fromPayment(updatedPayment);
    }

    /**
     * Update payment status.
     */
    @CacheEvict(value = "payments", key = "#paymentId")
    public PaymentResponse updatePaymentStatus(UUID paymentId, Payment.PaymentStatus newStatus) {
        logger.info("Updating payment status: {} -> {}", paymentId, newStatus);
        
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));
        
        payment.setStatus(newStatus);
        
        if (newStatus == Payment.PaymentStatus.COMPLETED) {
            payment.setCompletionDate(LocalDateTime.now());
        } else if (newStatus == Payment.PaymentStatus.FAILED) {
            payment.setFailureDate(LocalDateTime.now());
        }
        
        Payment updatedPayment = paymentRepository.save(payment);
        
        // Publish payment status changed event
        publishPaymentStatusChangedEvent(updatedPayment);
        
        return PaymentResponse.fromPayment(updatedPayment);
    }

    /**
     * Process scheduled payments.
     */
    @Scheduled(fixedRate = 60000) // Every minute
    public void processScheduledPayments() {
        logger.debug("Processing scheduled payments");
        
        List<Payment> scheduledPayments = paymentRepository.findScheduledPayments(LocalDateTime.now());
        
        for (Payment payment : scheduledPayments) {
            try {
                processPayment(payment.getId());
            } catch (Exception e) {
                logger.error("Error processing scheduled payment: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Retry failed payments.
     */
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void retryFailedPayments() {
        logger.debug("Retrying failed payments");
        
        List<Payment> failedPayments = paymentRepository.findFailedPaymentsForRetry(LocalDateTime.now());
        
        for (Payment payment : failedPayments) {
            try {
                retryPayment(payment.getId());
            } catch (Exception e) {
                logger.error("Error retrying failed payment: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Process batch payments.
     */
    public void processBatchPayments(String batchId) {
        logger.info("Processing batch payments: {}", batchId);
        
        List<Payment> batchPayments = paymentRepository.findByBatchId(batchId);
        
        for (Payment payment : batchPayments) {
            try {
                processPayment(payment.getId());
            } catch (Exception e) {
                logger.error("Error processing batch payment: {}", e.getMessage(), e);
            }
        }
    }

    // Private helper methods
    private Payment.RiskLevel assessRiskLevel(PaymentCreateRequest request) {
        // Simple risk assessment logic
        if (request.getIsUrgent() != null && request.getIsUrgent()) {
            return Payment.RiskLevel.HIGH;
        }
        if (request.getPaymentType() == Payment.PaymentType.INTERNATIONAL_TRANSFER ||
            request.getPaymentType() == Payment.PaymentType.SWIFT_TRANSFER) {
            return Payment.RiskLevel.MEDIUM;
        }
        return Payment.RiskLevel.LOW;
    }

    private void performComplianceCheck(Payment payment) {
        // TODO: Implement compliance check logic
        logger.debug("Performing compliance check for payment: {}", payment.getPaymentId());
        payment.setComplianceStatus(Payment.ComplianceStatus.APPROVED);
    }

    private void performFraudCheck(Payment payment) {
        // TODO: Implement fraud check logic
        logger.debug("Performing fraud check for payment: {}", payment.getPaymentId());
        payment.setFraudScore(10); // Low risk score
    }

    private void processPaymentByType(Payment payment) {
        switch (payment.getPaymentType()) {
            case DOMESTIC_TRANSFER:
                processDomesticTransfer(payment);
                break;
            case INTERNATIONAL_TRANSFER:
                processInternationalTransfer(payment);
                break;
            case SEPA_TRANSFER:
                processSepaTransfer(payment);
                break;
            case SWIFT_TRANSFER:
                processSwiftTransfer(payment);
                break;
            case CARD_PAYMENT:
                processCardPayment(payment);
                break;
            case CRYPTO_PAYMENT:
                processCryptoPayment(payment);
                break;
            default:
                processGenericPayment(payment);
                break;
        }
    }

    private void processDomesticTransfer(Payment payment) {
        // TODO: Implement domestic transfer processing
        logger.debug("Processing domestic transfer: {}", payment.getPaymentId());
    }

    private void processInternationalTransfer(Payment payment) {
        // TODO: Implement international transfer processing
        logger.debug("Processing international transfer: {}", payment.getPaymentId());
    }

    private void processSepaTransfer(Payment payment) {
        // TODO: Implement SEPA transfer processing
        logger.debug("Processing SEPA transfer: {}", payment.getPaymentId());
    }

    private void processSwiftTransfer(Payment payment) {
        // TODO: Implement SWIFT transfer processing
        logger.debug("Processing SWIFT transfer: {}", payment.getPaymentId());
    }

    private void processCardPayment(Payment payment) {
        // TODO: Implement card payment processing
        logger.debug("Processing card payment: {}", payment.getPaymentId());
    }

    private void processCryptoPayment(Payment payment) {
        // TODO: Implement crypto payment processing
        logger.debug("Processing crypto payment: {}", payment.getPaymentId());
    }

    private void processGenericPayment(Payment payment) {
        // TODO: Implement generic payment processing
        logger.debug("Processing generic payment: {}", payment.getPaymentId());
    }

    // Event publishing methods
    private void publishPaymentCreatedEvent(Payment payment) {
        // TODO: Implement Kafka event publishing
        logger.debug("Payment created event would be published for payment: {}", payment.getPaymentId());
    }

    private void publishPaymentCompletedEvent(Payment payment) {
        // TODO: Implement Kafka event publishing
        logger.debug("Payment completed event would be published for payment: {}", payment.getPaymentId());
    }

    private void publishPaymentCancelledEvent(Payment payment) {
        // TODO: Implement Kafka event publishing
        logger.debug("Payment cancelled event would be published for payment: {}", payment.getPaymentId());
    }

    private void publishPaymentStatusChangedEvent(Payment payment) {
        // TODO: Implement Kafka event publishing
        logger.debug("Payment status changed event would be published for payment: {}", payment.getPaymentId());
    }
}
