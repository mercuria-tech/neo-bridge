package com.neobridge.account.controller;

import com.neobridge.account.dto.AccountCreateRequest;
import com.neobridge.account.dto.AccountResponse;
import com.neobridge.account.entity.Account;
import com.neobridge.account.service.AccountService;
import com.neobridge.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for account management in the NeoBridge platform.
 * Provides endpoints for account creation, management, and operations.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    /**
     * Create a new account.
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        logger.info("Creating account for user: {}", request.getUserId());
        
        try {
            AccountResponse account = accountService.createAccount(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(account, "Account created successfully"));
        } catch (Exception e) {
            logger.error("Error creating account: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to create account: " + e.getMessage()));
        }
    }

    /**
     * Get account by ID.
     */
    @GetMapping("/{accountId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountById(@PathVariable UUID accountId) {
        logger.debug("Fetching account by ID: {}", accountId);
        
        try {
            AccountResponse account = accountService.getAccountById(accountId);
            return ResponseEntity.ok(ApiResponse.success(account, "Account retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error fetching account: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Account not found: " + e.getMessage()));
        }
    }

    /**
     * Get account by account number.
     */
    @GetMapping("/number/{accountNumber}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountByNumber(@PathVariable String accountNumber) {
        logger.debug("Fetching account by number: {}", accountNumber);
        
        try {
            AccountResponse account = accountService.getAccountByNumber(accountNumber);
            return ResponseEntity.ok(ApiResponse.success(account, "Account retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error fetching account: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Account not found: " + e.getMessage()));
        }
    }

    /**
     * Get all accounts for a user.
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getUserAccounts(@PathVariable UUID userId) {
        logger.debug("Fetching accounts for user: {}", userId);
        
        try {
            List<AccountResponse> accounts = accountService.getUserAccounts(userId);
            return ResponseEntity.ok(ApiResponse.success(accounts, "User accounts retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error fetching user accounts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch user accounts: " + e.getMessage()));
        }
    }

    /**
     * Get all accounts with pagination.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AccountResponse>>> getAccounts(Pageable pageable) {
        logger.debug("Fetching accounts with pagination");
        
        try {
            Page<AccountResponse> accounts = accountService.getAccounts(pageable);
            return ResponseEntity.ok(ApiResponse.success(accounts, "Accounts retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error fetching accounts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch accounts: " + e.getMessage()));
        }
    }

    /**
     * Update account status.
     */
    @PutMapping("/{accountId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> updateAccountStatus(
            @PathVariable UUID accountId,
            @RequestParam Account.AccountStatus status) {
        logger.info("Updating account status: {} -> {}", accountId, status);
        
        try {
            AccountResponse account = accountService.updateAccountStatus(accountId, status);
            return ResponseEntity.ok(ApiResponse.success(account, "Account status updated successfully"));
        } catch (Exception e) {
            logger.error("Error updating account status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to update account status: " + e.getMessage()));
        }
    }

    /**
     * Update account limits.
     */
    @PutMapping("/{accountId}/limits")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> updateAccountLimits(
            @PathVariable UUID accountId,
            @RequestParam(required = false) BigDecimal dailyLimit,
            @RequestParam(required = false) BigDecimal monthlyLimit) {
        logger.info("Updating account limits for account: {}", accountId);
        
        try {
            AccountResponse account = accountService.updateAccountLimits(accountId, dailyLimit, monthlyLimit);
            return ResponseEntity.ok(ApiResponse.success(account, "Account limits updated successfully"));
        } catch (Exception e) {
            logger.error("Error updating account limits: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to update account limits: " + e.getMessage()));
        }
    }

    /**
     * Process account debit (withdrawal).
     */
    @PostMapping("/{accountId}/debit")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> debitAccount(
            @PathVariable UUID accountId,
            @RequestParam BigDecimal amount,
            @RequestParam String description,
            @RequestParam String reference) {
        logger.info("Processing debit for account: {}, amount: {}", accountId, amount);
        
        try {
            accountService.debitAccount(accountId, amount, description, reference);
            return ResponseEntity.ok(ApiResponse.success(null, "Account debited successfully"));
        } catch (Exception e) {
            logger.error("Error processing debit: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to process debit: " + e.getMessage()));
        }
    }

    /**
     * Process account credit (deposit).
     */
    @PostMapping("/{accountId}/credit")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> creditAccount(
            @PathVariable UUID accountId,
            @RequestParam BigDecimal amount,
            @RequestParam String description,
            @RequestParam String reference) {
        logger.info("Processing credit for account: {}, amount: {}", accountId, amount);
        
        try {
            accountService.creditAccount(accountId, amount, description, reference);
            return ResponseEntity.ok(ApiResponse.success(null, "Account credited successfully"));
        } catch (Exception e) {
            logger.error("Error processing credit: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to process credit: " + e.getMessage()));
        }
    }

    /**
     * Reserve amount in account.
     */
    @PostMapping("/{accountId}/reserve")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> reserveAmount(
            @PathVariable UUID accountId,
            @RequestParam BigDecimal amount) {
        logger.debug("Reserving amount in account: {}, amount: {}", accountId, amount);
        
        try {
            accountService.reserveAmount(accountId, amount);
            return ResponseEntity.ok(ApiResponse.success(null, "Amount reserved successfully"));
        } catch (Exception e) {
            logger.error("Error reserving amount: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to reserve amount: " + e.getMessage()));
        }
    }

    /**
     * Release reserved amount in account.
     */
    @PostMapping("/{accountId}/release-reservation")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> releaseReservation(
            @PathVariable UUID accountId,
            @RequestParam BigDecimal amount) {
        logger.debug("Releasing reservation in account: {}, amount: {}", accountId, amount);
        
        try {
            accountService.releaseReservation(accountId, amount);
            return ResponseEntity.ok(ApiResponse.success(null, "Reservation released successfully"));
        } catch (Exception e) {
            logger.error("Error releasing reservation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to release reservation: " + e.getMessage()));
        }
    }

    /**
     * Calculate interest for account.
     */
    @PostMapping("/{accountId}/calculate-interest")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> calculateInterest(@PathVariable UUID accountId) {
        logger.info("Calculating interest for account: {}", accountId);
        
        try {
            accountService.calculateInterest(accountId);
            return ResponseEntity.ok(ApiResponse.success(null, "Interest calculated successfully"));
        } catch (Exception e) {
            logger.error("Error calculating interest: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to calculate interest: " + e.getMessage()));
        }
    }

    /**
     * Close account.
     */
    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> closeAccount(@PathVariable UUID accountId) {
        logger.info("Closing account: {}", accountId);
        
        try {
            accountService.closeAccount(accountId);
            return ResponseEntity.ok(ApiResponse.success(null, "Account closed successfully"));
        } catch (Exception e) {
            logger.error("Error closing account: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to close account: " + e.getMessage()));
        }
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Account Service is running", "Service healthy"));
    }
}
