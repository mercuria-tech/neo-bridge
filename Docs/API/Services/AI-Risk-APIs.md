# 🤖 AI Risk Service APIs

## Overview

The NeoBridge AI Risk Service provides advanced machine learning-powered risk assessment, fraud detection, and yield optimization capabilities. This service leverages cutting-edge AI/ML algorithms to provide real-time risk analysis, portfolio optimization recommendations, and fraud prevention for the NeoBridge platform.

## Base URL

```
Production: https://api.neobridge.com/ai-risk/v1
Development: http://localhost:8084/ai-risk/v1
```

## Authentication

All endpoints require valid OAuth 2.1 access tokens in the Authorization header:

```
Authorization: Bearer <access_token>
```

## Endpoints

### 1. Risk Assessment

#### Assess Portfolio Risk

Performs comprehensive risk assessment for a user's investment portfolio using advanced ML models.

```http
POST /risk/portfolio/assess
```

**Request Body:**
```json
{
  "userId": "user_12345",
  "portfolioId": "portfolio_67890",
  "assessmentType": "comprehensive",
  "riskTolerance": "moderate",
  "timeHorizon": "5_years",
  "includeStressTesting": true,
  "includeScenarioAnalysis": true,
  "customScenarios": [
    {
      "name": "Market Crash 2008",
      "marketDrop": -0.40,
      "volatilityIncrease": 2.5
    }
  ]
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "assessmentId": "assessment_12345",
    "userId": "user_12345",
    "portfolioId": "portfolio_67890",
    "riskScore": 0.65,
    "riskLevel": "moderate",
    "assessmentDate": "2024-01-20T14:30:00Z",
    "riskMetrics": {
      "var95": 0.085,
      "var99": 0.125,
      "expectedShortfall": 0.145,
      "sharpeRatio": 1.25,
      "sortinoRatio": 1.85,
      "maxDrawdown": 0.18,
      "volatility": 0.22
    },
    "riskBreakdown": {
      "marketRisk": 0.45,
      "creditRisk": 0.15,
      "liquidityRisk": 0.20,
      "operationalRisk": 0.20
    },
    "stressTestResults": [
      {
        "scenario": "Market Crash 2008",
        "portfolioImpact": -0.32,
        "riskIncrease": 1.8
      }
    ],
    "recommendations": [
      {
        "type": "risk_reduction",
        "priority": "high",
        "description": "Consider reducing exposure to high-volatility assets",
        "impact": "Risk score reduction: 0.15"
      }
    ]
  }
}
```

**Assessment Types:**
- `quick` - Basic risk assessment (5-10 seconds)
- `standard` - Standard assessment with key metrics (30-60 seconds)
- `comprehensive` - Full assessment with stress testing (2-5 minutes)

#### Get Risk Assessment History

Retrieves historical risk assessments for a portfolio.

```http
GET /risk/portfolio/{portfolioId}/assessments?startDate={startDate}&endDate={endDate}&type={type}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "assessments": [
      {
        "assessmentId": "assessment_12345",
        "assessmentDate": "2024-01-20T14:30:00Z",
        "riskScore": 0.65,
        "riskLevel": "moderate",
        "assessmentType": "comprehensive"
      }
    ],
    "trends": {
      "riskScoreChange": -0.05,
      "riskLevelChange": "decreased",
      "volatilityTrend": "decreasing"
    }
  }
}
```

### 2. Fraud Detection

#### Analyze Transaction Risk

Performs real-time fraud risk analysis for financial transactions using ML models.

```http
POST /fraud/transaction/analyze
```

**Request Body:**
```json
{
  "transactionId": "tx_12345",
  "userId": "user_12345",
  "transactionType": "payment",
  "amount": 1500.00,
  "currency": "USD",
  "recipient": "merchant_67890",
  "location": {
    "country": "US",
    "city": "New York",
    "ipAddress": "192.168.1.100"
  },
  "deviceInfo": {
    "deviceId": "device_123",
    "deviceType": "mobile",
    "browser": "Chrome Mobile"
  },
  "userBehavior": {
    "loginTime": "2024-01-20T14:25:00Z",
    "previousTransactions": 45,
    "averageAmount": 250.00
  }
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "analysisId": "analysis_12345",
    "transactionId": "tx_12345",
    "fraudRiskScore": 0.12,
    "riskLevel": "low",
    "analysisTime": "2024-01-20T14:30:00Z",
    "riskFactors": [
      {
        "factor": "amount_deviation",
        "score": 0.15,
        "description": "Transaction amount is 6x higher than user average"
      },
      {
        "factor": "location_consistency",
        "score": 0.05,
        "description": "Transaction location matches user's usual pattern"
      }
    ],
    "recommendations": [
      {
        "action": "allow",
        "confidence": 0.88,
        "reason": "Low fraud risk, transaction appears legitimate"
      }
    ],
    "mlModelVersion": "fraud_detection_v2.1.3"
  }
}
```

#### Get Fraud Alerts

Retrieves fraud alerts and suspicious activity reports.

```http
GET /fraud/alerts?userId={userId}&status={status}&severity={severity}&page={page}&size={size}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "alerts": [
      {
        "alertId": "alert_12345",
        "userId": "user_12345",
        "alertType": "suspicious_transaction",
        "severity": "medium",
        "status": "investigating",
        "description": "Unusual transaction pattern detected",
        "riskScore": 0.75,
        "createdAt": "2024-01-20T14:30:00Z",
        "actions": [
          {
            "action": "transaction_hold",
            "timestamp": "2024-01-20T14:30:00Z",
            "performedBy": "ai_system"
          }
        ]
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "totalElements": 1,
      "totalPages": 1
    }
  }
}
```

### 3. Yield Optimization

#### Optimize Portfolio Yield

Provides AI-powered recommendations for portfolio yield optimization.

```http
POST /yield/portfolio/optimize
```

**Request Body:**
```json
{
  "portfolioId": "portfolio_67890",
  "userId": "user_12345",
  "optimizationGoal": "maximize_yield",
  "riskConstraint": "moderate",
  "timeHorizon": "3_years",
  "liquidityRequirements": "medium",
  "preferredAssets": ["stocks", "bonds", "crypto"],
  "excludedAssets": ["penny_stocks"],
  "rebalancingFrequency": "quarterly",
  "includeDeFi": true
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "optimizationId": "optimization_12345",
    "portfolioId": "portfolio_67890",
    "optimizationDate": "2024-01-20T14:30:00Z",
    "currentYield": 0.045,
    "projectedYield": 0.068,
    "yieldImprovement": 0.023,
    "riskImpact": "minimal",
    "recommendations": [
      {
        "action": "rebalance",
        "priority": "high",
        "description": "Increase allocation to high-yield bonds",
        "expectedImpact": "+0.015 yield improvement",
        "riskChange": "+0.02 risk increase"
      },
      {
        "action": "add_position",
        "priority": "medium",
        "description": "Consider DeFi yield farming opportunities",
        "expectedImpact": "+0.008 yield improvement",
        "riskChange": "+0.05 risk increase"
      }
    ],
    "allocationChanges": [
      {
        "asset": "high_yield_bonds",
        "currentAllocation": 0.20,
        "recommendedAllocation": 0.30,
        "change": 0.10
      }
    ],
    "mlModelVersion": "yield_optimization_v1.2.0"
  }
}
```

#### Get Yield Opportunities

Retrieves available yield opportunities based on user preferences and risk profile.

```http
GET /yield/opportunities?riskLevel={riskLevel}&minYield={minYield}&assetType={assetType}&liquidity={liquidity}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "opportunities": [
      {
        "opportunityId": "yield_123",
        "assetType": "defi_lending",
        "protocol": "Aave V3",
        "asset": "USDC",
        "currentYield": 0.085,
        "projectedYield": 0.092,
        "riskLevel": "medium",
        "liquidity": "high",
        "minimumAmount": 1000,
        "lockPeriod": "0 days",
        "apy": "8.5%",
        "riskFactors": [
          "smart_contract_risk",
          "protocol_governance_risk"
        ],
        "lastUpdated": "2024-01-20T14:22:00Z"
      }
    ],
    "filters": {
      "riskLevel": "moderate",
      "minYield": 0.05,
      "assetType": "all",
      "liquidity": "high"
    }
  }
}
```

### 4. Market Analysis

#### Analyze Market Sentiment

Performs AI-powered market sentiment analysis for specific assets or markets.

```http
POST /market/sentiment/analyze
```

**Request Body:**
```json
{
  "assets": ["ETH", "BTC", "AAPL"],
  "analysisType": "comprehensive",
  "timeframe": "7_days",
  "includeSocialMedia": true,
  "includeNewsAnalysis": true,
  "includeTechnicalIndicators": true
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "analysisId": "sentiment_12345",
    "analysisDate": "2024-01-20T14:30:00Z",
    "timeframe": "7_days",
    "overallSentiment": "bullish",
    "sentimentScore": 0.72,
    "assetSentiments": [
      {
        "asset": "ETH",
        "sentiment": "very_bullish",
        "score": 0.85,
        "confidence": 0.88,
        "factors": [
          {
            "factor": "social_media",
            "score": 0.90,
            "description": "Positive sentiment on Twitter and Reddit"
          },
          {
            "factor": "news_analysis",
            "score": 0.80,
            "description": "Favorable news coverage about ETH 2.0"
          }
        ]
      }
    ],
    "marketTrends": {
      "trend": "increasing",
      "momentum": "strong",
      "volatility": "moderate"
    },
    "mlModelVersion": "sentiment_analysis_v2.0.1"
  }
}
```

#### Get Market Predictions

Retrieves AI-generated market predictions and forecasts.

```http
GET /market/predictions?asset={asset}&timeframe={timeframe}&confidence={confidence}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "predictions": [
      {
        "predictionId": "prediction_12345",
        "asset": "ETH",
        "timeframe": "30_days",
        "predictionDate": "2024-01-20T14:30:00Z",
        "predictedPrice": 2200.00,
        "confidence": 0.75,
        "predictionRange": {
          "low": 1900.00,
          "high": 2500.00
        },
        "factors": [
          "positive_technical_indicators",
          "strong_fundamentals",
          "institutional_adoption"
        ],
        "riskFactors": [
          "regulatory_uncertainty",
          "market_volatility"
        ],
        "mlModelVersion": "price_prediction_v1.5.2"
      }
    ]
  }
}
```

### 5. Model Management

#### Get Model Performance

Retrieves performance metrics for AI/ML models.

```http
GET /models/performance?modelType={modelType}&timeframe={timeframe}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "models": [
      {
        "modelId": "fraud_detection_v2.1.3",
        "modelType": "fraud_detection",
        "version": "2.1.3",
        "deploymentDate": "2024-01-15T10:00:00Z",
        "performance": {
          "accuracy": 0.945,
          "precision": 0.923,
          "recall": 0.956,
          "f1Score": 0.939,
          "falsePositiveRate": 0.077,
          "falseNegativeRate": 0.044
        },
        "usage": {
          "totalPredictions": 125000,
          "successfulPredictions": 118125,
          "uptime": 0.998
        },
        "lastUpdated": "2024-01-20T14:22:00Z"
      }
    ]
  }
}
```

#### Retrain Model

Initiates model retraining with new data.

```http
POST /models/{modelId}/retrain
```

**Request Body:**
```json
{
  "reason": "performance_degradation",
  "dataVersion": "2024.01.20",
  "expectedImprovement": "accuracy_increase",
  "priority": "medium"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "retrainJobId": "retrain_12345",
    "modelId": "fraud_detection_v2.1.3",
    "status": "queued",
    "estimatedDuration": "4-6 hours",
    "queuePosition": 2,
    "createdAt": "2024-01-20T14:30:00Z"
  }
}
```

### 6. Error Handling

The AI Risk Service uses standard HTTP status codes and provides detailed error information:

```json
{
  "success": false,
  "error": {
    "code": "MODEL_UNAVAILABLE",
    "message": "AI model is currently unavailable",
    "details": "Model is being retrained, expected completion in 2 hours",
    "timestamp": "2024-01-20T14:30:00Z",
    "retryAfter": 7200
  }
}
```

**Common Error Codes:**
- `MODEL_UNAVAILABLE` - AI model is temporarily unavailable
- `INSUFFICIENT_DATA` - Not enough data for analysis
- `INVALID_PARAMETERS` - Request parameters are invalid
- `RATE_LIMIT_EXCEEDED` - Too many requests to AI models
- `ANALYSIS_TIMEOUT` - Analysis took too long to complete
- `MODEL_ERROR` - Internal model processing error

### 7. Rate Limiting

The AI Risk Service implements intelligent rate limiting based on request complexity:

- **Quick Analysis**: 100 requests per minute
- **Standard Analysis**: 50 requests per minute
- **Comprehensive Analysis**: 20 requests per minute
- **Model Training**: 5 requests per hour

Rate limit headers are included in responses:
```
X-RateLimit-Limit: 50
X-RateLimit-Remaining: 45
X-RateLimit-Reset: 1642687200
X-RateLimit-ResetTime: 2024-01-20T15:30:00Z
```

### 8. Webhooks

The AI Risk Service supports webhooks for real-time notifications:

**Webhook Events:**
- `risk_assessment.completed` - Risk assessment completed
- `fraud_alert.triggered` - New fraud alert generated
- `yield_optimization.ready` - Yield optimization completed
- `market_prediction.updated` - Market prediction updated
- `model.retrained` - AI model retraining completed
- `anomaly.detected` - Anomaly detected in user behavior

**Webhook Configuration:**
```json
{
  "webhookId": "webhook_12345",
  "url": "https://your-app.com/webhooks/ai-risk",
  "events": ["fraud_alert.triggered", "risk_assessment.completed"],
  "secret": "webhook_secret_key",
  "status": "active"
}
```

### 9. Performance Considerations

- **Response Times**: Quick analysis (5-10s), Standard (30-60s), Comprehensive (2-5min)
- **Caching**: Results are cached for 15 minutes for identical requests
- **Batch Processing**: Multiple analyses can be batched for efficiency
- **Async Processing**: Long-running analyses return job IDs for status checking

### 10. Security Considerations

- **Data Privacy**: All user data is encrypted and anonymized for ML processing
- **Model Security**: Models are deployed in secure, isolated environments
- **Access Control**: Role-based access to different AI capabilities
- **Audit Logging**: All AI decisions are logged for compliance and debugging
- **Bias Detection**: Regular bias testing and mitigation for ML models

---

## Support

For technical support or questions about the AI Risk Service APIs:

- **Documentation**: [AI Risk Service Guide](Docs/Technical/AI-Risk-Service-Guide.md)
- **API Reference**: [OpenAPI Specification](Docs/API/openapi-ai-risk.yaml)
- **Support**: [support@neobridge.com](mailto:support@neobridge.com)
- **Status**: [API Status Page](https://status.neobridge.com)
- **ML Model Status**: [Model Performance Dashboard](https://ml-status.neobridge.com)
