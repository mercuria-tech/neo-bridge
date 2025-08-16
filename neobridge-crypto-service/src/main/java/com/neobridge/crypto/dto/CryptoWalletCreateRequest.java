package com.neobridge.crypto.dto;

import com.neobridge.crypto.entity.CryptoWallet.BlockchainNetwork;
import com.neobridge.crypto.entity.CryptoWallet.Cryptocurrency;
import com.neobridge.crypto.entity.CryptoWallet.WalletType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * DTO for creating a new crypto wallet in the NeoBridge platform.
 */
public class CryptoWalletCreateRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Wallet name is required")
    @Size(max = 100, message = "Wallet name must not exceed 100 characters")
    private String walletName;

    @NotNull(message = "Cryptocurrency is required")
    private Cryptocurrency cryptocurrency;

    @NotNull(message = "Blockchain network is required")
    private BlockchainNetwork network;

    @NotNull(message = "Wallet type is required")
    private WalletType walletType;

    @Size(max = 1000, message = "Public key must not exceed 1000 characters")
    private String publicKey;

    @Size(max = 1000, message = "Encrypted private key must not exceed 1000 characters")
    private String encryptedPrivateKey;

    @Size(max = 100, message = "Seed phrase hash must not exceed 100 characters")
    private String seedPhraseHash;

    @Size(max = 100, message = "Derivation path must not exceed 100 characters")
    private String derivationPath;

    private Boolean isHotWallet = true;

    private Boolean isColdStorage = false;

    private Boolean isMultiSig = false;

    private Integer requiredSignatures = 1;

    private Integer totalSignatures = 1;

    @Size(max = 1000, message = "Metadata must not exceed 1000 characters")
    private String metadata;

    // Constructors
    public CryptoWalletCreateRequest() {}

    public CryptoWalletCreateRequest(UUID userId, String walletName, Cryptocurrency cryptocurrency,
                                    BlockchainNetwork network, WalletType walletType) {
        this.userId = userId;
        this.walletName = walletName;
        this.cryptocurrency = cryptocurrency;
        this.network = network;
        this.walletType = walletType;
    }

    // Getters and Setters
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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public void setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    public String getSeedPhraseHash() {
        return seedPhraseHash;
    }

    public void setSeedPhraseHash(String seedPhraseHash) {
        this.seedPhraseHash = seedPhraseHash;
    }

    public String getDerivationPath() {
        return derivationPath;
    }

    public void setDerivationPath(String derivationPath) {
        this.derivationPath = derivationPath;
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

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "CryptoWalletCreateRequest{" +
                "userId=" + userId +
                ", walletName='" + walletName + '\'' +
                ", cryptocurrency=" + cryptocurrency +
                ", network=" + network +
                ", walletType=" + walletType +
                ", isHotWallet=" + isHotWallet +
                ", isColdStorage=" + isColdStorage +
                ", isMultiSig=" + isMultiSig +
                '}';
    }
}
