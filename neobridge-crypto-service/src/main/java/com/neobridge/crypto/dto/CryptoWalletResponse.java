package com.neobridge.crypto.dto;

import com.neobridge.crypto.entity.CryptoWallet;
import com.neobridge.crypto.entity.CryptoWallet.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for crypto wallet responses in the NeoBridge platform.
 */
public class CryptoWalletResponse {

    private UUID id;
    private UUID userId;
    private String walletName;
    private String walletAddress;
    private Cryptocurrency cryptocurrency;
    private BlockchainNetwork network;
    private WalletType walletType;
    private WalletStatus status;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private BigDecimal lockedBalance;
    private BigDecimal stakedBalance;
    private BigDecimal yieldBalance;
    private BigDecimal fiatValueUsd;
    private BigDecimal fiatValueEur;
    private BigDecimal lastPriceUsd;
    private BigDecimal lastPriceEur;
    private LocalDateTime lastPriceUpdate;
    private Boolean isHotWallet;
    private Boolean isColdStorage;
    private Boolean isMultiSig;
    private Integer requiredSignatures;
    private Integer totalSignatures;
    private String publicKey;
    private String derivationPath;
    private Long nonce;
    private Long gasLimit;
    private BigDecimal gasPrice;
    private BigDecimal maxFeePerGas;
    private BigDecimal maxPriorityFeePerGas;
    private LocalDateTime lastTransactionDate;
    private Long transactionCount;
    private BigDecimal totalFeesPaid;
    private String totalFeesCurrency;
    private String metadata;
    private Boolean isDelegated;
    private String delegationAddress;
    private BigDecimal delegationRewards;
    private LocalDateTime lastRewardClaim;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Static factory method
    public static CryptoWalletResponse fromCryptoWallet(CryptoWallet wallet) {
        CryptoWalletResponse response = new CryptoWalletResponse();
        response.setId(wallet.getId());
        response.setUserId(wallet.getUserId());
        response.setWalletName(wallet.getWalletName());
        response.setWalletAddress(wallet.getWalletAddress());
        response.setCryptocurrency(wallet.getCryptocurrency());
        response.setNetwork(wallet.getNetwork());
        response.setWalletType(wallet.getWalletType());
        response.setStatus(wallet.getStatus());
        response.setBalance(wallet.getBalance());
        response.setAvailableBalance(wallet.getAvailableBalance());
        response.setLockedBalance(wallet.getLockedBalance());
        response.setStakedBalance(wallet.getStakedBalance());
        response.setYieldBalance(wallet.getYieldBalance());
        response.setFiatValueUsd(wallet.getFiatValueUsd());
        response.setFiatValueEur(wallet.getFiatValueEur());
        response.setLastPriceUsd(wallet.getLastPriceUsd());
        response.setLastPriceEur(wallet.getLastPriceEur());
        response.setLastPriceUpdate(wallet.getLastPriceUpdate());
        response.setIsHotWallet(wallet.getIsHotWallet());
        response.setIsColdStorage(wallet.getIsColdStorage());
        response.setIsMultiSig(wallet.getIsMultiSig());
        response.setRequiredSignatures(wallet.getRequiredSignatures());
        response.setTotalSignatures(wallet.getTotalSignatures());
        response.setPublicKey(wallet.getPublicKey());
        response.setDerivationPath(wallet.getDerivationPath());
        response.setNonce(wallet.getNonce());
        response.setGasLimit(wallet.getGasLimit());
        response.setGasPrice(wallet.getGasPrice());
        response.setMaxFeePerGas(wallet.getMaxFeePerGas());
        response.setMaxPriorityFeePerGas(wallet.getMaxPriorityFeePerGas());
        response.setLastTransactionDate(wallet.getLastTransactionDate());
        response.setTransactionCount(wallet.getTransactionCount());
        response.setTotalFeesPaid(wallet.getTotalFeesPaid());
        response.setTotalFeesCurrency(wallet.getTotalFeesCurrency());
        response.setMetadata(wallet.getMetadata());
        response.setIsDelegated(wallet.getIsDelegated());
        response.setDelegationAddress(wallet.getDelegationAddress());
        response.setDelegationRewards(wallet.getDelegationRewards());
        response.setLastRewardClaim(wallet.getLastRewardClaim());
        response.setCreatedAt(wallet.getCreatedAt());
        response.setUpdatedAt(wallet.getUpdatedAt());
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

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public Cryptocurrency getCryptocurrency() {
        return cryptocurrency;
    }

    public void setCryptocurrency(Cryptocurrency cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public BlockchainNetwork getNetwork() {
        return network;
    }

    public void setNetwork(BlockchainNetwork network) {
        this.network = network;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public void setStatus(WalletStatus status) {
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

    public BigDecimal getLockedBalance() {
        return lockedBalance;
    }

    public void setLockedBalance(BigDecimal lockedBalance) {
        this.lockedBalance = lockedBalance;
    }

    public BigDecimal getStakedBalance() {
        return stakedBalance;
    }

    public void setStakedBalance(BigDecimal stakedBalance) {
        this.stakedBalance = stakedBalance;
    }

    public BigDecimal getYieldBalance() {
        return yieldBalance;
    }

    public void setYieldBalance(BigDecimal yieldBalance) {
        this.yieldBalance = yieldBalance;
    }

    public BigDecimal getFiatValueUsd() {
        return fiatValueUsd;
    }

    public void setFiatValueUsd(BigDecimal fiatValueUsd) {
        this.fiatValueUsd = fiatValueUsd;
    }

    public BigDecimal getFiatValueEur() {
        return fiatValueEur;
    }

    public void setFiatValueEur(BigDecimal fiatValueEur) {
        this.fiatValueEur = fiatValueEur;
    }

    public BigDecimal getLastPriceUsd() {
        return lastPriceUsd;
    }

    public void setLastPriceUsd(BigDecimal lastPriceUsd) {
        this.lastPriceUsd = lastPriceUsd;
    }

    public BigDecimal getLastPriceEur() {
        return lastPriceEur;
    }

    public void setLastPriceEur(BigDecimal lastPriceEur) {
        this.lastPriceEur = lastPriceEur;
    }

    public LocalDateTime getLastPriceUpdate() {
        return lastPriceUpdate;
    }

    public void setLastPriceUpdate(LocalDateTime lastPriceUpdate) {
        this.lastPriceUpdate = lastPriceUpdate;
    }

    public Boolean getIsHotWallet() {
        return isHotWallet;
    }

    public void setIsHotWallet(Boolean isHotWallet) {
        this.isHotWallet = isHotWallet;
    }

    public Boolean getIsColdStorage() {
        return isColdStorage;
    }

    public void setIsColdStorage(Boolean isColdStorage) {
        this.isColdStorage = isColdStorage;
    }

    public Boolean getIsMultiSig() {
        return isMultiSig;
    }

    public void setIsMultiSig(Boolean isMultiSig) {
        this.isMultiSig = isMultiSig;
    }

    public Integer getRequiredSignatures() {
        return requiredSignatures;
    }

    public void setRequiredSignatures(Integer requiredSignatures) {
        this.requiredSignatures = requiredSignatures;
    }

    public Integer getTotalSignatures() {
        return totalSignatures;
    }

    public void setTotalSignatures(Integer totalSignatures) {
        this.totalSignatures = totalSignatures;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getDerivationPath() {
        return derivationPath;
    }

    public void setDerivationPath(String derivationPath) {
        this.derivationPath = derivationPath;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public Long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigDecimal getMaxFeePerGas() {
        return maxFeePerGas;
    }

    public void setMaxFeePerGas(BigDecimal maxFeePerGas) {
        this.maxFeePerGas = maxFeePerGas;
    }

    public BigDecimal getMaxPriorityFeePerGas() {
        return maxPriorityFeePerGas;
    }

    public void setMaxPriorityFeePerGas(BigDecimal maxPriorityFeePerGas) {
        this.maxPriorityFeePerGas = maxPriorityFeePerGas;
    }

    public LocalDateTime getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(LocalDateTime lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public BigDecimal getTotalFeesPaid() {
        return totalFeesPaid;
    }

    public void setTotalFeesPaid(BigDecimal totalFeesPaid) {
        this.totalFeesPaid = totalFeesPaid;
    }

    public String getTotalFeesCurrency() {
        return totalFeesCurrency;
    }

    public void setTotalFeesCurrency(String totalFeesCurrency) {
        this.totalFeesCurrency = totalFeesCurrency;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Boolean getIsDelegated() {
        return isDelegated;
    }

    public void setIsDelegated(Boolean isDelegated) {
        this.isDelegated = isDelegated;
    }

    public String getDelegationAddress() {
        return delegationAddress;
    }

    public void setDelegationAddress(String delegationAddress) {
        this.delegationAddress = delegationAddress;
    }

    public BigDecimal getDelegationRewards() {
        return delegationRewards;
    }

    public void setDelegationRewards(BigDecimal delegationRewards) {
        this.delegationRewards = delegationRewards;
    }

    public LocalDateTime getLastRewardClaim() {
        return lastRewardClaim;
    }

    public void setLastRewardClaim(LocalDateTime lastRewardClaim) {
        this.lastRewardClaim = lastRewardClaim;
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
        return "CryptoWalletResponse{" +
                "id=" + id +
                ", userId=" + userId +
                ", walletName='" + walletName + '\'' +
                ", walletAddress='" + walletAddress + '\'' +
                ", cryptocurrency=" + cryptocurrency +
                ", network=" + network +
                ", walletType=" + walletType +
                ", status=" + status +
                ", balance=" + balance +
                ", availableBalance=" + availableBalance +
                ", fiatValueUsd=" + fiatValueUsd +
                '}';
    }
}
