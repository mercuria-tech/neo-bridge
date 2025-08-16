-- NeoBridge Platform - Initial Database Schema Migration
-- Version: V1
-- Description: Initial database setup with all core tables

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create custom types
CREATE TYPE user_status AS ENUM ('PENDING', 'ACTIVE', 'SUSPENDED', 'BLOCKED');
CREATE TYPE kyc_status AS ENUM ('PENDING', 'IN_PROGRESS', 'APPROVED', 'REJECTED');
CREATE TYPE account_status AS ENUM ('PENDING', 'ACTIVE', 'SUSPENDED', 'CLOSED');
CREATE TYPE account_type AS ENUM ('CHECKING', 'SAVINGS', 'BUSINESS', 'CRYPTO');
CREATE TYPE transaction_status AS ENUM ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'CANCELLED');
CREATE TYPE transaction_type AS ENUM ('TRANSFER', 'PAYMENT', 'CRYPTO_BUY', 'CRYPTO_SELL', 'CRYPTO_TRANSFER', 'CARD_PAYMENT');
CREATE TYPE card_status AS ENUM ('PENDING', 'ACTIVE', 'BLOCKED', 'EXPIRED', 'CANCELLED');
CREATE TYPE card_type AS ENUM ('VIRTUAL', 'PHYSICAL', 'DEBIT', 'CREDIT');
CREATE TYPE crypto_asset_type AS ENUM ('BTC', 'ETH', 'BNB', 'MATIC', 'SOL', 'ADA', 'USDT', 'USDC');
CREATE TYPE notification_type AS ENUM ('EMAIL', 'SMS', 'PUSH', 'IN_APP');
CREATE TYPE compliance_status AS ENUM ('PENDING', 'REVIEW', 'APPROVED', 'REJECTED', 'ESCALATED');

-- Users and Authentication
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    nationality VARCHAR(2),
    country_of_residence VARCHAR(2),
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    kyc_status kyc_status DEFAULT 'PENDING',
    user_status user_status DEFAULT 'PENDING',
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

-- User Roles and Permissions
CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE permissions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    resource VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE user_roles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    role_id UUID NOT NULL REFERENCES roles(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, role_id)
);

CREATE TABLE role_permissions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    role_id UUID NOT NULL REFERENCES roles(id),
    permission_id UUID NOT NULL REFERENCES permissions(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(role_id, permission_id)
);

-- KYC and Compliance
CREATE TABLE kyc_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    document_type VARCHAR(50) NOT NULL,
    document_number VARCHAR(100),
    issuing_country VARCHAR(2),
    expiry_date DATE,
    document_url VARCHAR(500),
    verification_status compliance_status DEFAULT 'PENDING',
    verified_at TIMESTAMP,
    verified_by UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE compliance_checks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    check_type VARCHAR(50) NOT NULL,
    check_result compliance_status DEFAULT 'PENDING',
    risk_score INTEGER DEFAULT 0,
    details JSONB,
    performed_at TIMESTAMP,
    performed_by UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

-- Banking Accounts
CREATE TABLE accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    account_type account_type NOT NULL,
    currency VARCHAR(3) NOT NULL,
    iban VARCHAR(34),
    account_number VARCHAR(50),
    routing_number VARCHAR(50),
    balance DECIMAL(20,8) DEFAULT 0,
    available_balance DECIMAL(20,8) DEFAULT 0,
    blocked_balance DECIMAL(20,8) DEFAULT 0,
    account_status account_status DEFAULT 'PENDING',
    partner_account_id VARCHAR(255),
    partner_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

-- Crypto Wallets
CREATE TABLE crypto_wallets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    asset_type crypto_asset_type NOT NULL,
    wallet_address VARCHAR(255),
    balance DECIMAL(20,8) DEFAULT 0,
    staked_balance DECIMAL(20,8) DEFAULT 0,
    custody_provider VARCHAR(100),
    wallet_id VARCHAR(255),
    is_hot_wallet BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

-- Transactions
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    account_id UUID REFERENCES accounts(id),
    crypto_wallet_id UUID REFERENCES crypto_wallets(id),
    transaction_type transaction_type NOT NULL,
    amount DECIMAL(20,8) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    fee_amount DECIMAL(20,8) DEFAULT 0,
    fee_currency VARCHAR(10),
    exchange_rate DECIMAL(20,8),
    reference VARCHAR(255),
    description TEXT,
    status transaction_status DEFAULT 'PENDING',
    partner_transaction_id VARCHAR(255),
    metadata JSONB,
    processed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

-- Cards
CREATE TABLE cards (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    account_id UUID REFERENCES accounts(id),
    card_type card_type NOT NULL,
    card_number_hash VARCHAR(255),
    last_four_digits VARCHAR(4),
    expiry_month INTEGER,
    expiry_year INTEGER,
    cvv_hash VARCHAR(255),
    card_status card_status DEFAULT 'PENDING',
    spending_limit DECIMAL(15,2),
    daily_limit DECIMAL(15,2),
    monthly_limit DECIMAL(15,2),
    partner_card_id VARCHAR(255),
    partner_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

-- Investment Accounts
CREATE TABLE investment_accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    account_type VARCHAR(50) DEFAULT 'STANDARD',
    total_value DECIMAL(15,2) DEFAULT 0,
    cash_balance DECIMAL(15,2) DEFAULT 0,
    broker_account_id VARCHAR(255),
    broker_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

-- Stock Holdings
CREATE TABLE stock_holdings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    investment_account_id UUID NOT NULL REFERENCES investment_accounts(id),
    symbol VARCHAR(20) NOT NULL,
    quantity DECIMAL(10,4) NOT NULL,
    average_cost DECIMAL(10,2),
    current_value DECIMAL(15,2),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

-- Notifications
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    notification_type notification_type NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    read_at TIMESTAMP,
    sent_at TIMESTAMP,
    delivery_status VARCHAR(50) DEFAULT 'PENDING',
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE
);

-- Audit Log
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    action VARCHAR(100) NOT NULL,
    resource_type VARCHAR(100),
    resource_id UUID,
    details JSONB,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_kyc_status ON users(kyc_status);
CREATE INDEX idx_users_user_status ON users(user_status);

CREATE INDEX idx_accounts_user_id ON accounts(user_id);
CREATE INDEX idx_accounts_iban ON accounts(iban);
CREATE INDEX idx_accounts_account_status ON accounts(account_status);

CREATE INDEX idx_crypto_wallets_user_id ON crypto_wallets(user_id);
CREATE INDEX idx_crypto_wallets_asset_type ON crypto_wallets(asset_type);

CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);

CREATE INDEX idx_cards_user_id ON cards(user_id);
CREATE INDEX idx_cards_account_id ON cards(account_id);
CREATE INDEX idx_cards_card_status ON cards(card_status);

CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_read_at ON notifications(read_at);

CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);

-- Insert default roles and permissions
INSERT INTO roles (name, description) VALUES
('USER', 'Standard user with basic access'),
('PREMIUM', 'Premium user with enhanced features'),
('BUSINESS', 'Business account holder'),
('ADMIN', 'System administrator'),
('COMPLIANCE', 'Compliance officer'),
('SUPPORT', 'Customer support representative');

INSERT INTO permissions (name, description, resource, action) VALUES
('READ_OWN_ACCOUNT', 'Read own account information', 'ACCOUNT', 'READ'),
('WRITE_OWN_ACCOUNT', 'Modify own account information', 'ACCOUNT', 'WRITE'),
('READ_OWN_TRANSACTIONS', 'Read own transaction history', 'TRANSACTION', 'READ'),
('CREATE_TRANSFER', 'Create money transfers', 'TRANSFER', 'CREATE'),
('READ_CRYPTO_WALLET', 'Read crypto wallet information', 'CRYPTO_WALLET', 'READ'),
('TRADE_CRYPTO', 'Buy and sell cryptocurrency', 'CRYPTO', 'TRADE'),
('MANAGE_CARDS', 'Manage debit and credit cards', 'CARD', 'MANAGE'),
('READ_ALL_ACCOUNTS', 'Read all user accounts', 'ACCOUNT', 'READ_ALL'),
('MANAGE_USERS', 'Manage user accounts', 'USER', 'MANAGE'),
('REVIEW_KYC', 'Review KYC applications', 'KYC', 'REVIEW'),
('VIEW_AUDIT_LOGS', 'View system audit logs', 'AUDIT_LOG', 'READ');

-- Assign permissions to roles
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'USER' AND p.name IN ('READ_OWN_ACCOUNT', 'WRITE_OWN_ACCOUNT', 'READ_OWN_TRANSACTIONS', 'CREATE_TRANSFER');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'PREMIUM' AND p.name IN ('READ_OWN_ACCOUNT', 'WRITE_OWN_ACCOUNT', 'READ_OWN_TRANSACTIONS', 'CREATE_TRANSFER', 'READ_CRYPTO_WALLET', 'TRADE_CRYPTO', 'MANAGE_CARDS');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'ADMIN' AND p.name IN ('READ_ALL_ACCOUNTS', 'MANAGE_USERS', 'VIEW_AUDIT_LOGS');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'COMPLIANCE' AND p.name IN ('REVIEW_KYC', 'READ_ALL_ACCOUNTS', 'VIEW_AUDIT_LOGS');

-- Create a default admin user (password: Admin123!)
INSERT INTO users (email, password_hash, first_name, last_name, kyc_status, user_status, email_verified, phone_verified)
VALUES ('admin@neobridge.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'System', 'Administrator', 'APPROVED', 'ACTIVE', true, true);

-- Assign admin role to default admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email = 'admin@neobridge.com' AND r.name = 'ADMIN';
