package com.neobridge.nft.entity;

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
 * NFT entity representing a non-fungible token in the NeoBridge marketplace.
 * Supports multiple blockchain networks and comprehensive metadata.
 */
@Entity
@Table(name = "nfts", indexes = {
    @Index(name = "idx_nfts_token_id", columnList = "token_id"),
    @Index(name = "idx_nfts_contract_address", columnList = "contract_address"),
    @Index(name = "idx_nfts_owner_id", columnList = "owner_id"),
    @Index(name = "idx_nfts_creator_id", columnList = "creator_id"),
    @Index(name = "idx_nfts_blockchain", columnList = "blockchain"),
    @Index(name = "idx_nfts_status", columnList = "status"),
    @Index(name = "idx_nfts_category", columnList = "category"),
    @Index(name = "idx_nfts_created_at", columnList = "created_at")
})
public class NFT extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "token_id", unique = true, nullable = false)
    private String tokenId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "contract_address", nullable = false)
    private String contractAddress;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @NotNull
    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @NotBlank
    @Size(max = 200)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @NotBlank
    @Size(max = 50)
    @Column(name = "blockchain", nullable = false)
    @Enumerated(EnumType.STRING)
    private Blockchain blockchain;

    @NotBlank
    @Size(max = 50)
    @Column(name = "network", nullable = false)
    private String network;

    @NotBlank
    @Size(max = 50)
    @Column(name = "standard", nullable = false)
    @Enumerated(EnumType.STRING)
    private NFTStandard standard;

    @NotBlank
    @Size(max = 50)
    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Size(max = 50)
    @Column(name = "subcategory")
    private String subcategory;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private NFTStatus status = NFTStatus.MINTED;

    @Column(name = "is_listed")
    private Boolean isListed = false;

    @Column(name = "is_auction")
    private Boolean isAuction = false;

    @Column(name = "is_fractionalized")
    private Boolean isFractionalized = false;

    @Column(name = "total_supply")
    private Long totalSupply = 1L;

    @Column(name = "circulating_supply")
    private Long circulatingSupply = 1L;

    @Column(name = "fractionalized_supply")
    private Long fractionalizedSupply = 0L;

    @Column(name = "fractionalized_price", precision = 19, scale = 8)
    private BigDecimal fractionalizedPrice;

    @Column(name = "fractionalized_currency")
    private String fractionalizedCurrency = "ETH";

    // Metadata
    @Column(name = "metadata_uri")
    private String metadataUri;

    @Column(name = "image_uri")
    private String imageUri;

    @Column(name = "animation_uri")
    private String animationUri;

    @Column(name = "external_uri")
    private String externalUri;

    @Column(name = "attributes", columnDefinition = "TEXT")
    private String attributes;

    @Column(name = "properties", columnDefinition = "TEXT")
    private String properties;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    // Royalties
    @Column(name = "royalty_percentage", precision = 5, scale = 4)
    private BigDecimal royaltyPercentage = BigDecimal.ZERO;

    @Column(name = "royalty_recipient")
    private String royaltyRecipient;

    // Pricing
    @Column(name = "mint_price", precision = 19, scale = 8)
    private BigDecimal mintPrice;

    @Column(name = "mint_currency")
    private String mintCurrency = "ETH";

    @Column(name = "current_price", precision = 19, scale = 8)
    private BigDecimal currentPrice;

    @Column(name = "current_currency")
    private String currentCurrency = "ETH";

    @Column(name = "floor_price", precision = 19, scale = 8)
    private BigDecimal floorPrice;

    @Column(name = "highest_bid", precision = 19, scale = 8)
    private BigDecimal highestBid;

    @Column(name = "highest_bidder")
    private String highestBidder;

    // Statistics
    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "share_count")
    private Long shareCount = 0L;

    @Column(name = "trade_count")
    private Long tradeCount = 0L;

    @Column(name = "total_volume", precision = 19, scale = 8)
    private BigDecimal totalVolume = BigDecimal.ZERO;

    @Column(name = "average_price", precision = 19, scale = 8)
    private BigDecimal averagePrice;

    @Column(name = "price_change_24h", precision = 5, scale = 4)
    private BigDecimal priceChange24h;

    @Column(name = "price_change_7d", precision = 5, scale = 4)
    private BigDecimal priceChange7d;

    @Column(name = "price_change_30d", precision = 5, scale = 4)
    private BigDecimal priceChange30d;

    // Timestamps
    @Column(name = "mint_date")
    private LocalDateTime mintDate;

    @Column(name = "list_date")
    private LocalDateTime listDate;

    @Column(name = "last_trade_date")
    private LocalDateTime lastTradeDate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // Verification
    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "verification_date")
    private LocalDateTime verificationDate;

    @Column(name = "verified_by")
    private String verifiedBy;

    // Compliance
    @Column(name = "is_compliant")
    private Boolean isCompliant = true;

    @Column(name = "compliance_score", precision = 5, scale = 4)
    private BigDecimal complianceScore = BigDecimal.ONE;

    @Column(name = "risk_level")
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel = RiskLevel.LOW;

    // Storage
    @Column(name = "storage_provider")
    @Enumerated(EnumType.STRING)
    private StorageProvider storageProvider = StorageProvider.IPFS;

    @Column(name = "storage_hash")
    private String storageHash;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_dimensions")
    private String fileDimensions;

    // Relationships
    @OneToMany(mappedBy = "nft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NFTOffer> offers = new ArrayList<>();

    @OneToMany(mappedBy = "nft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NFTAuction> auctions = new ArrayList<>();

    @OneToMany(mappedBy = "nft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NFTTransaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "nft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NFTLike> likes = new ArrayList<>();

    // Enums
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
        IMMUTABLE("Immutable"),
        FLOW("Flow"),
        TEZOS("Tezos"),
        CARDANO("Cardano"),
        ALGORAND("Algorand"),
        HEDERA("Hedera"),
        NEAR("NEAR"),
        COSMOS("Cosmos"),
        POLKADOT("Polkadot"),
        CHAINLINK("Chainlink"),
        POLYGON_MUMBAI("Polygon Mumbai"),
        ETHEREUM_GOERLI("Ethereum Goerli"),
        ETHEREUM_SEPOLIA("Ethereum Sepolia"),
        BSC_TESTNET("BSC Testnet"),
        SOLANA_DEVNET("Solana Devnet");

        private final String displayName;

        Blockchain(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum NFTStandard {
        ERC_721("ERC-721"),
        ERC_1155("ERC-1155"),
        ERC_4907("ERC-4907"),
        ERC_6551("ERC-6551"),
        SPL_TOKEN("SPL Token"),
        BEP_721("BEP-721"),
        BEP_1155("BEP-1155"),
        CARDANO_NFT("Cardano NFT"),
        TEZOS_FA2("Tezos FA2"),
        FLOW_NFT("Flow NFT"),
        ALGORAND_ASA("Algorand ASA"),
        HEDERA_NFT("Hedera NFT"),
        NEAR_NFT("NEAR NFT"),
        COSMOS_NFT("Cosmos NFT"),
        POLKADOT_NFT("Polkadot NFT");

        private final String displayName;

        NFTStandard(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Category {
        ART("Art"),
        COLLECTIBLES("Collectibles"),
        GAMING("Gaming"),
        MUSIC("Music"),
        PHOTOGRAPHY("Photography"),
        SPORTS("Sports"),
        UTILITY("Utility"),
        VIRTUAL_WORLDS("Virtual Worlds"),
        TRADING_CARDS("Trading Cards"),
        DOMAIN_NAMES("Domain Names"),
        MEMES("Memes"),
        PUNKS("Punks"),
        APES("Apes"),
        DOODLES("Doodles"),
        BORED_APE("Bored Ape"),
        CRYPTOPUNKS("CryptoPunks"),
        AZUKI("Azuki"),
        DEEGEN("Degen"),
        MILADY("Milady"),
        PUDGYPENGUINS("Pudgy Penguins"),
        MOONBIRDS("Moonbirds"),
        DOODLES("Doodles"),
        WORLD_OF_WOMEN("World of Women"),
        COOL_CATS("Cool Cats"),
        BAYC("BAYC"),
        MAYC("MAYC"),
        OTHERDEED("Otherdeed"),
        SAND("The Sandbox"),
        DECENTRALAND("Decentraland"),
        AXIE_INFINITY("Axie Infinity"),
        CRYPTOKITTIES("CryptoKitties"),
        GODS_UNCHAINED("Gods Unchained"),
        SORARE("Sorare"),
        NBA_TOP_SHOT("NBA Top Shot"),
        UFC_STRIKE("UFC Strike"),
        NFL_ALL_DAY("NFL All Day"),
        FORMULA_1("Formula 1"),
        CHAMPIONS_LEAGUE("Champions League"),
        OLYMPICS("Olympics"),
        WORLD_CUP("World Cup"),
        SUPER_BOWL("Super Bowl"),
        CHAMPIONSHIP("Championship"),
        ALL_STAR("All Star"),
        HALL_OF_FAME("Hall of Fame"),
        LEGENDARY("Legendary"),
        EPIC("Epic"),
        RARE("Rare"),
        COMMON("Common"),
        UNCOMMON("Uncommon");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum NFTStatus {
        MINTED("Minted"),
        LISTED("Listed"),
        SOLD("Sold"),
        TRANSFERRED("Transferred"),
        BURNED("Burned"),
        LOCKED("Locked"),
        STAKED("Staked"),
        FRACTIONALIZED("Fractionalized"),
        UNDER_AUCTION("Under Auction"),
        AUCTION_ENDED("Auction Ended"),
        EXPIRED("Expired"),
        DELISTED("Delisted"),
        SUSPENDED("Suspended"),
        UNDER_REVIEW("Under Review"),
        COMPLIANCE_CHECK("Compliance Check"),
        FRAUD_CHECK("Fraud Check"),
        MAINTENANCE("Maintenance"),
        MIGRATING("Migrating"),
        ARCHIVED("Archived");

        private final String displayName;

        NFTStatus(String displayName) {
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

    public enum StorageProvider {
        IPFS("InterPlanetary File System"),
        ARWEAVE("Arweave"),
        PINATA("Pinata"),
        INFURA("Infura"),
        ALCHMY("Alchemy"),
        QUICKNODE("QuickNode"),
        ANKR("Ankr"),
        GETBLOCK("GetBlock"),
        BLOCKNATIVE("Blocknative"),
        MORALIS("Moralis"),
        THIRDWEB("Thirdweb"),
        OPENZEPPELIN("OpenZeppelin"),
        HARDHAT("Hardhat"),
        TRUFFLE("Truffle"),
        REMIX("Remix"),
        FOUNDRY("Foundry"),
        BROWNIE("Brownie"),
        EMBARK("Embark"),
        DAPP("Dapp"),
        AAVE("Aave"),
        UNISWAP("Uniswap"),
        SUSHISWAP("SushiSwap"),
        CURVE("Curve"),
        BALANCER("Balancer"),
        COMPOUND("Compound"),
        MAKERDAO("MakerDAO"),
        YEARN("Yearn"),
        PICKLE("Pickle"),
        CONVEX("Convex"),
        FRAX("Frax"),
        SYNTHETIX("Synthetix"),
        PERPETUAL("Perpetual"),
        GMX("GMX"),
        DYDX("dYdX"),
        PERP("Perp"),
        RIBBON("Ribbon"),
        STRADDLE("Straddle"),
        BUTTERFLY("Butterfly"),
        IRON_CONDOR("Iron Condor"),
        CALENDAR_SPREAD("Calendar Spread"),
        DIAGONAL_SPREAD("Diagonal Spread"),
        VERTICAL_SPREAD("Vertical Spread"),
        STRANGLE("Strangle"),
        STRADDLE("Straddle"),
        BUTTERFLY("Butterfly"),
        IRON_CONDOR("Iron Condor"),
        CALENDAR_SPREAD("Calendar Spread"),
        DIAGONAL_SPREAD("Diagonal Spread"),
        VERTICAL_SPREAD("Vertical Spread");

        private final String displayName;

        StorageProvider(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public NFT() {}

    public NFT(String tokenId, String contractAddress, UUID ownerId, UUID creatorId, 
                String name, Blockchain blockchain, NFTStandard standard, Category category) {
        this.tokenId = tokenId;
        this.contractAddress = contractAddress;
        this.ownerId = ownerId;
        this.creatorId = creatorId;
        this.name = name;
        this.blockchain = blockchain;
        this.standard = standard;
        this.category = category;
        this.mintDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    // Helper methods
    public boolean isListedForSale() {
        return isListed && status == NFTStatus.LISTED;
    }

    public boolean isUnderAuction() {
        return isAuction && status == NFTStatus.UNDER_AUCTION;
    }

    public boolean isFractionalized() {
        return isFractionalized && fractionalizedSupply > 0;
    }

    public boolean isVerified() {
        return isVerified && verificationDate != null;
    }

    public BigDecimal getPriceChangePercentage() {
        if (currentPrice != null && mintPrice != null && mintPrice.compareTo(BigDecimal.ZERO) > 0) {
            return currentPrice.subtract(mintPrice)
                    .divide(mintPrice, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    public void incrementViewCount() {
        this.viewCount++;
        this.lastUpdated = LocalDateTime.now();
    }

    public void incrementLikeCount() {
        this.likeCount++;
        this.lastUpdated = LocalDateTime.now();
    }

    public void incrementShareCount() {
        this.shareCount++;
        this.lastUpdated = LocalDateTime.now();
    }

    public void incrementTradeCount() {
        this.tradeCount++;
        this.lastUpdated = LocalDateTime.now();
    }

    public void updatePrice(BigDecimal newPrice) {
        this.currentPrice = newPrice;
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateStatus(NFTStatus newStatus) {
        this.status = newStatus;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public NFTStandard getStandard() {
        return standard;
    }

    public void setStandard(NFTStandard standard) {
        this.standard = standard;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public NFTStatus getStatus() {
        return status;
    }

    public void setStatus(NFTStatus status) {
        this.status = status;
    }

    public Boolean getIsListed() {
        return isListed;
    }

    public void setIsListed(Boolean isListed) {
        this.isListed = isListed;
    }

    public Boolean getIsAuction() {
        return isAuction;
    }

    public void setIsAuction(Boolean isAuction) {
        this.isAuction = isAuction;
    }

    public Boolean getIsFractionalized() {
        return isFractionalized;
    }

    public void setIsFractionalized(Boolean isFractionalized) {
        this.isFractionalized = isFractionalized;
    }

    public Long getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(Long totalSupply) {
        this.totalSupply = totalSupply;
    }

    public Long getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(Long circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public Long getFractionalizedSupply() {
        return fractionalizedSupply;
    }

    public void setFractionalizedSupply(Long fractionalizedSupply) {
        this.fractionalizedSupply = fractionalizedSupply;
    }

    public BigDecimal getFractionalizedPrice() {
        return fractionalizedPrice;
    }

    public void setFractionalizedPrice(BigDecimal fractionalizedPrice) {
        this.fractionalizedPrice = fractionalizedPrice;
    }

    public String getFractionalizedCurrency() {
        return fractionalizedCurrency;
    }

    public void setFractionalizedCurrency(String fractionalizedCurrency) {
        this.fractionalizedCurrency = fractionalizedCurrency;
    }

    public String getMetadataUri() {
        return metadataUri;
    }

    public void setMetadataUri(String metadataUri) {
        this.metadataUri = metadataUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getAnimationUri() {
        return animationUri;
    }

    public void setAnimationUri(String animationUri) {
        this.animationUri = animationUri;
    }

    public String getExternalUri() {
        return externalUri;
    }

    public void setExternalUri(String externalUri) {
        this.externalUri = externalUri;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public BigDecimal getRoyaltyPercentage() {
        return royaltyPercentage;
    }

    public void setRoyaltyPercentage(BigDecimal royaltyPercentage) {
        this.royaltyPercentage = royaltyPercentage;
    }

    public String getRoyaltyRecipient() {
        return royaltyRecipient;
    }

    public void setRoyaltyRecipient(String royaltyRecipient) {
        this.royaltyRecipient = royaltyRecipient;
    }

    public BigDecimal getMintPrice() {
        return mintPrice;
    }

    public void setMintPrice(BigDecimal mintPrice) {
        this.mintPrice = mintPrice;
    }

    public String getMintCurrency() {
        return mintCurrency;
    }

    public void setMintCurrency(String mintCurrency) {
        this.mintCurrency = mintCurrency;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCurrentCurrency() {
        return currentCurrency;
    }

    public void setCurrentCurrency(String currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    public BigDecimal getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(BigDecimal floorPrice) {
        this.floorPrice = floorPrice;
    }

    public BigDecimal getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(BigDecimal highestBid) {
        this.highestBid = highestBid;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public Long getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getPriceChange24h() {
        return priceChange24h;
    }

    public void setPriceChange24h(BigDecimal priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public BigDecimal getPriceChange7d() {
        return priceChange7d;
    }

    public void setPriceChange7d(BigDecimal priceChange7d) {
        this.priceChange7d = priceChange7d;
    }

    public BigDecimal getPriceChange30d() {
        return priceChange30d;
    }

    public void setPriceChange30d(BigDecimal priceChange30d) {
        this.priceChange30d = priceChange30d;
    }

    public LocalDateTime getMintDate() {
        return mintDate;
    }

    public void setMintDate(LocalDateTime mintDate) {
        this.mintDate = mintDate;
    }

    public LocalDateTime getListDate() {
        return listDate;
    }

    public void setListDate(LocalDateTime listDate) {
        this.listDate = listDate;
    }

    public LocalDateTime getLastTradeDate() {
        return lastTradeDate;
    }

    public void setLastTradeDate(LocalDateTime lastTradeDate) {
        this.lastTradeDate = lastTradeDate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public LocalDateTime getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(LocalDateTime verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Boolean getIsCompliant() {
        return isCompliant;
    }

    public void setIsCompliant(Boolean isCompliant) {
        this.isCompliant = isCompliant;
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

    public StorageProvider getStorageProvider() {
        return storageProvider;
    }

    public void setStorageProvider(StorageProvider storageProvider) {
        this.storageProvider = storageProvider;
    }

    public String getStorageHash() {
        return storageHash;
    }

    public void setStorageHash(String storageHash) {
        this.storageHash = storageHash;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileDimensions() {
        return fileDimensions;
    }

    public void setFileDimensions(String fileDimensions) {
        this.fileDimensions = fileDimensions;
    }

    public List<NFTOffer> getOffers() {
        return offers;
    }

    public void setOffers(List<NFTOffer> offers) {
        this.offers = offers;
    }

    public List<NFTAuction> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<NFTAuction> auctions) {
        this.auctions = auctions;
    }

    public List<NFTTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<NFTTransaction> transactions) {
        this.transactions = transactions;
    }

    public List<NFTLike> getLikes() {
        return likes;
    }

    public void setLikes(List<NFTLike> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "NFT{" +
                "id=" + getId() +
                ", tokenId='" + tokenId + '\'' +
                ", name='" + name + '\'' +
                ", blockchain=" + blockchain +
                ", category=" + category +
                ", status=" + status +
                ", ownerId=" + ownerId +
                '}';
    }
}
