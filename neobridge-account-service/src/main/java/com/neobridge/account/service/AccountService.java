package com.neobridge.account.service;

import com.neobridge.account.dto.AccountCreateRequest;
import com.neobridge.account.dto.AccountResponse;
import com.neobridge.account.entity.Account;
import com.neobridge.account.entity.Transaction;
import com.neobridge.account.exception.AccountNotFoundException;
import com.neobridge.account.exception.InsufficientBalanceException;
import com.neobridge.account.exception.InvalidAccountOperationException;
import com.neobridge.account.repository.AccountRepository;
import com.neobridge.account.repository.TransactionRepository;
import com.neobridge.account.util.AccountNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing accounts in the NeoBridge platform.
 * Handles account creation, balance management, and transaction processing.
 */
@Service
@Transactional
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Create a new account for a user.
     */
    public AccountResponse createAccount(AccountCreateRequest request) {
        logger.info("Creating account for user: {}", request.getUserId());

        // Generate unique account number
        String accountNumber = accountNumberGenerator.generateAccountNumber(request.getAccountType(), request.getCurrency());

        // Create account entity
        Account account = new Account(
            request.getUserId(),
            accountNumber,
            request.getAccountName(),
            request.getAccountType(),
            request.getCurrency()
        );

        // Set additional properties
        if (request.getDescription() != null) {
            account.setDescription(request.getDescription());
        }
        if (request.getDailyLimit() != null) {
            account.setDailyLimit(request.getDailyLimit());
        }
        if (request.getMonthlyLimit() != null) {
            account.setMonthlyLimit(request.getMonthlyLimit());
        }
        if (request.getIsJointAccount() != null) {
            account.setIsJointAccount(request.getIsJointAccount());
        }
        if (request.getJointAccountHolders() != null) {
            account.setJointAccountHolders(request.getJointAccountHolders());
        }

        // Set initial deposit if provided
        if (request.getInitialDeposit() != null && request.getInitialDeposit().compareTo(BigDecimal.ZERO) > 0) {
            account.credit(request.getInitialDeposit());
            
            // Create initial deposit transaction
            Transaction transaction = new Transaction(
                account.getId(),
                account.getUserId(),
                Transaction.TransactionType.DEPOSIT,
                request.getInitialDeposit(),
                account.getCurrency().name(),
                "Initial deposit"
            );
            transaction.setBalanceBefore(BigDecimal.ZERO);
            transaction.setBalanceAfter(account.getBalance());
            transaction.setReference("INIT-" + accountNumber);
            transaction.markAsCompleted();
            
            transactionRepository.save(transaction);
        }

        // Save account
        Account savedAccount = accountRepository.save(account);
        logger.info("Account created successfully: {}", accountNumber);

        // Publish account created event
        publishAccountCreatedEvent(savedAccount);

        return AccountResponse.fromAccount(savedAccount);
    }

    /**
     * Get account by ID.
     */
    @Cacheable(value = "accounts", key = "#accountId")
    public AccountResponse getAccountById(UUID accountId) {
        logger.debug("Fetching account by ID: {}", accountId);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        return AccountResponse.fromAccount(account);
    }

    /**
     * Get account by account number.
     */
    @Cacheable(value = "accounts", key = "#accountNumber")
    public AccountResponse getAccountByNumber(String accountNumber) {
        logger.debug("Fetching account by number: {}", accountNumber);
        
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        
        return AccountResponse.fromAccount(account);
    }

    /**
     * Get all accounts for a user.
     */
    public List<AccountResponse> getUserAccounts(UUID userId) {
        logger.debug("Fetching accounts for user: {}", userId);
        
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.stream()
            .map(AccountResponse::fromAccount)
            .collect(Collectors.toList());
    }

    /**
     * Get accounts with pagination.
     */
    public Page<AccountResponse> getAccounts(Pageable pageable) {
        logger.debug("Fetching accounts with pagination");
        
        Page<Account> accounts = accountRepository.findAll(pageable);
        return accounts.map(AccountResponse::fromAccount);
    }

    /**
     * Update account status.
     */
    @CacheEvict(value = "accounts", key = "#accountId")
    public AccountResponse updateAccountStatus(UUID accountId, Account.AccountStatus newStatus) {
        logger.info("Updating account status: {} -> {}", accountId, newStatus);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        // Validate status transition
        validateStatusTransition(account.getStatus(), newStatus);
        
        account.setStatus(newStatus);
        Account updatedAccount = accountRepository.save(account);
        
        // Publish account status changed event
        publishAccountStatusChangedEvent(updatedAccount);
        
        return AccountResponse.fromAccount(updatedAccount);
    }

    /**
     * Update account limits.
     */
    @CacheEvict(value = "accounts", key = "#accountId")
    public AccountResponse updateAccountLimits(UUID accountId, BigDecimal dailyLimit, BigDecimal monthlyLimit) {
        logger.info("Updating account limits for account: {}", accountId);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        if (dailyLimit != null && dailyLimit.compareTo(BigDecimal.ZERO) > 0) {
            account.setDailyLimit(dailyLimit);
        }
        if (monthlyLimit != null && monthlyLimit.compareTo(BigDecimal.ZERO) > 0) {
            account.setMonthlyLimit(monthlyLimit);
        }
        
        Account updatedAccount = accountRepository.save(account);
        return AccountResponse.fromAccount(updatedAccount);
    }

    /**
     * Process account debit (withdrawal).
     */
    @CacheEvict(value = "accounts", key = "#accountId")
    public void debitAccount(UUID accountId, BigDecimal amount, String description, String reference) {
        logger.info("Processing debit for account: {}, amount: {}", accountId, amount);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        // Validate account status
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new InvalidAccountOperationException("Account is not active: " + account.getStatus());
        }
        
        // Validate sufficient balance
        if (!account.hasSufficientBalance(amount)) {
            throw new InsufficientBalanceException("Insufficient balance for debit: " + amount);
        }
        
        // Validate daily limit
        if (!account.hasSufficientDailyLimit(amount)) {
            throw new InvalidAccountOperationException("Daily limit exceeded for amount: " + amount);
        }
        
        // Validate monthly limit
        if (!account.hasSufficientMonthlyLimit(amount)) {
            throw new InvalidAccountOperationException("Monthly limit exceeded for amount: " + amount);
        }
        
        // Process debit
        BigDecimal balanceBefore = account.getBalance();
        account.debit(amount);
        BigDecimal balanceAfter = account.getBalance();
        
        // Save account
        accountRepository.save(account);
        
        // Create transaction record
        Transaction transaction = new Transaction(
            accountId,
            account.getUserId(),
            Transaction.TransactionType.WITHDRAWAL,
            amount,
            account.getCurrency().name(),
            description
        );
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setReference(reference);
        transaction.markAsCompleted();
        
        transactionRepository.save(transaction);
        
        // Publish account debited event
        publishAccountDebitedEvent(account, amount, reference);
        
        logger.info("Debit processed successfully for account: {}, new balance: {}", accountId, balanceAfter);
    }

    /**
     * Process account credit (deposit).
     */
    @CacheEvict(value = "accounts", key = "#accountId")
    public void creditAccount(UUID accountId, BigDecimal amount, String description, String reference) {
        logger.info("Processing credit for account: {}, amount: {}", accountId, amount);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        // Validate account status
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new InvalidAccountOperationException("Account is not active: " + account.getStatus());
        }
        
        // Process credit
        BigDecimal balanceBefore = account.getBalance();
        account.credit(amount);
        BigDecimal balanceAfter = account.getBalance();
        
        // Save account
        accountRepository.save(account);
        
        // Create transaction record
        Transaction transaction = new Transaction(
            accountId,
            account.getUserId(),
            Transaction.TransactionType.DEPOSIT,
            amount,
            account.getCurrency().name(),
            description
        );
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setReference(reference);
        transaction.markAsCompleted();
        
        transactionRepository.save(transaction);
        
        // Publish account credited event
        publishAccountCreditedEvent(account, amount, reference);
        
        logger.info("Credit processed successfully for account: {}, new balance: {}", accountId, balanceAfter);
    }

    /**
     * Reserve amount in account (for pending transactions).
     */
    @CacheEvict(value = "accounts", key = "#accountId")
    public void reserveAmount(UUID accountId, BigDecimal amount) {
        logger.debug("Reserving amount in account: {}, amount: {}", accountId, amount);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        if (!account.hasSufficientBalance(amount)) {
            throw new InsufficientBalanceException("Insufficient balance for reservation: " + amount);
        }
        
        account.reserve(amount);
        accountRepository.save(account);
    }

    /**
     * Release reserved amount in account.
     */
    @CacheEvict(value = "accounts", key = "#accountId")
    public void releaseReservation(UUID accountId, BigDecimal amount) {
        logger.debug("Releasing reservation in account: {}, amount: {}", accountId, amount);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        account.releaseReservation(amount);
        accountRepository.save(account);
    }

    /**
     * Calculate and apply interest to account.
     */
    @CacheEvict(value = "accounts", key = "#accountId")
    public void calculateInterest(UUID accountId) {
        logger.info("Calculating interest for account: {}", accountId);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        if (account.getInterestRate().compareTo(BigDecimal.ZERO) <= 0) {
            logger.debug("No interest rate set for account: {}", accountId);
            return;
        }
        
        // Calculate interest (simplified calculation)
        BigDecimal interestAmount = account.getBalance()
            .multiply(account.getInterestRate())
            .divide(BigDecimal.valueOf(365), 4, BigDecimal.ROUND_HALF_UP);
        
        if (interestAmount.compareTo(BigDecimal.ZERO) > 0) {
            account.credit(interestAmount);
            account.setLastInterestCalculation(LocalDateTime.now());
            accountRepository.save(account);
            
            // Create interest transaction
            Transaction transaction = new Transaction(
                accountId,
                account.getUserId(),
                Transaction.TransactionType.INTEREST,
                interestAmount,
                account.getCurrency().name(),
                "Interest credit"
            );
            transaction.setReference("INT-" + account.getAccountNumber() + "-" + LocalDateTime.now().toLocalDate());
            transaction.markAsCompleted();
            
            transactionRepository.save(transaction);
            
            logger.info("Interest applied to account: {}, amount: {}", accountId, interestAmount);
        }
    }

    /**
     * Reset daily transaction limits (called by scheduler).
     */
    @CacheEvict(value = "accounts", allEntries = true)
    public void resetDailyLimits() {
        logger.info("Resetting daily transaction limits for all accounts");
        
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            account.resetDailyLimits();
        }
        accountRepository.saveAll(accounts);
    }

    /**
     * Reset monthly transaction limits (called by scheduler).
     */
    @CacheEvict(value = "accounts", allEntries = true)
    public void resetMonthlyLimits() {
        logger.info("Resetting monthly transaction limits for all accounts");
        
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            account.resetMonthlyLimits();
        }
        accountRepository.saveAll(accounts);
    }

    /**
     * Close account.
     */
    @CacheEvict(value = "accounts", key = "#accountId")
    public void closeAccount(UUID accountId) {
        logger.info("Closing account: {}", accountId);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        if (account.getStatus() == Account.AccountStatus.CLOSED) {
            throw new InvalidAccountOperationException("Account is already closed");
        }
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new InvalidAccountOperationException("Cannot close account with non-zero balance: " + account.getBalance());
        }
        
        account.setStatus(Account.AccountStatus.CLOSED);
        accountRepository.save(account);
        
        // Publish account closed event
        publishAccountClosedEvent(account);
        
        logger.info("Account closed successfully: {}", accountId);
    }

    // Private helper methods
    private void validateStatusTransition(Account.AccountStatus currentStatus, Account.AccountStatus newStatus) {
        // Add validation logic for status transitions
        if (currentStatus == Account.AccountStatus.CLOSED && newStatus != Account.AccountStatus.CLOSED) {
            throw new InvalidAccountOperationException("Cannot change status of closed account");
        }
    }

    // Event publishing methods
    private void publishAccountCreatedEvent(Account account) {
        // TODO: Implement Kafka event publishing
        logger.debug("Account created event would be published for account: {}", account.getAccountNumber());
    }

    private void publishAccountStatusChangedEvent(Account account) {
        // TODO: Implement Kafka event publishing
        logger.debug("Account status changed event would be published for account: {}", account.getAccountNumber());
    }

    private void publishAccountDebitedEvent(Account account, BigDecimal amount, String reference) {
        // TODO: Implement Kafka event publishing
        logger.debug("Account debited event would be published for account: {}, amount: {}", account.getAccountNumber(), amount);
    }

    private void publishAccountCreditedEvent(Account account, BigDecimal amount, String reference) {
        // TODO: Implement Kafka event publishing
        logger.debug("Account credited event would be published for account: {}, amount: {}", account.getAccountNumber(), amount);
    }

    private void publishAccountClosedEvent(Account account) {
        // TODO: Implement Kafka event publishing
        logger.debug("Account closed event would be published for account: {}", account.getAccountNumber());
    }
}
