package com.neobridge.crypto.entity;

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
 * CryptoWallet entity representing a cryptocurrency wallet in the NeoBridge platform.
 * Supports multiple cryptocurrencies and blockchain networks.
 */
@Entity
@Table(name = "crypto_wallets", indexes = {
    @Index(name = "idx_crypto_wallets_user_id", columnList = "user_id"),
    @Index(name = "idx_crypto_wallets_wallet_address", columnList = "wallet_address"),
    @Index(name = "idx_crypto_wallets_cryptocurrency", columnList = "cryptocurrency"),
    @Index(name = "idx_crypto_wallets_network", columnList = "network"),
    @Index(name = "idx_crypto_wallets_status", columnList = "status")
})
public class CryptoWallet extends BaseEntity {

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "wallet_name", nullable = false)
    private String walletName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "wallet_address", unique = true, nullable = false)
    private String walletAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "cryptocurrency", nullable = false)
    private Cryptocurrency cryptocurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "network", nullable = false)
    private BlockchainNetwork network;

    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_type", nullable = false)
    private WalletType walletType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WalletStatus status = WalletStatus.ACTIVE;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "balance", nullable = false, precision = 30, scale = 18)
    private BigDecimal balance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "available_balance", nullable = false, precision = 30, scale = 18)
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "locked_balance", nullable = false, precision = 30, scale = 18)
    private BigDecimal lockedBalance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "staked_balance", nullable = false, precision = 30, scale = 18)
    private BigDecimal stakedBalance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "yield_balance", nullable = false, precision = 30, scale = 18)
    private BigDecimal yieldBalance = BigDecimal.ZERO;

    @Column(name = "fiat_value_usd", precision = 19, scale = 4)
    private BigDecimal fiatValueUsd = BigDecimal.ZERO;

    @Column(name = "fiat_value_eur", precision = 19, scale = 4)
    private BigDecimal fiatValueEur = BigDecimal.ZERO;

    @Column(name = "last_price_usd", precision = 19, scale = 4)
    private BigDecimal lastPriceUsd = BigDecimal.ZERO;

    @Column(name = "last_price_eur", precision = 19, scale = 4)
    private BigDecimal lastPriceEur = BigDecimal.ZERO;

    @Column(name = "last_price_update")
    private LocalDateTime lastPriceUpdate;

    @Column(name = "is_hot_wallet", nullable = false)
    private Boolean isHotWallet = true;

    @Column(name = "is_cold_storage", nullable = false)
    private Boolean isColdStorage = false;

    @Column(name = "is_multi_sig", nullable = false)
    private Boolean isMultiSig = false;

    @Column(name = "required_signatures")
    private Integer requiredSignatures = 1;

    @Column(name = "total_signatures")
    private Integer totalSignatures = 1;

    @Size(max = 1000)
    @Column(name = "public_key")
    private String publicKey;

    @Size(max = 1000)
    @Column(name = "encrypted_private_key")
    private String encryptedPrivateKey;

    @Size(max = 100)
    @Column(name = "seed_phrase_hash")
    private String seedPhraseHash;

    @Column(name = "derivation_path")
    private String derivationPath;

    @Column(name = "nonce")
    private Long nonce = 0L;

    @Column(name = "gas_limit")
    private Long gasLimit = 21000L;

    @Column(name = "gas_price")
    private BigDecimal gasPrice = BigDecimal.ZERO;

    @Column(name = "max_fee_per_gas")
    private BigDecimal maxFeePerGas = BigDecimal.ZERO;

    @Column(name = "max_priority_fee_per_gas")
    private BigDecimal maxPriorityFeePerGas = BigDecimal.ZERO;

    @Column(name = "last_transaction_date")
    private LocalDateTime lastTransactionDate;

    @Column(name = "transaction_count")
    private Long transactionCount = 0L;

    @Column(name = "total_fees_paid", precision = 30, scale = 18)
    private BigDecimal totalFeesPaid = BigDecimal.ZERO;

    @Column(name = "total_fees_currency")
    private String totalFeesCurrency;

    @Size(max = 1000)
    @Column(name = "metadata")
    private String metadata;

    @Column(name = "is_delegated", nullable = false)
    private Boolean isDelegated = false;

    @Column(name = "delegation_address")
    private String delegationAddress;

    @Column(name = "delegation_rewards", precision = 30, scale = 18)
    private BigDecimal delegationRewards = BigDecimal.ZERO;

    @Column(name = "last_reward_claim")
    private LocalDateTime lastRewardClaim;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CryptoTransaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CryptoOrder> orders = new ArrayList<>();

    // Enums
    public enum Cryptocurrency {
        BTC("Bitcoin"),
        ETH("Ethereum"),
        USDT("Tether"),
        USDC("USD Coin"),
        BNB("Binance Coin"),
        ADA("Cardano"),
        SOL("Solana"),
        DOT("Polkadot"),
        MATIC("Polygon"),
        LINK("Chainlink"),
        UNI("Uniswap"),
        AAVE("Aave"),
        COMP("Compound"),
        MKR("Maker"),
        YFI("Yearn.finance"),
        CRV("Curve"),
        SUSHI("SushiSwap"),
        SNX("Synthetix"),
        BAL("Balancer"),
        REN("Ren"),
        KNC("Kyber Network"),
        ZRX("0x Protocol"),
        BAND("Band Protocol"),
        ALPHA("Alpha Finance"),
        PERP("Perpetual Protocol"),
        DYDX("dYdX"),
        GMX("GMX"),
        ARB("Arbitrum"),
        OP("Optimism"),
        BASE("Base"),
        POLYGON_ZKEVM("Polygon zkEVM"),
        ZKSYNC("zkSync"),
        STARKNET("StarkNet"),
        CELO("Celo"),
        NEAR("NEAR Protocol"),
        FLOW("Flow"),
        ICP("Internet Computer"),
        FIL("Filecoin"),
        EOS("EOS"),
        TRX("TRON"),
        XRP("Ripple"),
        LTC("Litecoin"),
        BCH("Bitcoin Cash"),
        XLM("Stellar"),
        VET("VeChain"),
        THETA("Theta"),
        HBAR("Hedera"),
        ALGO("Algorand"),
        ATOM("Cosmos"),
        OSMO("Osmosis"),
        INJ("Injective"),
        SEI("Sei Network"),
        SUI("Sui"),
        APT("Aptos"),
        MOVE("Move"),
        AR("Arweave"),
        RNDR("Render Token"),
        ROSE("Oasis Network"),
        MINA("Mina Protocol"),
        CKB("Nervos Network"),
        IOTA("IOTA"),
        NANO("Nano"),
        XTZ("Tezos"),
        WAVES("Waves"),
        QTUM("Qtum"),
        NEO("Neo"),
        ONT("Ontology"),
        ZIL("Zilliqa"),
        ONE("Harmony"),
        ELROND("MultiversX"),
        KDA("Kadena"),
        KSM("Kusama"),
        GLMR("Moonbeam"),
        ACA("Acala"),
        KAR("Karura"),
        PHA("Phala Network"),
        MOVR("Moonriver"),
        DEV("Dev Protocol"),
        RMRK("RMRK"),
        ASTAR("Astar"),
        SHIDEN("Shiden"),
        KILT("KILT Protocol"),
        CRU("Crust Network"),
        PCX("ChainX"),
        DOCK("Dock"),
        POLK("Polkamarkets"),
        RING("Darwinia Network"),
        KTON("Darwinia Commitment Token"),
        PHA("Phala Network"),
        LIT("Litentry"),
        BNC("Bifrost"),
        VS("Versa"),
        KMA("Calamari Network"),
        PICA("Picasso"),
        TUR("Turing Network"),
        KINT("Kintsugi"),
        KSM("Kusama"),
        DOT("Polkadot"),
        WND("Westend"),
        ROC("Rococo"),
        KSM("Kusama"),
        DOT("Polkadot"),
        WND("Westend"),
        ROC("Rococo");

        private final String displayName;

        Cryptocurrency(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum BlockchainNetwork {
        BITCOIN("Bitcoin"),
        ETHEREUM("Ethereum"),
        BINANCE_SMART_CHAIN("Binance Smart Chain"),
        POLYGON("Polygon"),
        ARBITRUM("Arbitrum"),
        OPTIMISM("Optimism"),
        BASE("Base"),
        POLYGON_ZKEVM("Polygon zkEVM"),
        ZKSYNC("zkSync"),
        STARKNET("StarkNet"),
        CELO("Celo"),
        NEAR("NEAR Protocol"),
        FLOW("Flow"),
        INTERNET_COMPUTER("Internet Computer"),
        FILECOIN("Filecoin"),
        EOS("EOS"),
        TRON("TRON"),
        RIPPLE("Ripple"),
        LITECOIN("Litecoin"),
        BITCOIN_CASH("Bitcoin Cash"),
        STELLAR("Stellar"),
        VECHAIN("VeChain"),
        THETA("Theta"),
        HEDERA("Hedera"),
        ALGORAND("Algorand"),
        COSMOS("Cosmos"),
        OSMOSIS("Osmosis"),
        INJECTIVE("Injective"),
        SEI("Sei Network"),
        SUI("Sui"),
        APTOS("Aptos"),
        MOVE("Move"),
        ARWEAVE("Arweave"),
        RENDER("Render Token"),
        OASIS("Oasis Network"),
        MINA("Mina Protocol"),
        NERVOS("Nervos Network"),
        IOTA("IOTA"),
        NANO("Nano"),
        TEZOS("Tezos"),
        WAVES("Waves"),
        QTUM("Qtum"),
        NEO("Neo"),
        ONTOLOGY("Ontology"),
        ZILLIQA("Zilliqa"),
        HARMONY("Harmony"),
        MULTIVERSX("MultiversX"),
        KADENA("Kadena"),
        KUSAMA("Kusama"),
        MOONBEAM("Moonbeam"),
        ACALA("Acala"),
        KARURA("Karura"),
        PHALA("Phala Network"),
        MOONRIVER("Moonriver"),
        DEV("Dev Protocol"),
        RMRK("RMRK"),
        ASTAR("Astar"),
        SHIDEN("Shiden"),
        KILT("KILT Protocol"),
        CRUST("Crust Network"),
        CHAINX("ChainX"),
        DOCK("Dock"),
        POLKAMARKETS("Polkamarkets"),
        DARWINIA("Darwinia Network"),
        CALAMARI("Calamari Network"),
        PICASSO("Picasso"),
        TURING("Turing Network"),
        KINTSUGI("Kintsugi"),
        WESTEND("Westend"),
        ROCOCO("Rococo");

        private final String displayName;

        BlockchainNetwork(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum WalletType {
        HOT_WALLET("Hot Wallet"),
        COLD_STORAGE("Cold Storage"),
        MULTI_SIG("Multi-Signature"),
        HARDWARE_WALLET("Hardware Wallet"),
        PAPER_WALLET("Paper Wallet"),
        BRAIN_WALLET("Brain Wallet"),
        DETERMINISTIC("Deterministic"),
        NON_DETERMINISTIC("Non-Deterministic"),
        HD_WALLET("Hierarchical Deterministic"),
        WATCH_ONLY("Watch-Only"),
        EXCHANGE_WALLET("Exchange Wallet"),
        DEFI_WALLET("DeFi Wallet"),
        NFT_WALLET("NFT Wallet"),
        GAMING_WALLET("Gaming Wallet"),
        STAKING_WALLET("Staking Wallet"),
        YIELD_WALLET("Yield Wallet"),
        LIQUIDITY_WALLET("Liquidity Wallet"),
        GOVERNANCE_WALLET("Governance Wallet"),
        TREASURY_WALLET("Treasury Wallet"),
        OPERATIONS_WALLET("Operations Wallet");

        private final String displayName;

        WalletType(String displayName) {
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
        PENDING("Pending"),
        UNDER_REVIEW("Under Review"),
        COMPLIANCE_CHECK("Compliance Check"),
        FRAUD_CHECK("Fraud Check"),
        MAINTENANCE("Maintenance"),
        MIGRATING("Migrating"),
        ARCHIVED("Archived");

        private final String displayName;

        WalletStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public CryptoWallet() {}

    public CryptoWallet(UUID userId, String walletName, String walletAddress, 
                        Cryptocurrency cryptocurrency, BlockchainNetwork network, WalletType walletType) {
        this.userId = userId;
        this.walletName = walletName;
        this.walletAddress = walletAddress;
        this.cryptocurrency = cryptocurrency;
        this.network = network;
        this.walletType = walletType;
    }

    // Helper methods
    public boolean hasSufficientBalance(BigDecimal amount) {
        return availableBalance.compareTo(amount) >= 0;
    }

    public boolean hasSufficientBalanceForFee(BigDecimal amount, BigDecimal fee) {
        return availableBalance.compareTo(amount.add(fee)) >= 0;
    }

    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        this.availableBalance = this.availableBalance.subtract(amount);
        updateTransactionCount();
    }

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.availableBalance = this.availableBalance.add(amount);
        updateTransactionCount();
    }

    public void lock(BigDecimal amount) {
        this.availableBalance = this.availableBalance.subtract(amount);
        this.lockedBalance = this.lockedBalance.add(amount);
    }

    public void unlock(BigDecimal amount) {
        this.lockedBalance = this.lockedBalance.subtract(amount);
        this.availableBalance = this.availableBalance.add(amount);
    }

    public void stake(BigDecimal amount) {
        this.availableBalance = this.availableBalance.subtract(amount);
        this.stakedBalance = this.stakedBalance.add(amount);
    }

    public void unstake(BigDecimal amount) {
        this.stakedBalance = this.stakedBalance.subtract(amount);
        this.availableBalance = this.availableBalance.add(amount);
    }

    public void addYield(BigDecimal amount) {
        this.yieldBalance = this.yieldBalance.add(amount);
    }

    public void claimYield() {
        this.availableBalance = this.availableBalance.add(this.yieldBalance);
        this.yieldBalance = BigDecimal.ZERO;
        this.lastRewardClaim = LocalDateTime.now();
    }

    public void addDelegationReward(BigDecimal amount) {
        this.delegationRewards = this.delegationRewards.add(amount);
    }

    public void claimDelegationRewards() {
        this.availableBalance = this.availableBalance.add(this.delegationRewards);
        this.delegationRewards = BigDecimal.ZERO;
        this.lastRewardClaim = LocalDateTime.now();
    }

    private void updateTransactionCount() {
        this.transactionCount++;
        this.lastTransactionDate = LocalDateTime.now();
    }

    public BigDecimal getTotalBalance() {
        return balance.add(yieldBalance).add(delegationRewards);
    }

    public BigDecimal getEffectiveBalance() {
        return availableBalance.add(yieldBalance).add(delegationRewards);
    }

    public boolean isEthereumBased() {
        return network == BlockchainNetwork.ETHEREUM ||
               network == BlockchainNetwork.POLYGON ||
               network == BlockchainNetwork.ARBITRUM ||
               network == BlockchainNetwork.OPTIMISM ||
               network == BlockchainNetwork.BASE ||
               network == BlockchainNetwork.POLYGON_ZKEVM ||
               network == BlockchainNetwork.ZKSYNC ||
               network == BlockchainNetwork.STARKNET;
    }

    public boolean isBitcoinBased() {
        return network == BlockchainNetwork.BITCOIN ||
               network == BlockchainNetwork.LITECOIN ||
               network == BlockchainNetwork.BITCOIN_CASH;
    }

    public boolean isPolkadotBased() {
        return network == BlockchainNetwork.POLKADOT ||
               network == BlockchainNetwork.KUSAMA ||
               network == BlockchainNetwork.MOONBEAM ||
               network == BlockchainNetwork.ACALA ||
               network == BlockchainNetwork.ASTAR;
    }

    public boolean isCosmosBased() {
        return network == BlockchainNetwork.COSMOS ||
               network == BlockchainNetwork.OSMOSIS ||
               network == BlockchainNetwork.INJECTIVE ||
               network == BlockchainNetwork.SEI;
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

    public List<CryptoTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<CryptoTransaction> transactions) {
        this.transactions = transactions;
    }

    public List<CryptoOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<CryptoOrder> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "CryptoWallet{" +
                "id=" + getId() +
                ", userId=" + userId +
                ", walletName='" + walletName + '\'' +
                ", walletAddress='" + walletAddress + '\'' +
                ", cryptocurrency=" + cryptocurrency +
                ", network=" + network +
                ", walletType=" + walletType +
                ", status=" + status +
                ", balance=" + balance +
                ", availableBalance=" + availableBalance +
                '}';
    }
}
