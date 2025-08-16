package com.neobridge.custody.entity;

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
 * CustodyWallet entity representing an institutional-grade custody wallet in the NeoBridge platform.
 * Supports multi-signature, cold storage, and advanced security features.
 */
@Entity
@Table(name = "custody_wallets", indexes = {
    @Index(name = "idx_custody_wallets_wallet_id", columnList = "wallet_id"),
    @Index(name = "idx_custody_wallets_owner_id", columnList = "owner_id"),
    @Index(name = "idx_custody_wallets_wallet_type", columnList = "wallet_type"),
    @Index(name = "idx_custody_wallets_status", columnList = "status"),
    @Index(name = "idx_custody_wallets_blockchain", columnList = "blockchain"),
    @Index(name = "idx_custody_wallets_created_at", columnList = "created_at")
})
public class CustodyWallet extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "wallet_id", unique = true, nullable = false)
    private String walletId;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "wallet_name", nullable = false)
    private String walletName;

    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "wallet_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WalletType walletType;

    @NotNull
    @Column(name = "blockchain", nullable = false)
    @Enumerated(EnumType.STRING)
    private Blockchain blockchain;

    @NotBlank
    @Size(max = 50)
    @Column(name = "network", nullable = false)
    private String network;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private WalletStatus status = WalletStatus.ACTIVE;

    @NotNull
    @Column(name = "security_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private SecurityLevel securityLevel = SecurityLevel.INSTITUTIONAL;

    @Column(name = "is_multi_sig")
    private Boolean isMultiSig = false;

    @Column(name = "required_signatures")
    private Integer requiredSignatures = 1;

    @Column(name = "total_signers")
    private Integer totalSigners = 1;

    @Column(name = "is_cold_storage")
    private Boolean isColdStorage = false;

    @Column(name = "is_hot_wallet")
    private Boolean isHotWallet = false;

    @Column(name = "is_hardware_wallet")
    private Boolean isHardwareWallet = false;

    @Column(name = "is_air_gapped")
    private Boolean isAirGapped = false;

    @Column(name = "is_offline")
    private Boolean isOffline = false;

    // Address Information
    @Column(name = "public_address")
    private String publicAddress;

    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "derivation_path")
    private String derivationPath;

    @Column(name = "account_index")
    private Integer accountIndex = 0;

    @Column(name = "change_index")
    private Integer changeIndex = 0;

    // Balance Information
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total_balance", nullable = false, precision = 19, scale = 8)
    private BigDecimal totalBalance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "available_balance", nullable = false, precision = 19, scale = 8)
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "reserved_balance", nullable = false, precision = 19, scale = 8)
    private BigDecimal reservedBalance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "pending_balance", nullable = false, precision = 19, scale = 8)
    private BigDecimal pendingBalance = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false)
    private String currency = "ETH";

    // Security Features
    @Column(name = "encryption_algorithm")
    @Enumerated(EnumType.STRING)
    private EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.AES_256_GCM;

    @Column(name = "key_derivation_function")
    @Enumerated(EnumType.STRING)
    private KeyDerivationFunction keyDerivationFunction = KeyDerivationFunction.PBKDF2;

    @Column(name = "key_iterations")
    private Integer keyIterations = 100000;

    @Column(name = "key_length")
    private Integer keyLength = 256;

    @Column(name = "salt")
    private String salt;

    @Column(name = "iv")
    private String iv;

    @Column(name = "checksum")
    private String checksum;

    // HSM Integration
    @Column(name = "hsm_provider")
    @Enumerated(EnumType.STRING)
    private HSMProvider hsmProvider = HSMProvider.AZURE_KEY_VAULT;

    @Column(name = "hsm_key_id")
    private String hsmKeyId;

    @Column(name = "hsm_key_version")
    private String hsmKeyVersion;

    @Column(name = "hsm_key_type")
    @Enumerated(EnumType.STRING)
    private HSMKeyType hsmKeyType = HSMKeyType.RSA_4096;

    // Compliance & Regulatory
    @Column(name = "is_regulated")
    private Boolean isRegulated = true;

    @Column(name = "regulatory_framework")
    private String regulatoryFramework = "EU_MiCA";

    @Column(name = "compliance_score", precision = 5, scale = 4)
    private BigDecimal complianceScore = BigDecimal.ONE;

    @Column(name = "risk_level")
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel = RiskLevel.LOW;

    @Column(name = "audit_trail_enabled")
    private Boolean auditTrailEnabled = true;

    @Column(name = "reporting_enabled")
    private Boolean reportingEnabled = true;

    // White-Label Features
    @Column(name = "is_white_label")
    private Boolean isWhiteLabel = false;

    @Column(name = "white_label_partner")
    private String whiteLabelPartner;

    @Column(name = "white_label_theme")
    private String whiteLabelTheme;

    @Column(name = "white_label_logo")
    private String whiteLabelLogo;

    @Column(name = "white_label_colors")
    private String whiteLabelColors;

    @Column(name = "white_label_domain")
    private String whiteLabelDomain;

    // Multi-Tenant Support
    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "tenant_tier")
    @Enumerated(EnumType.STRING)
    private TenantTier tenantTier = TenantTier.STANDARD;

    // Timestamps
    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @Column(name = "last_backup")
    private LocalDateTime lastBackup;

    @Column(name = "last_recovery")
    private LocalDateTime lastRecovery;

    @Column(name = "last_audit")
    private LocalDateTime lastAudit;

    @Column(name = "next_audit")
    private LocalDateTime nextAudit;

    // Relationships
    @OneToMany(mappedBy = "custodyWallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WalletSigner> signers = new ArrayList<>();

    @OneToMany(mappedBy = "custodyWallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WalletTransaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "custodyWallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WalletBackup> backups = new ArrayList<>();

    // Enums
    public enum WalletType {
        SINGLE_SIGNATURE("Single Signature"),
        MULTI_SIGNATURE("Multi-Signature"),
        THRESHOLD_SIGNATURE("Threshold Signature"),
        SHAMIR_SECRET_SHARING("Shamir Secret Sharing"),
        MPC_WALLET("Multi-Party Computation"),
        HARDWARE_WALLET("Hardware Wallet"),
        COLD_STORAGE("Cold Storage"),
        HOT_WALLET("Hot Wallet"),
        WARM_WALLET("Warm Wallet"),
        VAULT_WALLET("Vault Wallet"),
        TREASURY_WALLET("Treasury Wallet"),
        OPERATIONAL_WALLET("Operational Wallet"),
        RESERVE_WALLET("Reserve Wallet"),
        STAKING_WALLET("Staking Wallet"),
        GOVERNANCE_WALLET("Governance Wallet");

        private final String displayName;

        WalletType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Blockchain {
        ETHEREUM("Ethereum"),
        POLYGON("Polygon"),
        BINANCE_SMART_CHAIN("Binance Smart Chain"),
        SOLANA("Solana"),
        AVALANCHE("Avalanche"),
        ARBITRUM("Arbitrum"),
        OPTIMISM("Optimism"),
        POLYGON_ZKEVM("Polygon zkEVM"),
        BASE("Base"),
        LINEA("Linea"),
        SCROLL("Scroll"),
        MANTA("Manta"),
        ZKSYNC("zkSync"),
        STARKNET("Starknet"),
        BITCOIN("Bitcoin"),
        LITECOIN("Litecoin"),
        BITCOIN_CASH("Bitcoin Cash"),
        DOGECOIN("Dogecoin"),
        CARDANO("Cardano"),
        ALGORAND("Algorand"),
        HEDERA("Hedera"),
        NEAR("NEAR"),
        COSMOS("Cosmos"),
        POLKADOT("Polkadot"),
        CHAINLINK("Chainlink");

        private final String displayName;

        Blockchain(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum WalletStatus {
        ACTIVE("Active"),
        INACTIVE("Inactive"),
        SUSPENDED("Suspended"),
        LOCKED("Locked"),
        MAINTENANCE("Maintenance"),
        RECOVERY("Recovery"),
        ARCHIVED("Archived"),
        COMPROMISED("Compromised"),
        UNDER_REVIEW("Under Review"),
        COMPLIANCE_CHECK("Compliance Check"),
        AUDIT_IN_PROGRESS("Audit in Progress"),
        BACKUP_IN_PROGRESS("Backup in Progress"),
        RESTORATION_IN_PROGRESS("Restoration in Progress");

        private final String displayName;

        WalletStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum SecurityLevel {
        BASIC("Basic"),
        STANDARD("Standard"),
        PREMIUM("Premium"),
        INSTITUTIONAL("Institutional"),
        ENTERPRISE("Enterprise"),
        GOVERNMENT("Government"),
        MILITARY("Military"),
        NUCLEAR("Nuclear");

        private final String displayName;

        SecurityLevel(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum EncryptionAlgorithm {
        AES_128_CBC("AES-128-CBC"),
        AES_192_CBC("AES-192-CBC"),
        AES_256_CBC("AES-256-CBC"),
        AES_128_GCM("AES-128-GCM"),
        AES_192_GCM("AES-192-GCM"),
        AES_256_GCM("AES-256-GCM"),
        AES_128_CCM("AES-128-CCM"),
        AES_192_CCM("AES-192-CCM"),
        AES_256_CCM("AES-256-CCM"),
        CHACHA20_POLY1305("ChaCha20-Poly1305"),
        RSA_2048("RSA-2048"),
        RSA_3072("RSA-3072"),
        RSA_4096("RSA-4096"),
        ECDSA_P256("ECDSA-P256"),
        ECDSA_P384("ECDSA-P384"),
        ECDSA_P521("ECDSA-P521"),
        ED25519("Ed25519");

        private final String displayName;

        EncryptionAlgorithm(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum KeyDerivationFunction {
        PBKDF2("PBKDF2"),
        SCRYPT("Scrypt"),
        ARGON2("Argon2"),
        BCRYPT("Bcrypt"),
        HKDF("HKDF"),
        PBKDF2_HMAC_SHA256("PBKDF2-HMAC-SHA256"),
        PBKDF2_HMAC_SHA512("PBKDF2-HMAC-SHA512");

        private final String displayName;

        KeyDerivationFunction(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum HSMProvider {
        AZURE_KEY_VAULT("Azure Key Vault"),
        AWS_KMS("AWS Key Management Service"),
        GOOGLE_CLOUD_KMS("Google Cloud KMS"),
        HASHICORP_VAULT("HashiCorp Vault"),
        THALES_HSM("Thales HSM"),
        ATOS_HSM("Atos HSM"),
        UTIMACO_HSM("Utimaco HSM"),
        GEMALTO_HSM("Gemalto HSM"),
        YUBICO_HSM("Yubico HSM"),
        TREZOR_HSM("Trezor HSM"),
        LEDGER_HSM("Ledger HSM"),
        COLD_CARD_HSM("Cold Card HSM"),
        BITBOX_HSM("BitBox HSM"),
        ELLIPAL_HSM("Ellipal HSM"),
        SAFEPAL_HSM("SafePal HSM");

        private final String displayName;

        HSMProvider(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum HSMKeyType {
        RSA_2048("RSA-2048"),
        RSA_3072("RSA-3072"),
        RSA_4096("RSA-4096"),
        ECDSA_P256("ECDSA-P256"),
        ECDSA_P384("ECDSA-P384"),
        ECDSA_P521("ECDSA-P521"),
        ED25519("Ed25519"),
        X25519("X25519"),
        AES_128("AES-128"),
        AES_192("AES-192"),
        AES_256("AES-256"),
        CHACHA20("ChaCha20"),
        HMAC_SHA256("HMAC-SHA256"),
        HMAC_SHA512("HMAC-SHA512");

        private final String displayName;

        HSMKeyType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum RiskLevel {
        VERY_LOW("Very Low Risk", 1),
        LOW("Low Risk", 2),
        MODERATE("Moderate Risk", 3),
        HIGH("High Risk", 4),
        VERY_HIGH("Very High Risk", 5),
        EXTREME("Extreme Risk", 6);

        private final String displayName;
        private final int level;

        RiskLevel(String displayName, int level) {
            this.displayName = displayName;
            this.level = level;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getLevel() {
            return level;
        }
    }

    public enum TenantTier {
        BASIC("Basic"),
        STANDARD("Standard"),
        PREMIUM("Premium"),
        ENTERPRISE("Enterprise"),
        INSTITUTIONAL("Institutional"),
        GOVERNMENT("Government"),
        CUSTOM("Custom");

        private final String displayName;

        TenantTier(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public CustodyWallet() {}

    public CustodyWallet(String walletId, UUID ownerId, String walletName, 
                         WalletType walletType, Blockchain blockchain) {
        this.walletId = walletId;
        this.ownerId = ownerId;
        this.walletName = walletName;
        this.walletType = walletType;
        this.blockchain = blockchain;
    }

    // Helper methods
    public boolean isMultiSignature() {
        return isMultiSig && totalSigners > 1 && requiredSignatures > 1;
    }

    public boolean isColdStorage() {
        return isColdStorage && !isHotWallet;
    }

    public boolean isHardwareSecured() {
        return isHardwareWallet || isAirGapped || isOffline;
    }

    public boolean isWhiteLabelEnabled() {
        return isWhiteLabel && whiteLabelPartner != null;
    }

    public boolean isMultiTenant() {
        return tenantId != null && tenantName != null;
    }

    public BigDecimal getTotalBalance() {
        return availableBalance.add(reservedBalance).add(pendingBalance);
    }

    public boolean hasSufficientBalance(BigDecimal amount) {
        return availableBalance.compareTo(amount) >= 0;
    }

    public void reserveAmount(BigDecimal amount) {
        if (hasSufficientBalance(amount)) {
            this.availableBalance = this.availableBalance.subtract(amount);
            this.reservedBalance = this.reservedBalance.add(amount);
        }
    }

    public void releaseReservation(BigDecimal amount) {
        if (this.reservedBalance.compareTo(amount) >= 0) {
            this.reservedBalance = this.reservedBalance.subtract(amount);
            this.availableBalance = this.availableBalance.add(amount);
        }
    }

    public void addBalance(BigDecimal amount) {
        this.totalBalance = this.totalBalance.add(amount);
        this.availableBalance = this.availableBalance.add(amount);
        this.lastActivity = LocalDateTime.now();
    }

    public void subtractBalance(BigDecimal amount) {
        if (hasSufficientBalance(amount)) {
            this.totalBalance = this.totalBalance.subtract(amount);
            this.availableBalance = this.availableBalance.subtract(amount);
            this.lastActivity = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public SecurityLevel getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(SecurityLevel securityLevel) {
        this.securityLevel = securityLevel;
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

    public Integer getTotalSigners() {
        return totalSigners;
    }

    public void setTotalSigners(Integer totalSigners) {
        this.totalSigners = totalSigners;
    }

    public Boolean getIsColdStorage() {
        return isColdStorage;
    }

    public void setIsColdStorage(Boolean isColdStorage) {
        this.isColdStorage = isColdStorage;
    }

    public Boolean getIsHotWallet() {
        return isHotWallet;
    }

    public void setIsHotWallet(Boolean isHotWallet) {
        this.isHotWallet = isHotWallet;
    }

    public Boolean getIsHardwareWallet() {
        return isHardwareWallet;
    }

    public void setIsHardwareWallet(Boolean isHardwareWallet) {
        this.isHardwareWallet = isHardwareWallet;
    }

    public Boolean getIsAirGapped() {
        return isAirGapped;
    }

    public void setIsAirGapped(Boolean isAirGapped) {
        this.isAirGapped = isAirGapped;
    }

    public Boolean getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(Boolean isOffline) {
        this.isOffline = isOffline;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
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

    public Integer getAccountIndex() {
        return accountIndex;
    }

    public void setAccountIndex(Integer accountIndex) {
        this.accountIndex = accountIndex;
    }

    public Integer getChangeIndex() {
        return changeIndex;
    }

    public void setChangeIndex(Integer changeIndex) {
        this.changeIndex = changeIndex;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
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

    public BigDecimal getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(BigDecimal pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public KeyDerivationFunction getKeyDerivationFunction() {
        return keyDerivationFunction;
    }

    public void setKeyDerivationFunction(KeyDerivationFunction keyDerivationFunction) {
        this.keyDerivationFunction = keyDerivationFunction;
    }

    public Integer getKeyIterations() {
        return keyIterations;
    }

    public void setKeyIterations(Integer keyIterations) {
        this.keyIterations = keyIterations;
    }

    public Integer getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(Integer keyLength) {
        this.keyLength = keyLength;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public HSMProvider getHsmProvider() {
        return hsmProvider;
    }

    public void setHsmProvider(HSMProvider hsmProvider) {
        this.hsmProvider = hsmProvider;
    }

    public String getHsmKeyId() {
        return hsmKeyId;
    }

    public void setHsmKeyId(String hsmKeyId) {
        this.hsmKeyId = hsmKeyId;
    }

    public String getHsmKeyVersion() {
        return hsmKeyVersion;
    }

    public void setHsmKeyVersion(String hsmKeyVersion) {
        this.hsmKeyVersion = hsmKeyVersion;
    }

    public HSMKeyType getHsmKeyType() {
        return hsmKeyType;
    }

    public void setHsmKeyType(HSMKeyType hsmKeyType) {
        this.hsmKeyType = hsmKeyType;
    }

    public Boolean getIsRegulated() {
        return isRegulated;
    }

    public void setIsRegulated(Boolean isRegulated) {
        this.isRegulated = isRegulated;
    }

    public String getRegulatoryFramework() {
        return regulatoryFramework;
    }

    public void setRegulatoryFramework(String regulatoryFramework) {
        this.regulatoryFramework = regulatoryFramework;
    }

    public BigDecimal getComplianceScore() {
        return complianceScore;
    }

    public void setComplianceScore(BigDecimal complianceScore) {
        this.complianceScore = complianceScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Boolean getAuditTrailEnabled() {
        return auditTrailEnabled;
    }

    public void setAuditTrailEnabled(Boolean auditTrailEnabled) {
        this.auditTrailEnabled = auditTrailEnabled;
    }

    public Boolean getReportingEnabled() {
        return reportingEnabled;
    }

    public void setReportingEnabled(Boolean reportingEnabled) {
        this.reportingEnabled = reportingEnabled;
    }

    public Boolean getIsWhiteLabel() {
        return isWhiteLabel;
    }

    public void setIsWhiteLabel(Boolean isWhiteLabel) {
        this.isWhiteLabel = isWhiteLabel;
    }

    public String getWhiteLabelPartner() {
        return whiteLabelPartner;
    }

    public void setWhiteLabelPartner(String whiteLabelPartner) {
        this.whiteLabelPartner = whiteLabelPartner;
    }

    public String getWhiteLabelTheme() {
        return whiteLabelTheme;
    }

    public void setWhiteLabelTheme(String whiteLabelTheme) {
        this.whiteLabelTheme = whiteLabelTheme;
    }

    public String getWhiteLabelLogo() {
        return whiteLabelLogo;
    }

    public void setWhiteLabelLogo(String whiteLabelLogo) {
        this.whiteLabelLogo = whiteLabelLogo;
    }

    public String getWhiteLabelColors() {
        return whiteLabelColors;
    }

    public void setWhiteLabelColors(String whiteLabelColors) {
        this.whiteLabelColors = whiteLabelColors;
    }

    public String getWhiteLabelDomain() {
        return whiteLabelDomain;
    }

    public void setWhiteLabelDomain(String whiteLabelDomain) {
        this.whiteLabelDomain = whiteLabelDomain;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public TenantTier getTenantTier() {
        return tenantTier;
    }

    public void setTenantTier(TenantTier tenantTier) {
        this.tenantTier = tenantTier;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public LocalDateTime getLastBackup() {
        return lastBackup;
    }

    public void setLastBackup(LocalDateTime lastBackup) {
        this.lastBackup = lastBackup;
    }

    public LocalDateTime getLastRecovery() {
        return lastRecovery;
    }

    public void setLastRecovery(LocalDateTime lastRecovery) {
        this.lastRecovery = lastRecovery;
    }

    public LocalDateTime getLastAudit() {
        return lastAudit;
    }

    public void setLastAudit(LocalDateTime lastAudit) {
        this.lastAudit = lastAudit;
    }

    public LocalDateTime getNextAudit() {
        return nextAudit;
    }

    public void setNextAudit(LocalDateTime nextAudit) {
        this.nextAudit = nextAudit;
    }

    public List<WalletSigner> getSigners() {
        return signers;
    }

    public void setSigners(List<WalletSigner> signers) {
        this.signers = signers;
    }

    public List<WalletTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<WalletTransaction> transactions) {
        this.transactions = transactions;
    }

    public List<WalletBackup> getBackups() {
        return backups;
    }

    public void setBackups(List<WalletBackup> backups) {
        this.backups = backups;
    }

    @Override
    public String toString() {
        return "CustodyWallet{" +
                "id=" + getId() +
                ", walletId='" + walletId + '\'' +
                ", walletName='" + walletName + '\'' +
                ", walletType=" + walletType +
                ", blockchain=" + blockchain +
                ", status=" + status +
                ", securityLevel=" + securityLevel +
                '}';
    }
}
