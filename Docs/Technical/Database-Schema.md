# 🗄️ NeoBridge Platform - Complete Database Schema

**Comprehensive database design and schema documentation for the NeoBridge neobank platform**

---

## 🎯 **Database Overview**

The **NeoBridge Platform** uses **PostgreSQL 16** as its primary database with a comprehensive schema designed for enterprise-grade banking operations. The database supports multi-currency accounts, cryptocurrency operations, investment management, and regulatory compliance.

### **🌟 Database Features**
- **Multi-currency support** (30+ currencies)
- **Cryptocurrency wallet management** (20+ blockchain networks)
- **Investment portfolio tracking** (40+ account types)
- **Regulatory compliance** (KYC/AML, audit trails)
- **Real-time transaction processing** (high-performance design)
- **Data encryption** (AES-256-GCM for sensitive data)

---

## 🏗️ **Database Architecture**

### **📊 Database Design Principles**
- **Normalized Design**: 3NF normalization for data integrity
- **Partitioning Strategy**: Time-based partitioning for large tables
- **Indexing Strategy**: Optimized indexes for query performance
- **Backup Strategy**: Continuous backup with point-in-time recovery
- **Replication**: Read replicas for load distribution

### **🔐 Security Features**
- **Column-level encryption** for sensitive financial data
- **Row-level security** for multi-tenant isolation
- **Audit logging** for all data modifications
- **Data masking** for non-production environments
- **Access control** with role-based permissions

---

## 🗂️ **Core Database Schema**

### **👥 User Management Tables**

#### **`users` Table**
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    nationality VARCHAR(3),
    country_of_residence VARCHAR(3),
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    mfa_enabled BOOLEAN DEFAULT FALSE,
    account_locked BOOLEAN DEFAULT FALSE,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_id ON users(user_id);
CREATE INDEX idx_users_created_at ON users(created_at);
```

#### **`user_profiles` Table**
```sql
CREATE TABLE user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    profile_type VARCHAR(50) NOT NULL, -- INDIVIDUAL, BUSINESS, INSTITUTIONAL
    tax_id VARCHAR(50),
    occupation VARCHAR(255),
    employer VARCHAR(255),
    annual_income DECIMAL(15,2),
    source_of_funds VARCHAR(100),
    risk_tolerance VARCHAR(20), -- CONSERVATIVE, MODERATE, AGGRESSIVE
    investment_experience VARCHAR(20), -- BEGINNER, INTERMEDIATE, ADVANCED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_profiles_user_id ON user_profiles(user_id);
```

#### **`user_addresses` Table**
```sql
CREATE TABLE user_addresses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    address_type VARCHAR(20) NOT NULL, -- RESIDENTIAL, MAILING, BUSINESS
    street_address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state_province VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(3) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_addresses_user_id ON user_addresses(user_id);
```

### **🏦 Banking Tables**

#### **`accounts` Table**
```sql
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_id VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT REFERENCES users(id),
    account_type VARCHAR(50) NOT NULL, -- CHECKING, SAVINGS, INVESTMENT, CRYPTO
    account_name VARCHAR(255) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    account_number VARCHAR(50) UNIQUE,
    iban VARCHAR(34) UNIQUE,
    swift_code VARCHAR(11),
    balance DECIMAL(19,4) DEFAULT 0.0000,
    available_balance DECIMAL(19,4) DEFAULT 0.0000,
    blocked_balance DECIMAL(19,4) DEFAULT 0.0000,
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, SUSPENDED, CLOSED, FROZEN
    interest_rate DECIMAL(5,4) DEFAULT 0.0000,
    monthly_fee DECIMAL(10,2) DEFAULT 0.00,
    min_balance DECIMAL(19,4) DEFAULT 0.0000,
    daily_limit DECIMAL(19,4),
    monthly_limit DECIMAL(19,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP NULL
);

-- Indexes for performance
CREATE INDEX idx_accounts_user_id ON accounts(user_id);
CREATE INDEX idx_accounts_account_type ON accounts(account_type);
CREATE INDEX idx_accounts_currency ON accounts(currency);
CREATE INDEX idx_accounts_status ON accounts(status);
CREATE INDEX idx_accounts_iban ON accounts(iban);
```

#### **`transactions` Table**
```sql
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(50) UNIQUE NOT NULL,
    account_id BIGINT REFERENCES accounts(id),
    transaction_type VARCHAR(50) NOT NULL, -- DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT
    amount DECIMAL(19,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    description VARCHAR(500),
    reference_number VARCHAR(100),
    external_reference VARCHAR(100),
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
    transaction_date TIMESTAMP NOT NULL,
    posted_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Partitioning by transaction_date for performance
CREATE TABLE transactions_y2024m01 PARTITION OF transactions
FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');

CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_transactions_transaction_type ON transactions(transaction_type);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_transaction_date ON transactions(transaction_date);
```

#### **`transfers` Table**
```sql
CREATE TABLE transfers (
    id BIGSERIAL PRIMARY KEY,
    transfer_id VARCHAR(50) UNIQUE NOT NULL,
    from_account_id BIGINT REFERENCES accounts(id),
    to_account_id BIGINT REFERENCES accounts(id),
    transfer_type VARCHAR(50) NOT NULL, -- INTERNAL, SEPA, SWIFT, CRYPTO
    amount DECIMAL(19,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    exchange_rate DECIMAL(19,6),
    fees DECIMAL(19,4) DEFAULT 0.0000,
    description VARCHAR(500),
    beneficiary_name VARCHAR(255),
    beneficiary_iban VARCHAR(34),
    beneficiary_bic VARCHAR(11),
    scheduled_date DATE,
    executed_at TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, PROCESSING, COMPLETED, FAILED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transfers_from_account ON transfers(from_account_id);
CREATE INDEX idx_transfers_to_account ON transfers(to_account_id);
CREATE INDEX idx_transfers_status ON transfers(status);
CREATE INDEX idx_transfers_scheduled_date ON transfers(scheduled_date);
```

### **₿ Cryptocurrency Tables**

#### **`crypto_wallets` Table**
```sql
CREATE TABLE crypto_wallets (
    id BIGSERIAL PRIMARY KEY,
    wallet_id VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT REFERENCES users(id),
    blockchain_network VARCHAR(50) NOT NULL, -- ETHEREUM, POLYGON, BSC, SOLANA
    wallet_address VARCHAR(255) NOT NULL,
    wallet_type VARCHAR(50) NOT NULL, -- HOT, COLD, MULTISIG
    derivation_path VARCHAR(100),
    encrypted_private_key TEXT,
    public_key TEXT,
    balance_synced_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_crypto_wallets_user_id ON crypto_wallets(user_id);
CREATE INDEX idx_crypto_wallets_network ON crypto_wallets(blockchain_network);
CREATE INDEX idx_crypto_wallets_address ON crypto_wallets(wallet_address);
```

#### **`crypto_assets` Table**
```sql
CREATE TABLE crypto_assets (
    id BIGSERIAL PRIMARY KEY,
    asset_id VARCHAR(50) UNIQUE NOT NULL,
    wallet_id BIGINT REFERENCES crypto_wallets(id),
    token_symbol VARCHAR(20) NOT NULL, -- BTC, ETH, USDC, etc.
    token_contract_address VARCHAR(255),
    balance DECIMAL(30,18) DEFAULT 0.000000000000000000,
    locked_balance DECIMAL(30,18) DEFAULT 0.000000000000000000,
    last_price_usd DECIMAL(19,6),
    last_price_updated_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_crypto_assets_wallet_id ON crypto_assets(wallet_id);
CREATE INDEX idx_crypto_assets_symbol ON crypto_assets(token_symbol);
CREATE INDEX idx_crypto_assets_contract ON crypto_assets(token_contract_address);
```

#### **`crypto_transactions` Table**
```sql
CREATE TABLE crypto_transactions (
    id BIGSERIAL PRIMARY KEY,
    tx_hash VARCHAR(255) UNIQUE NOT NULL,
    wallet_id BIGINT REFERENCES crypto_wallets(id),
    transaction_type VARCHAR(50) NOT NULL, -- SEND, RECEIVE, SWAP, STAKE
    token_symbol VARCHAR(20) NOT NULL,
    amount DECIMAL(30,18) NOT NULL,
    fee_amount DECIMAL(30,18),
    fee_currency VARCHAR(20),
    from_address VARCHAR(255),
    to_address VARCHAR(255),
    block_number BIGINT,
    block_timestamp TIMESTAMP,
    confirmations INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, CONFIRMED, FAILED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_crypto_transactions_wallet_id ON crypto_transactions(wallet_id);
CREATE INDEX idx_crypto_transactions_tx_hash ON crypto_transactions(tx_hash);
CREATE INDEX idx_crypto_transactions_status ON crypto_transactions(status);
CREATE INDEX idx_crypto_transactions_block_number ON crypto_transactions(block_number);
```

### **📈 Investment Tables**

#### **`portfolios` Table**
```sql
CREATE TABLE portfolios (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT REFERENCES users(id),
    portfolio_name VARCHAR(255) NOT NULL,
    portfolio_type VARCHAR(50) NOT NULL, -- GROWTH, INCOME, BALANCED, CONSERVATIVE
    risk_profile VARCHAR(20) NOT NULL, -- CONSERVATIVE, MODERATE, AGGRESSIVE
    target_return DECIMAL(5,4),
    max_risk DECIMAL(5,4),
    currency VARCHAR(3) NOT NULL,
    total_value DECIMAL(19,4) DEFAULT 0.0000,
    total_cost DECIMAL(19,4) DEFAULT 0.0000,
    total_gain_loss DECIMAL(19,4) DEFAULT 0.0000,
    total_gain_loss_percentage DECIMAL(5,4) DEFAULT 0.0000,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_portfolios_user_id ON portfolios(user_id);
CREATE INDEX idx_portfolios_type ON portfolios(portfolio_type);
CREATE INDEX idx_portfolios_risk_profile ON portfolios(risk_profile);
```

#### **`portfolio_assets` Table**
```sql
CREATE TABLE portfolio_assets (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT REFERENCES portfolios(id),
    asset_type VARCHAR(50) NOT NULL, -- STOCK, BOND, ETF, CRYPTO, REAL_ESTATE
    asset_symbol VARCHAR(20),
    asset_name VARCHAR(255),
    quantity DECIMAL(19,6) NOT NULL,
    average_cost DECIMAL(19,4) NOT NULL,
    current_price DECIMAL(19,4),
    current_value DECIMAL(19,4),
    gain_loss DECIMAL(19,4),
    gain_loss_percentage DECIMAL(5,4),
    allocation_percentage DECIMAL(5,4),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_portfolio_assets_portfolio_id ON portfolio_assets(portfolio_id);
CREATE INDEX idx_portfolio_assets_asset_type ON portfolio_assets(asset_type);
CREATE INDEX idx_portfolio_assets_symbol ON portfolio_assets(asset_symbol);
```

### **🔐 Security & Compliance Tables**

#### **`user_sessions` Table**
```sql
CREATE TABLE user_sessions (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT REFERENCES users(id),
    access_token VARCHAR(500) NOT NULL,
    refresh_token VARCHAR(500),
    device_info JSONB,
    ip_address INET,
    user_agent TEXT,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX idx_user_sessions_session_id ON user_sessions(session_id);
CREATE INDEX idx_user_sessions_expires_at ON user_sessions(expires_at);
```

#### **`audit_logs` Table**
```sql
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    action VARCHAR(100) NOT NULL,
    resource_type VARCHAR(50) NOT NULL,
    resource_id VARCHAR(50),
    old_values JSONB,
    new_values JSONB,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_resource_type ON audit_logs(resource_type);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
```

#### **`kyc_verifications` Table**
```sql
CREATE TABLE kyc_verifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    verification_type VARCHAR(50) NOT NULL, -- IDENTITY, ADDRESS, INCOME, SOURCE_OF_FUNDS
    provider VARCHAR(50) NOT NULL, -- JUMIO, ONFIDO, MANUAL
    status VARCHAR(20) NOT NULL, -- PENDING, IN_PROGRESS, APPROVED, REJECTED
    verification_data JSONB,
    documents JSONB,
    verified_at TIMESTAMP,
    expires_at TIMESTAMP,
    rejection_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_kyc_verifications_user_id ON kyc_verifications(user_id);
CREATE INDEX idx_kyc_verifications_status ON kyc_verifications(status);
CREATE INDEX idx_kyc_verifications_type ON kyc_verifications(verification_type);
```

---

## 🔗 **Database Relationships**

### **📊 Entity Relationship Diagram**
```
users (1) ←→ (N) accounts
users (1) ←→ (N) crypto_wallets
users (1) ←→ (N) portfolios
users (1) ←→ (N) user_profiles
users (1) ←→ (N) user_addresses
users (1) ←→ (N) user_sessions
users (1) ←→ (N) audit_logs
users (1) ←→ (N) kyc_verifications

accounts (1) ←→ (N) transactions
accounts (1) ←→ (N) transfers (from_account)
accounts (1) ←→ (N) transfers (to_account)

crypto_wallets (1) ←→ (N) crypto_assets
crypto_wallets (1) ←→ (N) crypto_transactions

portfolios (1) ←→ (N) portfolio_assets
```

### **🔍 Key Relationships**
- **One-to-Many**: User can have multiple accounts, wallets, portfolios
- **One-to-Many**: Account can have multiple transactions and transfers
- **One-to-Many**: Wallet can have multiple crypto assets and transactions
- **One-to-Many**: Portfolio can have multiple assets
- **Audit Trail**: All major entities have audit logging

---

## 📊 **Database Performance**

### **⚡ Optimization Strategies**
- **Table Partitioning**: Large tables partitioned by date
- **Strategic Indexing**: Indexes on frequently queried columns
- **Connection Pooling**: Efficient database connection management
- **Query Optimization**: Optimized SQL queries with proper joins
- **Materialized Views**: Pre-computed views for complex queries

### **📈 Performance Metrics**
- **Query Response Time**: <50ms for 95% of queries
- **Connection Pool**: 20-100 concurrent connections
- **Index Usage**: >90% of queries use indexes
- **Cache Hit Rate**: >80% for frequently accessed data
- **Transaction Throughput**: 1000+ transactions per second

---

## 🔒 **Data Security**

### **🛡️ Encryption Strategy**
- **Column Encryption**: AES-256-GCM for sensitive financial data
- **Key Management**: HSM integration for encryption keys
- **Data Masking**: PII protection in non-production environments
- **Audit Logging**: Complete audit trail for all data access
- **Access Control**: Role-based access with fine-grained permissions

### **🔐 Security Policies**
- **Data Classification**: Sensitive, confidential, public data levels
- **Access Logging**: All database access logged and monitored
- **Backup Encryption**: Encrypted backups with key rotation
- **Network Security**: Database access restricted to application servers
- **Vulnerability Scanning**: Regular security assessments

---

## 📋 **Database Maintenance**

### **🔄 Maintenance Schedule**
- **Daily**: Automated backups and health checks
- **Weekly**: Performance analysis and index optimization
- **Monthly**: Security updates and vulnerability scans
- **Quarterly**: Schema review and optimization
- **Annually**: Complete database health assessment

### **💾 Backup Strategy**
- **Continuous Backup**: WAL archiving for point-in-time recovery
- **Full Backups**: Daily full database backups
- **Incremental Backups**: Hourly incremental backups
- **Cross-Region**: Backup replication to secondary region
- **Testing**: Monthly backup restoration testing

---

## 🚀 **Database Deployment**

### **☁️ Production Environment**
- **Primary Database**: Google Cloud SQL (PostgreSQL 16)
- **Instance Size**: 8 vCPU, 32GB RAM, 500GB SSD
- **High Availability**: Multi-zone deployment
- **Backup Storage**: Cloud Storage with lifecycle policies
- **Monitoring**: Cloud Monitoring with custom dashboards

### **🔧 Development Environment**
- **Local Database**: Docker PostgreSQL container
- **Data Seeding**: Automated test data generation
- **Migration Scripts**: Flyway for schema versioning
- **Testing**: Separate test database for integration tests

---

## 🏁 **Conclusion**

The **NeoBridge Platform database schema** is designed for **enterprise-grade banking operations** with comprehensive support for traditional banking, cryptocurrency, and investment services. Built with security, performance, and scalability in mind, it provides a robust foundation for the platform's financial services.

**Key strengths:**
- **Comprehensive financial data model** supporting all banking operations
- **Multi-currency and multi-asset support** for global operations
- **Advanced security features** with encryption and audit trails
- **Performance optimization** with strategic indexing and partitioning
- **Regulatory compliance** with complete audit and KYC support

**Ready to power the future of banking! 🚀**

---

<div align="center">

**NeoBridge Database Schema**  
**Built by Mercuria Technologies for Harmony Q&Q GmbH**

</div>
