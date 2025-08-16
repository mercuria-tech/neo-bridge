# 🏦 NeoBridge Account Service - Complete API Documentation

**Comprehensive API documentation for the NeoBridge Account Service with multi-currency banking operations**

---

## 🎯 **Account Service Overview**

The **NeoBridge Account Service** provides comprehensive banking account management capabilities including multi-currency accounts, real-time balance tracking, transaction processing, and regulatory compliance. This service handles all core banking operations for the NeoBridge platform.

### **🌟 Service Features**
- **Multi-currency accounts** (30+ supported currencies)
- **Real-time balance tracking** with instant updates
- **Transaction processing** and history management
- **Account types** (Checking, Savings, Investment, Business)
- **Regulatory compliance** (SEPA, SWIFT, AML/KYC)
- **BaaS integration** with Solarisbank and Swan

---

## 🏗️ **Account Management Endpoints**

### **📝 Create New Account**

#### **POST** `/api/v1/accounts`
**Create a new banking account with specified type and currency**

**Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "accountType": "CHECKING",
  "currency": "EUR",
  "accountName": "My Main Account",
  "initialDeposit": 1000.00,
  "accountPurpose": "PERSONAL_USE",
  "preferredLanguage": "EN",
  "monthlyStatement": true,
  "overdraftProtection": false
}
```

**Response (Success - 201 Created):**
```json
{
  "success": true,
  "data": {
    "accountId": "acc_abc123",
    "accountNumber": "DE89370400440532013000",
    "iban": "DE89370400440532013000",
    "swiftCode": "COBADEFFXXX",
    "accountType": "CHECKING",
    "currency": "EUR",
    "accountName": "My Main Account",
    "balance": 1000.00,
    "availableBalance": 1000.00,
    "blockedBalance": 0.00,
    "status": "ACTIVE",
    "interestRate": 0.01,
    "monthlyFee": 0.00,
    "minBalance": 0.00,
    "dailyLimit": 10000.00,
    "monthlyLimit": 100000.00,
    "createdAt": "2024-01-15T10:30:00Z",
    "accountFeatures": [
      "ONLINE_BANKING",
      "MOBILE_APP",
      "DEBIT_CARD",
      "SEPA_TRANSFERS"
    ]
  },
  "message": "Account created successfully",
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

**Account Types Available:**
- **CHECKING**: Daily banking with debit card
- **SAVINGS**: Interest-bearing savings account
- **INVESTMENT**: Investment portfolio account
- **BUSINESS**: Business banking account
- **JOINT**: Shared account for multiple users
- **CUSTODY**: Asset custody account

---

### **📖 Get Account Details**

#### **GET** `/api/v1/accounts/{accountId}`
**Retrieve detailed information about a specific account**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "accountId": "acc_abc123",
    "accountNumber": "DE89370400440532013000",
    "iban": "DE89370400440532013000",
    "swiftCode": "COBADEFFXXX",
    "accountType": "CHECKING",
    "currency": "EUR",
    "accountName": "My Main Account",
    "balance": 2500.75,
    "availableBalance": 2400.75,
    "blockedBalance": 100.00,
    "status": "ACTIVE",
    "interestRate": 0.01,
    "monthlyFee": 0.00,
    "minBalance": 0.00,
    "dailyLimit": 10000.00,
    "monthlyLimit": 100000.00,
    "lastTransactionDate": "2024-01-15T09:15:00Z",
    "createdAt": "2024-01-01T00:00:00Z",
    "updatedAt": "2024-01-15T09:15:00Z",
    "accountFeatures": [
      "ONLINE_BANKING",
      "MOBILE_APP",
      "DEBIT_CARD",
      "SEPA_TRANSFERS",
      "SWIFT_TRANSFERS"
    ],
    "linkedAccounts": [
      {
        "accountId": "acc_def456",
        "accountType": "SAVINGS",
        "currency": "EUR",
        "accountName": "My Savings"
      }
    ]
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **📋 List User Accounts**

#### **GET** `/api/v1/accounts`
**Retrieve all accounts for the authenticated user**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Query Parameters:**
```
?page=0&size=20&accountType=CHECKING&currency=EUR&status=ACTIVE
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "accountId": "acc_abc123",
        "accountNumber": "DE89370400440532013000",
        "accountType": "CHECKING",
        "currency": "EUR",
        "accountName": "My Main Account",
        "balance": 2500.75,
        "status": "ACTIVE",
        "lastTransactionDate": "2024-01-15T09:15:00Z"
      },
      {
        "accountId": "acc_def456",
        "accountNumber": "DE89370400440532013001",
        "accountType": "SAVINGS",
        "currency": "EUR",
        "accountName": "My Savings",
        "balance": 5000.00,
        "status": "ACTIVE",
        "lastTransactionDate": "2024-01-14T16:30:00Z"
      },
      {
        "accountId": "acc_ghi789",
        "accountNumber": "DE89370400440532013002",
        "accountType": "INVESTMENT",
        "currency": "USD",
        "accountName": "Investment Portfolio",
        "balance": 15000.00,
        "status": "ACTIVE",
        "lastTransactionDate": "2024-01-13T11:45:00Z"
      }
    ],
    "pagination": {
      "page": 0,
      "size": 20,
      "totalElements": 3,
      "totalPages": 1,
      "first": true,
      "last": true
    },
    "summary": {
      "totalAccounts": 3,
      "totalBalance": 22500.75,
      "currencies": ["EUR", "USD"],
      "accountTypes": ["CHECKING", "SAVINGS", "INVESTMENT"]
    }
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **✏️ Update Account Details**

#### **PUT** `/api/v1/accounts/{accountId}`
**Update account information and preferences**

**Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "accountName": "Updated Account Name",
  "monthlyStatement": false,
  "overdraftProtection": true,
  "preferredLanguage": "DE"
}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "message": "Account updated successfully",
    "updatedFields": ["accountName", "monthlyStatement", "overdraftProtection", "preferredLanguage"],
    "accountId": "acc_abc123"
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

## 💰 **Balance & Transaction Endpoints**

### **💰 Get Account Balance**

#### **GET** `/api/v1/accounts/{accountId}/balance`
**Retrieve current account balance and limits**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "accountId": "acc_abc123",
    "currentBalance": 2500.75,
    "availableBalance": 2400.75,
    "blockedBalance": 100.00,
    "pendingBalance": 0.00,
    "currency": "EUR",
    "lastUpdated": "2024-01-15T10:30:00Z",
    "limits": {
      "dailyLimit": 10000.00,
      "monthlyLimit": 100000.00,
      "dailyUsed": 1500.00,
      "monthlyUsed": 5000.00,
      "dailyRemaining": 8500.00,
      "monthlyRemaining": 95000.00
    },
    "overdraft": {
      "enabled": false,
      "limit": 0.00,
      "used": 0.00,
      "available": 0.00
    }
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **📊 Get Transaction History**

#### **GET** `/api/v1/accounts/{accountId}/transactions`
**Retrieve account transaction history with filtering**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Query Parameters:**
```
?page=0&size=20&startDate=2024-01-01&endDate=2024-01-15&type=ALL&minAmount=0&maxAmount=10000
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "transactionId": "txn_abc123",
        "type": "DEPOSIT",
        "amount": 1000.00,
        "currency": "EUR",
        "description": "Salary deposit",
        "referenceNumber": "REF123456",
        "status": "COMPLETED",
        "transactionDate": "2024-01-15T09:00:00Z",
        "postedDate": "2024-01-15T09:00:00Z",
        "balanceAfter": 2500.75,
        "category": "INCOME",
        "merchant": "Employer Corp",
        "location": "Berlin, Germany"
      },
      {
        "transactionId": "txn_def456",
        "type": "TRANSFER",
        "amount": -500.00,
        "currency": "EUR",
        "description": "Transfer to savings",
        "referenceNumber": "REF123457",
        "status": "COMPLETED",
        "transactionDate": "2024-01-14T16:30:00Z",
        "postedDate": "2024-01-14T16:30:00Z",
        "balanceAfter": 1500.75,
        "category": "TRANSFER",
        "merchant": "Internal Transfer",
        "location": "NeoBridge Platform"
      }
    ],
    "pagination": {
      "page": 0,
      "size": 20,
      "totalElements": 45,
      "totalPages": 3,
      "first": true,
      "last": false
    },
    "summary": {
      "totalTransactions": 45,
      "totalInflow": 5000.00,
      "totalOutflow": 2500.00,
      "netFlow": 2500.00,
      "categories": {
        "INCOME": 5000.00,
        "TRANSFER": -2500.00,
        "PAYMENT": 0.00,
        "FEE": -25.00
      }
    }
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

## 🔄 **Transfer & Payment Endpoints**

### **💸 Internal Transfer**

#### **POST** `/api/v1/accounts/{accountId}/transfers/internal`
**Transfer funds between user's own accounts**

**Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "toAccountId": "acc_def456",
  "amount": 500.00,
  "currency": "EUR",
  "description": "Monthly savings transfer",
  "scheduledDate": "2024-01-15T10:30:00Z",
  "recurring": {
    "enabled": true,
    "frequency": "MONTHLY",
    "dayOfMonth": 15,
    "endDate": "2024-12-31"
  }
}
```

**Response (Success - 201 Created):**
```json
{
  "success": true,
  "data": {
    "transferId": "trf_abc123",
    "fromAccount": {
      "accountId": "acc_abc123",
      "accountNumber": "DE89370400440532013000",
      "currency": "EUR"
    },
    "toAccount": {
      "accountId": "acc_def456",
      "accountNumber": "DE89370400440532013001",
      "currency": "EUR"
    },
    "amount": 500.00,
    "currency": "EUR",
    "description": "Monthly savings transfer",
    "status": "COMPLETED",
    "transferDate": "2024-01-15T10:30:00Z",
    "referenceNumber": "REF123458",
    "fees": 0.00,
    "exchangeRate": 1.0000,
    "recurring": {
      "enabled": true,
      "frequency": "MONTHLY",
      "nextTransferDate": "2024-02-15T10:30:00Z"
    }
  },
  "message": "Internal transfer completed successfully",
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **🌍 SEPA Transfer**

#### **POST** `/api/v1/accounts/{accountId}/transfers/sepa`
**Create SEPA transfer to external European bank account**

**Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "beneficiaryName": "John Smith",
  "beneficiaryIban": "DE89370400440532013000",
  "beneficiaryBic": "COBADEFFXXX",
  "amount": 250.00,
  "currency": "EUR",
  "description": "Payment for services",
  "reference": "INV-2024-001",
  "scheduledDate": "2024-01-16T10:30:00Z",
  "urgent": false
}
```

**Response (Success - 201 Created):**
```json
{
  "success": true,
  "data": {
    "transferId": "trf_def456",
    "transferType": "SEPA",
    "beneficiaryName": "John Smith",
    "beneficiaryIban": "DE89370400440532013000",
    "beneficiaryBic": "COBADEFFXXX",
    "amount": 250.00,
    "currency": "EUR",
    "description": "Payment for services",
    "reference": "INV-2024-001",
    "status": "PROCESSING",
    "scheduledDate": "2024-01-16T10:30:00Z",
    "estimatedCompletion": "2024-01-17T10:30:00Z",
    "fees": 0.50,
    "exchangeRate": 1.0000,
    "sepaDetails": {
      "sepaId": "SEPA123456",
      "creditorId": "DE98ZZZ09999999999",
      "mandateId": "MD123456"
    }
  },
  "message": "SEPA transfer initiated successfully",
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **🌐 SWIFT Transfer**

#### **POST** `/api/v1/accounts/{accountId}/transfers/swift`
**Create SWIFT transfer for international payments**

**Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "beneficiaryName": "Jane Doe",
  "beneficiaryAccountNumber": "1234567890",
  "beneficiaryBankName": "Chase Bank",
  "beneficiaryBankSwift": "CHASUS33",
  "beneficiaryBankAddress": "New York, NY, USA",
  "amount": 1000.00,
  "sourceCurrency": "EUR",
  "targetCurrency": "USD",
  "description": "International payment",
  "reference": "INT-2024-001",
  "urgent": true,
  "intermediaryBank": {
    "swift": "DEUTDEFF",
    "name": "Deutsche Bank",
    "account": "123456789"
  }
}
```

**Response (Success - 201 Created):**
```json
{
  "success": true,
  "data": {
    "transferId": "trf_ghi789",
    "transferType": "SWIFT",
    "beneficiaryName": "Jane Doe",
    "beneficiaryAccountNumber": "1234567890",
    "beneficiaryBankName": "Chase Bank",
    "beneficiaryBankSwift": "CHASUS33",
    "amount": 1000.00,
    "sourceCurrency": "EUR",
    "targetCurrency": "USD",
    "description": "International payment",
    "reference": "INT-2024-001",
    "status": "PROCESSING",
    "scheduledDate": "2024-01-15T10:30:00Z",
    "estimatedCompletion": "2024-01-18T10:30:00Z",
    "fees": 25.00,
    "exchangeRate": 1.0850,
    "swiftDetails": {
      "swiftId": "SWIFT123456",
      "messageType": "MT103",
      "intermediaryBank": "DEUTDEFF"
    }
  },
  "message": "SWIFT transfer initiated successfully",
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

## 📊 **Account Analytics Endpoints**

### **📈 Get Account Analytics**

#### **GET** `/api/v1/accounts/{accountId}/analytics`
**Retrieve comprehensive account analytics and insights**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Query Parameters:**
```
?period=LAST_30_DAYS&groupBy=DAY&includeCategories=true
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "accountId": "acc_abc123",
    "period": "LAST_30_DAYS",
    "startDate": "2023-12-16T00:00:00Z",
    "endDate": "2024-01-15T23:59:59Z",
    "summary": {
      "totalInflow": 5000.00,
      "totalOutflow": 2500.00,
      "netFlow": 2500.00,
      "averageBalance": 2250.00,
      "highestBalance": 3000.00,
      "lowestBalance": 1500.00,
      "transactionCount": 45
    },
    "cashFlow": {
      "daily": [
        {
          "date": "2024-01-15",
          "inflow": 1000.00,
          "outflow": 500.00,
          "netFlow": 500.00,
          "balance": 2500.75
        }
      ],
      "weekly": [
        {
          "week": "2024-W01",
          "inflow": 3000.00,
          "outflow": 1500.00,
          "netFlow": 1500.00,
          "averageBalance": 2000.00
        }
      ]
    },
    "categoryAnalysis": {
      "inflow": {
        "INCOME": 5000.00,
        "TRANSFER": 0.00,
        "REFUND": 0.00
      },
      "outflow": {
        "TRANSFER": 2000.00,
        "PAYMENT": 400.00,
        "FEE": 100.00
      }
    },
    "spendingPatterns": {
      "topMerchants": [
        {
          "merchant": "Grocery Store",
          "amount": 300.00,
          "transactions": 8
        }
      ],
      "spendingByDay": {
        "monday": 200.00,
        "tuesday": 150.00,
        "wednesday": 100.00
      }
    }
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

## 🔒 **Account Security Endpoints**

### **🔐 Block/Unblock Account**

#### **POST** `/api/v1/accounts/{accountId}/block`
**Temporarily block account for security reasons**

**Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "reason": "SUSPICIOUS_ACTIVITY",
  "duration": "24_HOURS",
  "description": "Unusual transaction pattern detected"
}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "accountId": "acc_abc123",
    "status": "BLOCKED",
    "blockReason": "SUSPICIOUS_ACTIVITY",
    "blockedAt": "2024-01-15T10:30:00Z",
    "blockExpiresAt": "2024-01-16T10:30:00Z",
    "description": "Unusual transaction pattern detected",
    "actions": [
      "CONTACT_SUPPORT",
      "VERIFY_IDENTITY",
      "REVIEW_TRANSACTIONS"
    ]
  },
  "message": "Account blocked successfully",
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **🔍 Get Account Activity**

#### **GET** `/api/v1/accounts/{accountId}/activity`
**Retrieve account activity and security events**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Query Parameters:**
```
?page=0&size=20&type=ALL&startDate=2024-01-01&endDate=2024-01-15
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "activityId": "act_abc123",
        "type": "LOGIN",
        "timestamp": "2024-01-15T10:30:00Z",
        "ipAddress": "192.168.1.100",
        "deviceInfo": {
          "deviceType": "MOBILE",
          "os": "iOS 17.0",
          "appVersion": "1.0.0"
        },
        "location": "Berlin, Germany",
        "riskScore": 0.1,
        "status": "SUCCESS"
      },
      {
        "activityId": "act_def456",
        "type": "TRANSACTION",
        "timestamp": "2024-01-15T09:15:00Z",
        "ipAddress": "192.168.1.100",
        "deviceInfo": {
          "deviceType": "MOBILE",
          "os": "iOS 17.0",
          "appVersion": "1.0.0"
        },
        "location": "Berlin, Germany",
        "riskScore": 0.2,
        "status": "SUCCESS",
        "details": {
          "transactionType": "SEPA_TRANSFER",
          "amount": 500.00,
          "beneficiary": "John Smith"
        }
      }
    ],
    "pagination": {
      "page": 0,
      "size": 20,
      "totalElements": 150,
      "totalPages": 8
    },
    "securitySummary": {
      "totalActivities": 150,
      "highRiskActivities": 2,
      "blockedActivities": 0,
      "lastRiskAssessment": "2024-01-15T10:00:00Z"
    }
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

## 🚨 **Error Handling**

### **📋 Error Response Format**
All error responses follow a consistent format:

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human-readable error message",
    "details": {
      "field": "Additional error details",
      "accountId": "acc_abc123",
      "constraint": "Business rule violation"
    }
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

### **🔍 Common Error Codes**

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| `ACCOUNT_NOT_FOUND` | 404 | Account does not exist |
| `INSUFFICIENT_FUNDS` | 400 | Insufficient balance for transaction |
| `ACCOUNT_BLOCKED` | 423 | Account is temporarily blocked |
| `TRANSACTION_LIMIT_EXCEEDED` | 400 | Transaction exceeds daily/monthly limit |
| `INVALID_CURRENCY` | 400 | Currency not supported for account |
| `BENEFICIARY_NOT_FOUND` | 404 | Beneficiary account not found |
| `TRANSFER_FAILED` | 500 | Transfer processing failed |
| `VALIDATION_ERROR` | 400 | Request validation failed |

---

## 🔒 **Security Considerations**

### **🛡️ Security Features**
- **Account Blocking**: Automatic and manual account blocking
- **Transaction Limits**: Daily and monthly transaction limits
- **Risk Scoring**: Real-time transaction risk assessment
- **Activity Monitoring**: Complete account activity tracking
- **Fraud Detection**: AI-powered fraud detection system

### **🔐 Compliance Features**
- **SEPA Compliance**: Full SEPA payment processing
- **SWIFT Compliance**: International payment standards
- **AML/KYC**: Anti-money laundering and know-your-customer
- **Regulatory Reporting**: Automated compliance reporting
- **Audit Trail**: Complete transaction audit logging

---

## 🚀 **Rate Limiting**

### **📊 Rate Limit Rules**
- **Account Creation**: 5 accounts per day
- **Balance Queries**: 100 requests per minute
- **Transaction History**: 50 requests per minute
- **Transfers**: 10 transfers per hour
- **Analytics**: 20 requests per minute

---

## 🏁 **Conclusion**

The **NeoBridge Account Service API** provides comprehensive banking account management with enterprise-grade security and compliance features. Built for multi-currency operations and global banking standards, it delivers a robust foundation for modern neobanking services.

**Key API strengths:**
- **Complete account lifecycle** management
- **Multi-currency support** for global operations
- **Advanced security** with fraud detection
- **Regulatory compliance** with SEPA/SWIFT standards
- **Real-time analytics** and insights

**Ready to power modern banking! 🚀**

---

<div align="center">

**NeoBridge Account Service APIs**  
**Built by Mercuria Technologies for Harmony Q&Q GmbH**

</div>
