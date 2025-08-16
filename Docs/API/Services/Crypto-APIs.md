# 🔗 Crypto Service APIs

## Overview

The NeoBridge Crypto Service provides comprehensive cryptocurrency operations including multi-chain wallet management, DeFi integration, trading capabilities, and institutional custody features. This service supports 20+ blockchain networks and provides enterprise-grade security for digital asset operations.

## Base URL

```
Production: https://api.neobridge.com/crypto/v1
Development: http://localhost:8083/crypto/v1
```

## Authentication

All endpoints require valid OAuth 2.1 access tokens in the Authorization header:

```
Authorization: Bearer <access_token>
```

## Endpoints

### 1. Wallet Management

#### Create Crypto Wallet

Creates a new cryptocurrency wallet for the specified blockchain network.

```http
POST /wallets
```

**Request Body:**
```json
{
  "userId": "user_12345",
  "blockchainNetwork": "ethereum",
  "walletType": "hot_wallet",
  "currency": "ETH",
  "label": "My Ethereum Wallet",
  "securityLevel": "standard",
  "backupEnabled": true,
  "multiSignature": false
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "walletId": "wallet_67890",
    "address": "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6",
    "blockchainNetwork": "ethereum",
    "currency": "ETH",
    "balance": "0.0",
    "status": "active",
    "createdAt": "2024-01-15T10:30:00Z",
    "securityFeatures": {
      "multiSignature": false,
      "backupEnabled": true,
      "encryptionLevel": "AES-256-GCM"
    }
  },
  "message": "Wallet created successfully"
}
```

**Validation Rules:**
- `blockchainNetwork`: Must be one of supported networks
- `walletType`: `hot_wallet`, `cold_wallet`, `hardware_wallet`
- `securityLevel`: `standard`, `enhanced`, `enterprise`

#### Get Wallet Details

Retrieves detailed information about a specific crypto wallet.

```http
GET /wallets/{walletId}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "walletId": "wallet_67890",
    "userId": "user_12345",
    "address": "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6",
    "blockchainNetwork": "ethereum",
    "currency": "ETH",
    "balance": "2.5",
    "status": "active",
    "label": "My Ethereum Wallet",
    "createdAt": "2024-01-15T10:30:00Z",
    "lastActivity": "2024-01-20T14:22:00Z",
    "securityFeatures": {
      "multiSignature": false,
      "backupEnabled": true,
      "encryptionLevel": "AES-256-GCM",
      "hsmProtected": false
    },
    "supportedTokens": ["ETH", "USDC", "USDT", "DAI"]
  }
}
```

#### List User Wallets

Retrieves all crypto wallets for a specific user.

```http
GET /wallets?userId={userId}&network={network}&status={status}
```

**Query Parameters:**
- `userId` (required): User identifier
- `network` (optional): Filter by blockchain network
- `status` (optional): Filter by wallet status
- `page` (optional): Page number for pagination
- `size` (optional): Number of items per page

**Response:**
```json
{
  "success": true,
  "data": {
    "wallets": [
      {
        "walletId": "wallet_67890",
        "address": "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6",
        "blockchainNetwork": "ethereum",
        "currency": "ETH",
        "balance": "2.5",
        "status": "active",
        "label": "My Ethereum Wallet"
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

### 2. Asset Management

#### Get Wallet Balance

Retrieves current balance and token holdings for a wallet.

```http
GET /wallets/{walletId}/balance
```

**Response:**
```json
{
  "success": true,
  "data": {
    "walletId": "wallet_67890",
    "address": "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6",
    "blockchainNetwork": "ethereum",
    "totalValueUSD": "4500.00",
    "assets": [
      {
        "currency": "ETH",
        "balance": "2.5",
        "valueUSD": "4500.00",
        "priceUSD": "1800.00",
        "lastUpdated": "2024-01-20T14:22:00Z"
      },
      {
        "currency": "USDC",
        "balance": "1000.00",
        "valueUSD": "1000.00",
        "priceUSD": "1.00",
        "lastUpdated": "2024-01-20T14:22:00Z"
      }
    ]
  }
}
```

#### Get Token Information

Retrieves detailed information about a specific token.

```http
GET /tokens/{tokenSymbol}?network={network}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "symbol": "ETH",
    "name": "Ethereum",
    "blockchainNetwork": "ethereum",
    "contractAddress": "0x0000000000000000000000000000000000000000",
    "decimals": 18,
    "totalSupply": "120000000",
    "marketCap": "216000000000",
    "priceUSD": "1800.00",
    "priceChange24h": "2.5",
    "volume24h": "15000000000",
    "lastUpdated": "2024-01-20T14:22:00Z"
  }
}
```

### 3. Transaction Operations

#### Send Cryptocurrency

Initiates a cryptocurrency transfer from one wallet to another.

```http
POST /transactions/send
```

**Request Body:**
```json
{
  "fromWalletId": "wallet_67890",
  "toAddress": "0x1234567890123456789012345678901234567890",
  "amount": "0.1",
  "currency": "ETH",
  "network": "ethereum",
  "priority": "medium",
  "memo": "Payment for services",
  "gasLimit": 21000,
  "maxFeePerGas": "25",
  "maxPriorityFeePerGas": "2"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "transactionId": "tx_12345",
    "fromWalletId": "wallet_67890",
    "toAddress": "0x1234567890123456789012345678901234567890",
    "amount": "0.1",
    "currency": "ETH",
    "network": "ethereum",
    "status": "pending",
    "hash": "0xabc123...",
    "gasUsed": 21000,
    "gasPrice": "25",
    "totalFee": "0.000525",
    "estimatedConfirmationTime": "2-5 minutes",
    "createdAt": "2024-01-20T14:30:00Z"
  }
}
```

**Validation Rules:**
- `amount`: Must be positive and not exceed wallet balance
- `priority`: `low`, `medium`, `high`, `urgent`
- `gasLimit`: Must be within network limits

#### Get Transaction Status

Retrieves the current status and details of a transaction.

```http
GET /transactions/{transactionId}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "transactionId": "tx_12345",
    "fromWalletId": "wallet_67890",
    "toAddress": "0x1234567890123456789012345678901234567890",
    "amount": "0.1",
    "currency": "ETH",
    "network": "ethereum",
    "status": "confirmed",
    "hash": "0xabc123...",
    "blockNumber": 18456789,
    "confirmations": 12,
    "gasUsed": 21000,
    "gasPrice": "25",
    "totalFee": "0.000525",
    "createdAt": "2024-01-20T14:30:00Z",
    "confirmedAt": "2024-01-20T14:35:00Z"
  }
}
```

#### Get Transaction History

Retrieves transaction history for a specific wallet.

```http
GET /wallets/{walletId}/transactions?type={type}&status={status}&page={page}&size={size}
```

**Query Parameters:**
- `type` (optional): `incoming`, `outgoing`, `all`
- `status` (optional): `pending`, `confirmed`, `failed`
- `startDate` (optional): Start date for filtering
- `endDate` (optional): End date for filtering

**Response:**
```json
{
  "success": true,
  "data": {
    "transactions": [
      {
        "transactionId": "tx_12345",
        "type": "outgoing",
        "amount": "0.1",
        "currency": "ETH",
        "status": "confirmed",
        "hash": "0xabc123...",
        "createdAt": "2024-01-20T14:30:00Z"
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

### 4. DeFi Operations

#### Get DeFi Protocols

Retrieves available DeFi protocols and their information.

```http
GET /defi/protocols?network={network}&category={category}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "protocols": [
      {
        "protocolId": "uniswap_v3",
        "name": "Uniswap V3",
        "category": "dex",
        "network": "ethereum",
        "totalValueLocked": "2500000000",
        "apy": "15.5",
        "riskLevel": "medium",
        "supportedTokens": ["ETH", "USDC", "USDT", "DAI"],
        "lastUpdated": "2024-01-20T14:22:00Z"
      }
    ]
  }
}
```

#### Get Yield Opportunities

Retrieves available yield farming and staking opportunities.

```http
GET /defi/yield-opportunities?network={network}&minApy={minApy}&riskLevel={riskLevel}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "opportunities": [
      {
        "opportunityId": "yield_123",
        "protocol": "Uniswap V3",
        "pair": "ETH/USDC",
        "network": "ethereum",
        "apy": "25.5",
        "riskLevel": "medium",
        "minimumStake": "100",
        "lockPeriod": "30 days",
        "rewards": ["UNI", "ETH"],
        "lastUpdated": "2024-01-20T14:22:00Z"
      }
    ]
  }
}
```

#### Stake Assets

Initiates staking of assets in a DeFi protocol.

```http
POST /defi/stake
```

**Request Body:**
```json
{
  "walletId": "wallet_67890",
  "protocolId": "uniswap_v3",
  "pair": "ETH/USDC",
  "amount": "1.0",
  "currency": "ETH",
  "lockPeriod": "30",
  "autoCompound": true
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "stakeId": "stake_12345",
    "walletId": "wallet_67890",
    "protocolId": "uniswap_v3",
    "pair": "ETH/USDC",
    "amount": "1.0",
    "currency": "ETH",
    "apy": "25.5",
    "status": "active",
    "startDate": "2024-01-20T14:30:00Z",
    "endDate": "2024-02-19T14:30:00Z",
    "estimatedRewards": "0.021"
  }
}
```

### 5. Institutional Custody

#### Create Custody Wallet

Creates a new institutional-grade custody wallet with enhanced security.

```http
POST /custody/wallets
```

**Request Body:**
```json
{
  "institutionId": "inst_12345",
  "walletName": "Institutional ETH Wallet",
  "blockchainNetwork": "ethereum",
  "currency": "ETH",
  "securityLevel": "enterprise",
  "multiSignature": true,
  "requiredSignatures": 3,
    "signers": [
      "user_1",
      "user_2", 
      "user_3"
    ],
  "hsmIntegration": true,
  "complianceLevel": "institutional"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "custodyWalletId": "custody_67890",
    "institutionId": "inst_12345",
    "address": "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6",
    "blockchainNetwork": "ethereum",
    "currency": "ETH",
    "status": "pending_activation",
    "securityFeatures": {
      "multiSignature": true,
      "requiredSignatures": 3,
      "hsmIntegration": true,
      "complianceLevel": "institutional"
    },
    "createdAt": "2024-01-20T14:30:00Z"
  }
}
```

#### Approve Custody Transaction

Approves a transaction for a multi-signature custody wallet.

```http
POST /custody/wallets/{custodyWalletId}/transactions/{transactionId}/approve
```

**Request Body:**
```json
{
  "approverId": "user_1",
  "signature": "0xdef456...",
  "approvalNote": "Approved for institutional transfer"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "transactionId": "tx_12345",
    "custodyWalletId": "custody_67890",
    "approverId": "user_1",
    "approvalStatus": "approved",
    "signaturesReceived": 1,
    "requiredSignatures": 3,
    "status": "pending_approval",
    "approvedAt": "2024-01-20T14:35:00Z"
  }
}
```

### 6. Network Operations

#### Get Network Status

Retrieves current status and information about blockchain networks.

```http
GET /networks/{network}/status
```

**Response:**
```json
{
  "success": true,
  "data": {
    "network": "ethereum",
    "status": "operational",
    "blockHeight": 18456789,
    "lastBlockTime": "2024-01-20T14:22:00Z",
    "averageBlockTime": "12.5",
    "gasPrice": {
      "slow": "20",
      "standard": "25",
      "fast": "30",
      "urgent": "40"
    },
    "networkLoad": "medium",
    "lastUpdated": "2024-01-20T14:22:00Z"
  }
}
```

#### Get Supported Networks

Retrieves list of all supported blockchain networks.

```http
GET /networks
```

**Response:**
```json
{
  "success": true,
  "data": {
    "networks": [
      {
        "network": "ethereum",
        "name": "Ethereum",
        "currency": "ETH",
        "status": "operational",
        "blockTime": "12.5",
        "supportedTokens": ["ETH", "USDC", "USDT", "DAI"],
        "features": ["smart_contracts", "defi", "nft"]
      },
      {
        "network": "polygon",
        "name": "Polygon",
        "currency": "MATIC",
        "status": "operational",
        "blockTime": "2.0",
        "supportedTokens": ["MATIC", "USDC", "USDT"],
        "features": ["smart_contracts", "defi", "nft", "scaling"]
      }
    ]
  }
}
```

### 7. Error Handling

The Crypto Service uses standard HTTP status codes and provides detailed error information:

```json
{
  "success": false,
  "error": {
    "code": "INSUFFICIENT_BALANCE",
    "message": "Insufficient balance for transaction",
    "details": "Wallet balance: 0.05 ETH, Required: 0.1 ETH",
    "timestamp": "2024-01-20T14:30:00Z"
  }
}
```

**Common Error Codes:**
- `INSUFFICIENT_BALANCE` - Wallet doesn't have enough funds
- `INVALID_ADDRESS` - Invalid blockchain address format
- `NETWORK_ERROR` - Blockchain network communication error
- `TRANSACTION_FAILED` - Transaction failed on blockchain
- `INSUFFICIENT_GAS` - Not enough gas for transaction
- `WALLET_NOT_FOUND` - Specified wallet doesn't exist
- `UNAUTHORIZED_ACCESS` - User doesn't have access to wallet

### 8. Rate Limiting

The Crypto Service implements rate limiting to ensure fair usage:

- **Standard Users**: 100 requests per minute
- **Premium Users**: 500 requests per minute
- **Institutional Users**: 1000 requests per minute

Rate limit headers are included in responses:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1642687200
```

### 9. Webhooks

The Crypto Service supports webhooks for real-time notifications:

**Webhook Events:**
- `wallet.created` - New wallet created
- `transaction.pending` - Transaction submitted to network
- `transaction.confirmed` - Transaction confirmed on blockchain
- `balance.updated` - Wallet balance changed
- `defi.stake.created` - New staking position created
- `defi.reward.earned` - Staking rewards earned

**Webhook Configuration:**
```json
{
  "webhookId": "webhook_12345",
  "url": "https://your-app.com/webhooks/crypto",
  "events": ["transaction.confirmed", "balance.updated"],
  "secret": "webhook_secret_key",
  "status": "active"
}
```

### 10. Security Considerations

- **API Keys**: Use secure API keys with appropriate permissions
- **Webhook Security**: Verify webhook signatures using the secret key
- **Rate Limiting**: Implement exponential backoff for rate limit handling
- **Error Handling**: Never expose sensitive information in error messages
- **HTTPS**: Always use HTTPS for production API calls
- **Token Storage**: Store access tokens securely and refresh before expiration

---

## Support

For technical support or questions about the Crypto Service APIs:

- **Documentation**: [Crypto Service Guide](Docs/Technical/Crypto-Service-Guide.md)
- **API Reference**: [OpenAPI Specification](Docs/API/openapi-crypto.yaml)
- **Support**: [support@neobridge.com](mailto:support@neobridge.com)
- **Status**: [API Status Page](https://status.neobridge.com)
