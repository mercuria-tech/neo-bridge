# 💰 NeoBridge Account Service

**Complete banking account management service with multi-currency support and advanced financial features**

---

## 🎯 **Service Overview**

The **NeoBridge Account Service** is the core banking engine of the platform, providing comprehensive account management, multi-currency support, transaction processing, and financial operations. Built for enterprise-grade banking, it supports traditional banking operations while integrating seamlessly with cryptocurrency and investment services.

### **🌟 Key Features**
- **Multi-currency account management** (30+ currencies supported)
- **SEPA and international transfers** with real-time processing
- **Advanced account types** (checking, savings, investment, crypto)
- **Real-time balance management** and transaction tracking
- **Compliance and regulatory** reporting capabilities
- **Integration with BaaS partners** (Solarisbank, Swan)
- **Advanced fraud detection** and risk management

---

## 🏗️ **Service Architecture**

```
neobridge-account-service/
├── src/main/java/com/neobridge/account/
│   ├── controller/        # REST API controllers
│   ├── service/          # Business logic services
│   ├── repository/       # Data access layer
│   ├── entity/           # Domain entities
│   ├── dto/              # Data transfer objects
│   ├── config/           # Service configurations
│   ├── integration/      # External service integrations
│   ├── validator/        # Business validation logic
│   └── util/             # Utility classes
├── src/main/resources/   # Configuration files
├── pom.xml              # Maven configuration
└── README.md            # This file
```

---

## 🏦 **Banking Features**

### **💳 Account Types**
- **Checking Accounts**: Daily banking with debit cards
- **Savings Accounts**: Interest-bearing accounts with tiered rates
- **Investment Accounts**: Securities and crypto trading accounts
- **Business Accounts**: Corporate banking with advanced features
- **Joint Accounts**: Multi-user account management
- **Custody Accounts**: Institutional asset custody

### **🌍 Multi-Currency Support**
- **30+ Fiat Currencies**: USD, EUR, GBP, CHF, JPY, AUD, CAD, etc.
- **Real-time Exchange Rates**: Live currency conversion
- **Multi-currency Wallets**: Single account, multiple currencies
- **Cross-currency Transfers**: Automatic conversion and routing
- **Currency Hedging**: Risk management for currency exposure

### **💸 Transaction Processing**
- **SEPA Transfers**: European payment processing
- **SWIFT Transfers**: International wire transfers
- **Instant Payments**: Real-time payment processing
- **Scheduled Payments**: Recurring and future-dated transfers
- **Bulk Payments**: Batch processing for multiple recipients
- **Payment Templates**: Saved payment configurations

---

## 🚀 **Quick Start**

### **📋 Prerequisites**
- Java 21 JDK
- Maven 3.9+
- PostgreSQL 16
- Redis 7.2
- NeoBridge Common Module
- NeoBridge Auth Service

### **🔧 Installation & Setup**
```bash
# Navigate to account service
cd neobridge-account-service

# Install dependencies
mvn clean install

# Run the service
mvn spring-boot:run
```

### **⚙️ Configuration**
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/neobridge_accounts
    username: neobridge_user
    password: neobridge_password
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

neobridge:
  account:
    # Account settings
    default-currency: EUR
    supported-currencies: [EUR, USD, GBP, CHF, JPY]
    
    # Transaction limits
    daily-transfer-limit: 10000.00
    monthly-transfer-limit: 100000.00
    
    # BaaS integration
    baas:
      provider: solarisbank
      api-key: ${BAAS_API_KEY}
      api-secret: ${BAAS_API_SECRET}
      environment: sandbox
```

---

## 💻 **API Endpoints**

### **🏦 Account Management**
```http
# Create Account
POST /api/v1/accounts
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "accountType": "CHECKING",
  "currency": "EUR",
  "accountName": "My Main Account",
  "initialDeposit": 1000.00
}

# Get Account Details
GET /api/v1/accounts/{accountId}
Authorization: Bearer {access_token}

# Get All User Accounts
GET /api/v1/accounts
Authorization: Bearer {access_token}

# Update Account
PUT /api/v1/accounts/{accountId}
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "accountName": "Updated Account Name",
  "status": "ACTIVE"
}
```

### **💸 Transaction Operations**
```http
# Create Transfer
POST /api/v1/transfers
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "fromAccountId": "acc_123",
  "toAccountId": "acc_456",
  "amount": 500.00,
  "currency": "EUR",
  "description": "Monthly rent payment",
  "transferType": "INTERNAL"
}

# Get Transaction History
GET /api/v1/accounts/{accountId}/transactions
Authorization: Bearer {access_token}
?page=0&size=20&sort=createdAt,desc

# Get Transaction Details
GET /api/v1/transactions/{transactionId}
Authorization: Bearer {access_token}
```

### **🌍 International Transfers**
```http
# Create SEPA Transfer
POST /api/v1/transfers/sepa
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "fromAccountId": "acc_123",
  "beneficiaryName": "John Doe",
  "beneficiaryIBAN": "DE89370400440532013000",
  "beneficiaryBIC": "COBADEFFXXX",
  "amount": 1000.00,
  "currency": "EUR",
  "description": "International payment"
}

# Create SWIFT Transfer
POST /api/v1/transfers/swift
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "fromAccountId": "acc_123",
  "beneficiaryName": "Jane Smith",
  "beneficiaryAccount": "1234567890",
  "beneficiaryBank": "CHASE BANK",
  "beneficiarySwiftCode": "CHASUS33",
  "amount": 5000.00,
  "currency": "USD",
  "description": "International wire transfer"
}
```

---

## 🏗️ **Data Models**

### **🏦 Account Entity**
```java
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {
    
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;
    
    @Column(name = "iban", unique = true)
    private String iban;
    
    @Column(name = "swift_code")
    private String swiftCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
    
    @Column(name = "account_name", nullable = false)
    private String accountName;
    
    @Column(name = "balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
}
```

### **💸 Transaction Entity**
```java
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {
    
    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;
    
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "reference_number")
    private String referenceNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;
    
    @Column(name = "executed_at")
    private LocalDateTime executedAt;
    
    @Column(name = "settled_at")
    private LocalDateTime settledAt;
}
```

### **🌍 Transfer Entity**
```java
@Entity
@Table(name = "transfers")
public class Transfer extends BaseEntity {
    
    @Column(name = "transfer_id", unique = true, nullable = false)
    private String transferId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type", nullable = false)
    private TransferType transferType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransferStatus status = TransferStatus.PENDING;
    
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
    
    @Column(name = "exchange_rate", precision = 19, scale = 6)
    private BigDecimal exchangeRate;
    
    @Column(name = "fees", precision = 19, scale = 4)
    private BigDecimal fees = BigDecimal.ZERO;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;
    
    @Column(name = "executed_at")
    private LocalDateTime executedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;
    
    // External transfer details
    @Column(name = "beneficiary_name")
    private String beneficiaryName;
    
    @Column(name = "beneficiary_iban")
    private String beneficiaryIBAN;
    
    @Column(name = "beneficiary_bic")
    private String beneficiaryBIC;
}
```

---

## 🔗 **External Integrations**

### **🏦 BaaS Integration (Solarisbank/Swan)**
```java
@Service
public class BaasIntegrationService {
    
    public Account createBaasAccount(CreateAccountRequest request) {
        // Create account in BaaS system
        BaasAccountResponse baasResponse = baasClient.createAccount(request);
        
        // Map to internal account model
        Account account = mapToAccount(baasResponse);
        
        // Save to local database
        return accountRepository.save(account);
    }
    
    public TransferResponse processTransfer(TransferRequest request) {
        // Process transfer through BaaS
        BaasTransferResponse baasResponse = baasClient.processTransfer(request);
        
        // Update local transaction status
        updateTransactionStatus(request.getTransactionId(), baasResponse.getStatus());
        
        return mapToTransferResponse(baasResponse);
    }
}
```

### **💳 Card Processor Integration**
```java
@Service
public class CardIntegrationService {
    
    public Card createCard(CreateCardRequest request) {
        // Create card in card processor system
        CardProcessorResponse processorResponse = cardProcessor.createCard(request);
        
        // Map to internal card model
        Card card = mapToCard(processorResponse);
        
        // Save to local database
        return cardRepository.save(card);
    }
    
    public CardTransaction processCardTransaction(CardTransactionRequest request) {
        // Process card transaction
        CardProcessorTransactionResponse processorResponse = 
            cardProcessor.processTransaction(request);
        
        // Create local transaction record
        CardTransaction transaction = mapToCardTransaction(processorResponse);
        
        return cardTransactionRepository.save(transaction);
    }
}
```

---

## 🔧 **Configuration Options**

### **🏦 Account Configuration**
```yaml
neobridge:
  account:
    # Account settings
    default-currency: EUR
    supported-currencies: [EUR, USD, GBP, CHF, JPY, AUD, CAD]
    
    # Account limits
    limits:
      daily-transfer: 10000.00
      monthly-transfer: 100000.00
      daily-withdrawal: 5000.00
      monthly-withdrawal: 50000.00
      
    # Account types
    types:
      checking:
        min-balance: 0.00
        monthly-fee: 0.00
        interest-rate: 0.00
      savings:
        min-balance: 100.00
        monthly-fee: 0.00
        interest-rate: 0.50
      investment:
        min-balance: 1000.00
        monthly-fee: 5.00
        interest-rate: 0.00
```

### **💸 Transaction Configuration**
```yaml
neobridge:
  account:
    transactions:
      # Processing settings
      batch-size: 100
      retry-attempts: 3
      timeout-seconds: 30
      
      # Fee structure
      fees:
        internal-transfer: 0.00
        sepa-transfer: 0.50
        swift-transfer: 25.00
        currency-conversion: 0.25
        
      # Limits
      limits:
        max-amount: 1000000.00
        min-amount: 0.01
        max-daily-transactions: 100
```

---

## 🧪 **Testing**

### **🔧 Unit Testing**
```bash
# Run unit tests
mvn test

# Run with coverage
mvn jacoco:report
```

### **🔗 Integration Testing**
```bash
# Run integration tests
mvn test -Dtest=*IntegrationTest

# Run with test containers
mvn test -Dtest=*IT
```

### **🏦 Banking Tests**
```java
@SpringBootTest
class AccountServiceIntegrationTest {
    
    @Test
    void testCreateAccount() {
        CreateAccountRequest request = CreateAccountRequest.builder()
            .accountType(AccountType.CHECKING)
            .currency(Currency.EUR)
            .accountName("Test Account")
            .initialDeposit(new BigDecimal("1000.00"))
            .build();
            
        Account account = accountService.createAccount(request);
        
        assertThat(account).isNotNull();
        assertThat(account.getAccountNumber()).isNotEmpty();
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("1000.00"));
    }
    
    @Test
    void testProcessTransfer() {
        TransferRequest request = TransferRequest.builder()
            .fromAccountId("acc_123")
            .toAccountId("acc_456")
            .amount(new BigDecimal("500.00"))
            .currency(Currency.EUR)
            .description("Test transfer")
            .build();
            
        TransferResponse response = transferService.processTransfer(request);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(TransferStatus.COMPLETED);
    }
}
```

---

## 📊 **Monitoring & Observability**

### **📈 Key Metrics**
- **Account creation** and management
- **Transaction processing** times and volumes
- **Transfer success** and failure rates
- **Balance changes** and account activity
- **Integration performance** with external services

### **🔍 Health Checks**
```http
# Health check endpoint
GET /actuator/health

# Account service health
GET /actuator/health/account

# Database health check
GET /actuator/health/db

# External service health
GET /actuator/health/baas
```

### **📝 Audit Logging**
- **Account changes** and modifications
- **Transaction processing** and status updates
- **Balance modifications** and adjustments
- **User actions** and administrative changes
- **Integration calls** to external services

---

## 🔒 **Security Features**

### **🛡️ Transaction Security**
- **Multi-factor authentication** for large transfers
- **Fraud detection** algorithms and monitoring
- **Transaction limits** and daily/monthly caps
- **Suspicious activity** detection and alerts
- **Secure communication** with external services

### **🔐 Data Protection**
- **Sensitive data encryption** at rest and in transit
- **PCI DSS compliance** for payment data
- **Audit trails** for all financial operations
- **Access control** based on user permissions
- **Data masking** for sensitive information

---

## 📋 **Compliance Features**

### **🔍 Regulatory Reporting**
- **Transaction monitoring** for AML compliance
- **Large transaction** reporting (over €15,000)
- **Suspicious activity** reporting (SAR)
- **Currency transaction** reporting (CTR)
- **Tax reporting** and documentation

### **📊 Audit Capabilities**
- **Complete audit trail** for all operations
- **User activity** logging and monitoring
- **System access** tracking and recording
- **Data retention** policies and compliance
- **Regulatory audit** support and reporting

---

## 🚀 **Deployment**

### **🐳 Docker Deployment**
```dockerfile
FROM openjdk:21-jdk-slim

COPY target/neobridge-account-service.jar app.jar
COPY config/ /app/config/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

### **☸️ Kubernetes Deployment**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: neobridge-account-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: neobridge-account-service
  template:
    metadata:
      labels:
        app: neobridge-account-service
    spec:
      containers:
      - name: account-service
        image: neobridge/account-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: account-db-secret
              key: url
        - name: BAAS_API_KEY
          valueFrom:
            secretKeyRef:
              name: baas-secret
              key: api-key
```

---

## 📚 **Documentation**

### **📖 API Documentation**
- **OpenAPI 3.0** specification
- **Swagger UI** integration
- **Postman collections** for testing
- **Code examples** in multiple languages

### **🏦 Banking Documentation**
- **Account types** and features
- **Transaction processing** flows
- **International transfer** procedures
- **Compliance and regulatory** requirements

---

## 🤝 **Contributing**

### **📝 Development Standards**
- Follow **Spring Boot best practices**
- Implement **comprehensive testing**
- Maintain **banking compliance**
- Document **all APIs and processes**
- Follow **financial security** guidelines

---

## 📞 **Support**

### **👥 Banking Team**
- **Product Manager**: Banking features and requirements
- **Compliance Officer**: Regulatory compliance support
- **Technical Lead**: Technical implementation support

---

## 📄 **License**

This service is part of the **NeoBridge platform** and is proprietary software developed by **Mercuria Technologies** for **Harmony Q&Q GmbH**.

---

## 🏁 **Conclusion**

The **NeoBridge Account Service** provides a complete banking foundation for the platform, supporting traditional banking operations while enabling modern financial services. Built with enterprise-grade security and compliance, it's ready for production use in regulated financial environments.

**Complete banking for the digital age! 🏦🚀**

---

<div align="center">

**Part of the NeoBridge Platform**  
**Built by Mercuria Technologies for Harmony Q&Q GmbH**

</div>
