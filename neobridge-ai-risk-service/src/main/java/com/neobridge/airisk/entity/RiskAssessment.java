package com.neobridge.airisk.entity;

import com.neobridge.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * RiskAssessment entity representing AI-generated risk assessments for users and portfolios.
 * Stores comprehensive risk metrics and AI model predictions.
 */
@Entity
@Table(name = "risk_assessments", indexes = {
    @Index(name = "idx_risk_assessments_user_id", columnList = "user_id"),
    @Index(name = "idx_risk_assessments_portfolio_id", columnList = "portfolio_id"),
    @Index(name = "idx_risk_assessments_assessment_date", columnList = "assessment_date"),
    @Index(name = "idx_risk_assessments_risk_score", columnList = "risk_score")
})
public class RiskAssessment extends BaseEntity {

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "portfolio_id")
    private UUID portfolioId;

    @NotNull
    @Column(name = "assessment_date", nullable = false)
    private LocalDateTime assessmentDate;

    @NotNull
    @Column(name = "assessment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssessmentType assessmentType;

    @NotNull
    @Column(name = "risk_score", nullable = false, precision = 5, scale = 4)
    private BigDecimal riskScore;

    @Column(name = "risk_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    @Column(name = "confidence_score", precision = 5, scale = 4)
    private BigDecimal confidenceScore;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "model_accuracy", precision = 5, scale = 4)
    private BigDecimal modelAccuracy;

    // Risk Metrics
    @Column(name = "volatility", precision = 5, scale = 4)
    private BigDecimal volatility;

    @Column(name = "var_95", precision = 5, scale = 4)
    private BigDecimal var95; // Value at Risk 95%

    @Column(name = "cvar_95", precision = 5, scale = 4)
    private BigDecimal cvar95; // Conditional Value at Risk 95%

    @Column(name = "max_drawdown", precision = 5, scale = 4)
    private BigDecimal maxDrawdown;

    @Column(name = "sharpe_ratio", precision = 5, scale = 4)
    private BigDecimal sharpeRatio;

    @Column(name = "sortino_ratio", precision = 5, scale = 4)
    private BigDecimal sortinoRatio;

    @Column(name = "beta", precision = 5, scale = 4)
    private BigDecimal beta;

    @Column(name = "alpha", precision = 5, scale = 4)
    private BigDecimal alpha;

    @Column(name = "correlation", precision = 5, scale = 4)
    private BigDecimal correlation;

    @Column(name = "concentration_risk", precision = 5, scale = 4)
    private BigDecimal concentrationRisk;

    @Column(name = "liquidity_risk", precision = 5, scale = 4)
    private BigDecimal liquidityRisk;

    @Column(name = "credit_risk", precision = 5, scale = 4)
    private BigDecimal creditRisk;

    @Column(name = "market_risk", precision = 5, scale = 4)
    private BigDecimal marketRisk;

    @Column(name = "operational_risk", precision = 5, scale = 4)
    private BigDecimal operationalRisk;

    @Column(name = "regulatory_risk", precision = 5, scale = 4)
    private BigDecimal regulatoryRisk;

    // AI Model Features
    @Column(name = "feature_vector", columnDefinition = "TEXT")
    private String featureVector;

    @Column(name = "prediction_probabilities", columnDefinition = "TEXT")
    private String predictionProbabilities;

    @Column(name = "feature_importance", columnDefinition = "TEXT")
    private String featureImportance;

    @Column(name = "model_explanation", columnDefinition = "TEXT")
    private String modelExplanation;

    // Risk Factors
    @Column(name = "age_factor", precision = 5, scale = 4)
    private BigDecimal ageFactor;

    @Column(name = "income_factor", precision = 5, scale = 4)
    private BigDecimal incomeFactor;

    @Column(name = "employment_factor", precision = 5, scale = 4)
    private BigDecimal employmentFactor;

    @Column(name = "credit_history_factor", precision = 5, scale = 4)
    private BigDecimal creditHistoryFactor;

    @Column(name = "investment_experience_factor", precision = 5, scale = 4)
    private BigDecimal investmentExperienceFactor;

    @Column(name = "portfolio_diversification_factor", precision = 5, scale = 4)
    private BigDecimal portfolioDiversificationFactor;

    @Column(name = "market_conditions_factor", precision = 5, scale = 4)
    private BigDecimal marketConditionsFactor;

    @Column(name = "economic_indicators_factor", precision = 5, scale = 4)
    private BigDecimal economicIndicatorsFactor;

    // Recommendations
    @Column(name = "risk_recommendations", columnDefinition = "TEXT")
    private String riskRecommendations;

    @Column(name = "portfolio_recommendations", columnDefinition = "TEXT")
    private String portfolioRecommendations;

    @Column(name = "action_items", columnDefinition = "TEXT")
    private String actionItems;

    @Column(name = "next_assessment_date")
    private LocalDateTime nextAssessmentDate;

    @Column(name = "is_high_risk")
    private Boolean isHighRisk = false;

    @Column(name = "requires_attention")
    private Boolean requiresAttention = false;

    @Size(max = 1000)
    @Column(name = "notes")
    private String notes;

    // Enums
    public enum AssessmentType {
        USER_PROFILE("User Profile Assessment"),
        PORTFOLIO_RISK("Portfolio Risk Assessment"),
        TRANSACTION_RISK("Transaction Risk Assessment"),
        COMPLIANCE_RISK("Compliance Risk Assessment"),
        MARKET_RISK("Market Risk Assessment"),
        CREDIT_RISK("Credit Risk Assessment"),
        OPERATIONAL_RISK("Operational Risk Assessment"),
        REGULATORY_RISK("Regulatory Risk Assessment"),
        FRAUD_RISK("Fraud Risk Assessment"),
        LIQUIDITY_RISK("Liquidity Risk Assessment"),
        CONCENTRATION_RISK("Concentration Risk Assessment"),
        CORRELATION_RISK("Correlation Risk Assessment"),
        VOLATILITY_RISK("Volatility Risk Assessment"),
        DRAWDOWN_RISK("Drawdown Risk Assessment"),
        YIELD_RISK("Yield Risk Assessment"),
        CURRENCY_RISK("Currency Risk Assessment"),
        INTEREST_RATE_RISK("Interest Rate Risk Assessment"),
        INFLATION_RISK("Inflation Risk Assessment"),
        POLITICAL_RISK("Political Risk Assessment"),
        ENVIRONMENTAL_RISK("Environmental Risk Assessment"),
        SOCIAL_RISK("Social Risk Assessment"),
        GOVERNANCE_RISK("Governance Risk Assessment");

        private final String displayName;

        AssessmentType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum RiskLevel {
        VERY_LOW("Very Low Risk", 1, "0.0-0.2"),
        LOW("Low Risk", 2, "0.2-0.4"),
        MODERATE("Moderate Risk", 3, "0.4-0.6"),
        HIGH("High Risk", 4, "0.6-0.8"),
        VERY_HIGH("Very High Risk", 5, "0.8-1.0"),
        EXTREME("Extreme Risk", 6, "1.0+");

        private final String displayName;
        private final int level;
        private final String range;

        RiskLevel(String displayName, int level, String range) {
            this.displayName = displayName;
            this.level = level;
            this.range = range;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getLevel() {
            return level;
        }

        public String getRange() {
            return range;
        }

        public static RiskLevel fromScore(BigDecimal score) {
            if (score.compareTo(BigDecimal.valueOf(0.2)) <= 0) return VERY_LOW;
            if (score.compareTo(BigDecimal.valueOf(0.4)) <= 0) return LOW;
            if (score.compareTo(BigDecimal.valueOf(0.6)) <= 0) return MODERATE;
            if (score.compareTo(BigDecimal.valueOf(0.8)) <= 0) return HIGH;
            if (score.compareTo(BigDecimal.ONE) <= 0) return VERY_HIGH;
            return EXTREME;
        }
    }

    // Constructors
    public RiskAssessment() {}

    public RiskAssessment(UUID userId, AssessmentType assessmentType, BigDecimal riskScore) {
        this.userId = userId;
        this.assessmentType = assessmentType;
        this.riskScore = riskScore;
        this.assessmentDate = LocalDateTime.now();
        this.riskLevel = RiskLevel.fromScore(riskScore);
    }

    // Helper methods
    public boolean isHighRisk() {
        return riskLevel == RiskLevel.HIGH || riskLevel == RiskLevel.VERY_HIGH || riskLevel == RiskLevel.EXTREME;
    }

    public boolean requiresImmediateAttention() {
        return riskLevel == RiskLevel.EXTREME || (riskLevel == RiskLevel.VERY_HIGH && confidenceScore.compareTo(BigDecimal.valueOf(0.8)) > 0);
    }

    public BigDecimal getCompositeRiskScore() {
        // Weighted combination of various risk factors
        BigDecimal compositeScore = BigDecimal.ZERO;
        
        if (volatility != null) compositeScore = compositeScore.add(volatility.multiply(BigDecimal.valueOf(0.2)));
        if (var95 != null) compositeScore = compositeScore.add(var95.multiply(BigDecimal.valueOf(0.15)));
        if (maxDrawdown != null) compositeScore = compositeScore.add(maxDrawdown.multiply(BigDecimal.valueOf(0.15)));
        if (concentrationRisk != null) compositeScore = compositeScore.add(concentrationRisk.multiply(BigDecimal.valueOf(0.1)));
        if (liquidityRisk != null) compositeScore = compositeScore.add(liquidityRisk.multiply(BigDecimal.valueOf(0.1)));
        if (creditRisk != null) compositeScore = compositeScore.add(creditRisk.multiply(BigDecimal.valueOf(0.1)));
        if (marketRisk != null) compositeScore = compositeScore.add(marketRisk.multiply(BigDecimal.valueOf(0.1)));
        
        return compositeScore;
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(UUID portfolioId) {
        this.portfolioId = portfolioId;
    }

    public LocalDateTime getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDateTime assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public BigDecimal getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(BigDecimal riskScore) {
        this.riskScore = riskScore;
        this.riskLevel = RiskLevel.fromScore(riskScore);
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public BigDecimal getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(BigDecimal confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public BigDecimal getModelAccuracy() {
        return modelAccuracy;
    }

    public void setModelAccuracy(BigDecimal modelAccuracy) {
        this.modelAccuracy = modelAccuracy;
    }

    public BigDecimal getVolatility() {
        return volatility;
    }

    public void setVolatility(BigDecimal volatility) {
        this.volatility = volatility;
    }

    public BigDecimal getVar95() {
        return var95;
    }

    public void setVar95(BigDecimal var95) {
        this.var95 = var95;
    }

    public BigDecimal getCvar95() {
        return cvar95;
    }

    public void setCvar95(BigDecimal cvar95) {
        this.cvar95 = cvar95;
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

    public BigDecimal getSortinoRatio() {
        return sortinoRatio;
    }

    public void setSortinoRatio(BigDecimal sortinoRatio) {
        this.sortinoRatio = sortinoRatio;
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

    public BigDecimal getCorrelation() {
        return correlation;
    }

    public void setCorrelation(BigDecimal correlation) {
        this.correlation = correlation;
    }

    public BigDecimal getConcentrationRisk() {
        return concentrationRisk;
    }

    public void setConcentrationRisk(BigDecimal concentrationRisk) {
        this.concentrationRisk = concentrationRisk;
    }

    public BigDecimal getLiquidityRisk() {
        return liquidityRisk;
    }

    public void setLiquidityRisk(BigDecimal liquidityRisk) {
        this.liquidityRisk = liquidityRisk;
    }

    public BigDecimal getCreditRisk() {
        return creditRisk;
    }

    public void setCreditRisk(BigDecimal creditRisk) {
        this.creditRisk = creditRisk;
    }

    public BigDecimal getMarketRisk() {
        return marketRisk;
    }

    public void setMarketRisk(BigDecimal marketRisk) {
        this.marketRisk = marketRisk;
    }

    public BigDecimal getOperationalRisk() {
        return operationalRisk;
    }

    public void setOperationalRisk(BigDecimal operationalRisk) {
        this.operationalRisk = operationalRisk;
    }

    public BigDecimal getRegulatoryRisk() {
        return regulatoryRisk;
    }

    public void setRegulatoryRisk(BigDecimal regulatoryRisk) {
        this.regulatoryRisk = regulatoryRisk;
    }

    public String getFeatureVector() {
        return featureVector;
    }

    public void setFeatureVector(String featureVector) {
        this.featureVector = featureVector;
    }

    public String getPredictionProbabilities() {
        return predictionProbabilities;
    }

    public void setPredictionProbabilities(String predictionProbabilities) {
        this.predictionProbabilities = predictionProbabilities;
    }

    public String getFeatureImportance() {
        return featureImportance;
    }

    public void setFeatureImportance(String featureImportance) {
        this.featureImportance = featureImportance;
    }

    public String getModelExplanation() {
        return modelExplanation;
    }

    public void setModelExplanation(String modelExplanation) {
        this.modelExplanation = modelExplanation;
    }

    public BigDecimal getAgeFactor() {
        return ageFactor;
    }

    public void setAgeFactor(BigDecimal ageFactor) {
        this.ageFactor = ageFactor;
    }

    public BigDecimal getIncomeFactor() {
        return incomeFactor;
    }

    public void setIncomeFactor(BigDecimal incomeFactor) {
        this.incomeFactor = incomeFactor;
    }

    public BigDecimal getEmploymentFactor() {
        return employmentFactor;
    }

    public void setEmploymentFactor(BigDecimal employmentFactor) {
        this.employmentFactor = employmentFactor;
    }

    public BigDecimal getCreditHistoryFactor() {
        return creditHistoryFactor;
    }

    public void setCreditHistoryFactor(BigDecimal creditHistoryFactor) {
        this.creditHistoryFactor = creditHistoryFactor;
    }

    public BigDecimal getInvestmentExperienceFactor() {
        return investmentExperienceFactor;
    }

    public void setInvestmentExperienceFactor(BigDecimal investmentExperienceFactor) {
        this.investmentExperienceFactor = investmentExperienceFactor;
    }

    public BigDecimal getPortfolioDiversificationFactor() {
        return portfolioDiversificationFactor;
    }

    public void setPortfolioDiversificationFactor(BigDecimal portfolioDiversificationFactor) {
        this.portfolioDiversificationFactor = portfolioDiversificationFactor;
    }

    public BigDecimal getMarketConditionsFactor() {
        return marketConditionsFactor;
    }

    public void setMarketConditionsFactor(BigDecimal marketConditionsFactor) {
        this.marketConditionsFactor = marketConditionsFactor;
    }

    public BigDecimal getEconomicIndicatorsFactor() {
        return economicIndicatorsFactor;
    }

    public void setEconomicIndicatorsFactor(BigDecimal economicIndicatorsFactor) {
        this.economicIndicatorsFactor = economicIndicatorsFactor;
    }

    public String getRiskRecommendations() {
        return riskRecommendations;
    }

    public void setRiskRecommendations(String riskRecommendations) {
        this.riskRecommendations = riskRecommendations;
    }

    public String getPortfolioRecommendations() {
        return portfolioRecommendations;
    }

    public void setPortfolioRecommendations(String portfolioRecommendations) {
        this.portfolioRecommendations = portfolioRecommendations;
    }

    public String getActionItems() {
        return actionItems;
    }

    public void setActionItems(String actionItems) {
        this.actionItems = actionItems;
    }

    public LocalDateTime getNextAssessmentDate() {
        return nextAssessmentDate;
    }

    public void setNextAssessmentDate(LocalDateTime nextAssessmentDate) {
        this.nextAssessmentDate = nextAssessmentDate;
    }

    public Boolean getIsHighRisk() {
        return isHighRisk;
    }

    public void setIsHighRisk(Boolean isHighRisk) {
        this.isHighRisk = isHighRisk;
    }

    public Boolean getRequiresAttention() {
        return requiresAttention;
    }

    public void setRequiresAttention(Boolean requiresAttention) {
        this.requiresAttention = requiresAttention;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "RiskAssessment{" +
                "id=" + getId() +
                ", userId=" + userId +
                ", assessmentType=" + assessmentType +
                ", riskScore=" + riskScore +
                ", riskLevel=" + riskLevel +
                ", assessmentDate=" + assessmentDate +
                '}';
    }
}
