package com.neobridge.investment.entity;

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
 * InvestmentAccount entity representing an investment account in the NeoBridge platform.
 * Supports multiple investment types and trading strategies.
 */
@Entity
@Table(name = "investment_accounts", indexes = {
    @Index(name = "idx_investment_accounts_user_id", columnList = "user_id"),
    @Index(name = "idx_investment_accounts_account_number", columnList = "account_number"),
    @Index(name = "idx_investment_accounts_account_type", columnList = "account_type"),
    @Index(name = "idx_investment_accounts_status", columnList = "status")
})
public class InvestmentAccount extends BaseEntity {

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @NotBlank
    @Size(max = 100)
    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_profile", nullable = false)
    private RiskProfile riskProfile = RiskProfile.CONSERVATIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "investment_strategy", nullable = false)
    private InvestmentStrategy investmentStrategy = InvestmentStrategy.BUY_AND_HOLD;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalValue = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "cash_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal cashBalance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "invested_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal investedAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "unrealized_pnl", nullable = false, precision = 19, scale = 4)
    private BigDecimal unrealizedPnl = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "realized_pnl", nullable = false, precision = 19, scale = 4)
    private BigDecimal realizedPnl = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total_return", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalReturn = BigDecimal.ZERO;

    @Column(name = "total_return_percentage", precision = 5, scale = 4)
    private BigDecimal totalReturnPercentage = BigDecimal.ZERO;

    @Column(name = "daily_return", precision = 5, scale = 4)
    private BigDecimal dailyReturn = BigDecimal.ZERO;

    @Column(name = "monthly_return", precision = 5, scale = 4)
    private BigDecimal monthlyReturn = BigDecimal.ZERO;

    @Column(name = "yearly_return", precision = 5, scale = 4)
    private BigDecimal yearlyReturn = BigDecimal.ZERO;

    @Column(name = "max_drawdown", precision = 5, scale = 4)
    private BigDecimal maxDrawdown = BigDecimal.ZERO;

    @Column(name = "sharpe_ratio", precision = 5, scale = 4)
    private BigDecimal sharpeRatio = BigDecimal.ZERO;

    @Column(name = "volatility", precision = 5, scale = 4)
    private BigDecimal volatility = BigDecimal.ZERO;

    @Column(name = "beta", precision = 5, scale = 4)
    private BigDecimal beta = BigDecimal.ZERO;

    @Column(name = "alpha", precision = 5, scale = 4)
    private BigDecimal alpha = BigDecimal.ZERO;

    @Column(name = "last_rebalance_date")
    private LocalDateTime lastRebalanceDate;

    @Column(name = "next_rebalance_date")
    private LocalDateTime nextRebalanceDate;

    @Column(name = "rebalance_frequency")
    private String rebalanceFrequency = "QUARTERLY";

    @Column(name = "is_auto_rebalance")
    private Boolean isAutoRebalance = false;

    @Column(name = "is_tax_optimized")
    private Boolean isTaxOptimized = false;

    @Column(name = "is_esg_focused")
    private Boolean isEsgFocused = false;

    @Size(max = 1000)
    @Column(name = "metadata")
    private String metadata;

    @OneToMany(mappedBy = "investmentAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PortfolioHolding> holdings = new ArrayList<>();

    @OneToMany(mappedBy = "investmentAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InvestmentOrder> orders = new ArrayList<>();

    // Enums
    public enum AccountType {
        INDIVIDUAL("Individual Account"),
        JOINT("Joint Account"),
        IRA("Individual Retirement Account"),
        ROTH_IRA("Roth IRA"),
        TRADITIONAL_IRA("Traditional IRA"),
        SEP_IRA("Simplified Employee Pension IRA"),
        SIMPLE_IRA("Savings Incentive Match Plan IRA"),
        ROLLOVER_IRA("Rollover IRA"),
        INHERITED_IRA("Inherited IRA"),
        CONVERSION_IRA("Conversion IRA"),
        TRUST("Trust Account"),
        CORPORATE("Corporate Account"),
        PARTNERSHIP("Partnership Account"),
        LLC("Limited Liability Company Account"),
        PENSION("Pension Account"),
        DEFINED_BENEFIT("Defined Benefit Plan"),
        DEFINED_CONTRIBUTION("Defined Contribution Plan"),
        HSA("Health Savings Account"),
        FSA("Flexible Spending Account"),
        DAF("Donor Advised Fund"),
        FOUNDATION("Foundation Account"),
        ENDOWMENT("Endowment Account"),
        CHARITABLE("Charitable Account"),
        CUSTODIAL("Custodial Account"),
        MINOR("Minor Account"),
        STUDENT("Student Account"),
        SENIOR("Senior Account"),
        PREMIUM("Premium Account"),
        BASIC("Basic Account"),
        STANDARD("Standard Account"),
        ADVANCED("Advanced Account"),
        PROFESSIONAL("Professional Account"),
        INSTITUTIONAL("Institutional Account"),
        WHOLESALE("Wholesale Account"),
        RETAIL("Retail Account"),
        ADVISORY("Advisory Account"),
        DISCRETIONARY("Discretionary Account"),
        NON_DISCRETIONARY("Non-Discretionary Account"),
        MANAGED("Managed Account"),
        SELF_DIRECTED("Self-Directed Account"),
        AUTOMATED("Automated Account"),
        HYBRID("Hybrid Account");

        private final String displayName;

        AccountType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum AccountStatus {
        ACTIVE("Active"),
        INACTIVE("Inactive"),
        SUSPENDED("Suspended"),
        CLOSED("Closed"),
        PENDING("Pending"),
        UNDER_REVIEW("Under Review"),
        COMPLIANCE_CHECK("Compliance Check"),
        FRAUD_CHECK("Fraud Check"),
        MAINTENANCE("Maintenance"),
        MIGRATING("Migrating"),
        ARCHIVED("Archived");

        private final String displayName;

        AccountStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum RiskProfile {
        CONSERVATIVE("Conservative"),
        MODERATE_CONSERVATIVE("Moderate Conservative"),
        MODERATE("Moderate"),
        MODERATE_AGGRESSIVE("Moderate Aggressive"),
        AGGRESSIVE("Aggressive"),
        VERY_AGGRESSIVE("Very Aggressive"),
        CUSTOM("Custom");

        private final String displayName;

        RiskProfile(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum InvestmentStrategy {
        BUY_AND_HOLD("Buy and Hold"),
        VALUE_INVESTING("Value Investing"),
        GROWTH_INVESTING("Growth Investing"),
        DIVIDEND_INVESTING("Dividend Investing"),
        MOMENTUM_INVESTING("Momentum Investing"),
        CONTRARIAN_INVESTING("Contrarian Investing"),
        INDEX_INVESTING("Index Investing"),
        ACTIVE_MANAGEMENT("Active Management"),
        PASSIVE_MANAGEMENT("Passive Management"),
        QUANTITATIVE("Quantitative"),
        FUNDAMENTAL("Fundamental"),
        TECHNICAL("Technical"),
        ARBITRAGE("Arbitrage"),
        PAIRS_TRADING("Pairs Trading"),
        MEAN_REVERSION("Mean Reversion"),
        TREND_FOLLOWING("Trend Following"),
        SCALPING("Scalping"),
        DAY_TRADING("Day Trading"),
        SWING_TRADING("Swing Trading"),
        POSITION_TRADING("Position Trading"),
        ALGORITHMIC("Algorithmic Trading"),
        HIGH_FREQUENCY("High Frequency Trading"),
        OPTIONS_TRADING("Options Trading"),
        FUTURES_TRADING("Futures Trading"),
        FOREX_TRADING("Forex Trading"),
        CRYPTO_TRADING("Cryptocurrency Trading"),
        COMMODITY_TRADING("Commodity Trading"),
        REAL_ESTATE("Real Estate Investment"),
        PRIVATE_EQUITY("Private Equity"),
        VENTURE_CAPITAL("Venture Capital"),
        HEDGE_FUND("Hedge Fund Strategy"),
        MUTUAL_FUND("Mutual Fund Strategy"),
        ETF_STRATEGY("ETF Strategy"),
        BOND_STRATEGY("Bond Strategy"),
        CASH_STRATEGY("Cash Strategy"),
        GOLD_STRATEGY("Gold Strategy"),
        INTERNATIONAL("International Strategy"),
        EMERGING_MARKETS("Emerging Markets Strategy"),
        SECTOR_ROTATION("Sector Rotation"),
        ASSET_ALLOCATION("Asset Allocation"),
        TACTICAL_ALLOCATION("Tactical Allocation"),
        STRATEGIC_ALLOCATION("Strategic Allocation"),
        CUSTOM("Custom Strategy");

        private final String displayName;

        InvestmentStrategy(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public InvestmentAccount() {}

    public InvestmentAccount(UUID userId, String accountNumber, String accountName, 
                            AccountType accountType) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountType = accountType;
    }

    // Helper methods
    public BigDecimal getTotalReturnAmount() {
        return unrealizedPnl.add(realizedPnl);
    }

    public BigDecimal getTotalReturnPercentage() {
        if (investedAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalReturnAmount().divide(investedAmount, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public void updateTotalValue() {
        this.totalValue = cashBalance.add(investedAmount);
    }

    public void updateUnrealizedPnl(BigDecimal newUnrealizedPnl) {
        this.unrealizedPnl = newUnrealizedPnl;
        updateTotalValue();
    }

    public void addRealizedPnl(BigDecimal realizedPnl) {
        this.realizedPnl = this.realizedPnl.add(realizedPnl);
        updateTotalValue();
    }

    public void addCash(BigDecimal amount) {
        this.cashBalance = this.cashBalance.add(amount);
        updateTotalValue();
    }

    public void subtractCash(BigDecimal amount) {
        this.cashBalance = this.cashBalance.subtract(amount);
        updateTotalValue();
    }

    public void addInvestment(BigDecimal amount) {
        this.investedAmount = this.investedAmount.add(amount);
        updateTotalValue();
    }

    public void subtractInvestment(BigDecimal amount) {
        this.investedAmount = this.investedAmount.subtract(amount);
        updateTotalValue();
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public RiskProfile getRiskProfile() {
        return riskProfile;
    }

    public void setRiskProfile(RiskProfile riskProfile) {
        this.riskProfile = riskProfile;
    }

    public InvestmentStrategy getInvestmentStrategy() {
        return investmentStrategy;
    }

    public void setInvestmentStrategy(InvestmentStrategy investmentStrategy) {
        this.investmentStrategy = investmentStrategy;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(BigDecimal investedAmount) {
        this.investedAmount = investedAmount;
    }

    public BigDecimal getUnrealizedPnl() {
        return unrealizedPnl;
    }

    public void setUnrealizedPnl(BigDecimal unrealizedPnl) {
        this.unrealizedPnl = unrealizedPnl;
    }

    public BigDecimal getRealizedPnl() {
        return realizedPnl;
    }

    public void setRealizedPnl(BigDecimal realizedPnl) {
        this.realizedPnl = realizedPnl;
    }

    public BigDecimal getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(BigDecimal totalReturn) {
        this.totalReturn = totalReturn;
    }

    public BigDecimal getTotalReturnPercentage() {
        return totalReturnPercentage;
    }

    public void setTotalReturnPercentage(BigDecimal totalReturnPercentage) {
        this.totalReturnPercentage = totalReturnPercentage;
    }

    public BigDecimal getDailyReturn() {
        return dailyReturn;
    }

    public void setDailyReturn(BigDecimal dailyReturn) {
        this.dailyReturn = dailyReturn;
    }

    public BigDecimal getMonthlyReturn() {
        return monthlyReturn;
    }

    public void setMonthlyReturn(BigDecimal monthlyReturn) {
        this.monthlyReturn = monthlyReturn;
    }

    public BigDecimal getYearlyReturn() {
        return yearlyReturn;
    }

    public void setYearlyReturn(BigDecimal yearlyReturn) {
        this.yearlyReturn = yearlyReturn;
    }

    public BigDecimal getMaxDrawdown() {
        return maxDrawdown;
    }

    public void setMaxDrawdown(BigDecimal maxDrawdown) {
        this.maxDrawdown = maxDrawdown;
    }

    public BigDecimal getSharpeRatio() {
        return sharpeRatio;
    }

    public void setSharpeRatio(BigDecimal sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    public BigDecimal getVolatility() {
        return volatility;
    }

    public void setVolatility(BigDecimal volatility) {
        this.volatility = volatility;
    }

    public BigDecimal getBeta() {
        return beta;
    }

    public void setBeta(BigDecimal beta) {
        this.beta = beta;
    }

    public BigDecimal getAlpha() {
        return alpha;
    }

    public void setAlpha(BigDecimal alpha) {
        this.alpha = alpha;
    }

    public LocalDateTime getLastRebalanceDate() {
        return lastRebalanceDate;
    }

    public void setLastRebalanceDate(LocalDateTime lastRebalanceDate) {
        this.lastRebalanceDate = lastRebalanceDate;
    }

    public LocalDateTime getNextRebalanceDate() {
        return nextRebalanceDate;
    }

    public void setNextRebalanceDate(LocalDateTime nextRebalanceDate) {
        this.nextRebalanceDate = nextRebalanceDate;
    }

    public String getRebalanceFrequency() {
        return rebalanceFrequency;
    }

    public void setRebalanceFrequency(String rebalanceFrequency) {
        this.rebalanceFrequency = rebalanceFrequency;
    }

    public Boolean getIsAutoRebalance() {
        return isAutoRebalance;
    }

    public void setIsAutoRebalance(Boolean isAutoRebalance) {
        this.isAutoRebalance = isAutoRebalance;
    }

    public Boolean getIsTaxOptimized() {
        return isTaxOptimized;
    }

    public void setIsTaxOptimized(Boolean isTaxOptimized) {
        this.isTaxOptimized = isTaxOptimized;
    }

    public Boolean getIsEsgFocused() {
        return isEsgFocused;
    }

    public void setIsEsgFocused(Boolean isEsgFocused) {
        this.isEsgFocused = isEsgFocused;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public List<PortfolioHolding> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<PortfolioHolding> holdings) {
        this.holdings = holdings;
    }

    public List<InvestmentOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<InvestmentOrder> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "InvestmentAccount{" +
                "id=" + getId() +
                ", userId=" + userId +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountType=" + accountType +
                ", status=" + status +
                ", totalValue=" + totalValue +
                ", cashBalance=" + cashBalance +
                ", investedAmount=" + investedAmount +
                '}';
    }
}
