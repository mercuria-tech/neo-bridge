# 🔐 NeoBridge Authentication Service - Complete API Documentation

**Comprehensive API documentation for the NeoBridge Authentication Service with OAuth 2.1 and advanced security features**

---

## 🎯 **Authentication Service Overview**

The **NeoBridge Authentication Service** provides enterprise-grade authentication and authorization capabilities through **OAuth 2.1** implementation, **JWT token management**, and **multi-factor authentication (MFA)**. This service is the security cornerstone of the entire NeoBridge platform.

### **🌟 Service Features**
- **OAuth 2.1 Authorization Server** with PKCE support
- **JWT token management** with RS256 signing
- **Multi-factor authentication** (TOTP, SMS, Email, Biometric)
- **Role-based access control** with fine-grained permissions
- **Advanced security monitoring** and threat detection
- **Compliance ready** (PCI DSS, SOC 2, ISO 27001)

---

## 🔑 **Authentication Endpoints**

### **📝 User Registration**

#### **POST** `/api/v1/auth/register`
**Create a new user account with comprehensive validation**

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123!",
  "fullName": "John Doe",
  "phoneNumber": "+1234567890",
  "dateOfBirth": "1990-01-15",
  "nationality": "US",
  "countryOfResidence": "US",
  "acceptTerms": true,
  "marketingConsent": false
}
```

**Response (Success - 201 Created):**
```json
{
  "success": true,
  "data": {
    "userId": "user_abc123",
    "email": "user@example.com",
    "fullName": "John Doe",
    "status": "PENDING_VERIFICATION",
    "verificationRequired": ["EMAIL", "PHONE"],
    "message": "Account created successfully. Please verify your email and phone number."
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

**Response (Error - 400 Bad Request):**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid input data",
    "details": [
      {
        "field": "email",
        "message": "Email format is invalid"
      },
      {
        "field": "password",
        "message": "Password must be at least 8 characters with uppercase, lowercase, number, and special character"
      }
    ]
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

**Validation Rules:**
- **Email**: Must be valid email format and unique
- **Password**: Minimum 8 characters, must include uppercase, lowercase, number, and special character
- **Phone Number**: Must be valid international format
- **Date of Birth**: Must be 18+ years old
- **Nationality**: Must be valid ISO 3166-1 alpha-3 country code

---

### **🔐 User Login**

#### **POST** `/api/v1/auth/login`
**Authenticate user and obtain access tokens**

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123!",
  "deviceInfo": {
    "deviceId": "dev_abc123",
    "deviceType": "MOBILE",
    "os": "iOS 17.0",
    "appVersion": "1.0.0"
  },
  "rememberMe": false
}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 900,
    "refreshExpiresIn": 604800,
    "user": {
      "userId": "user_abc123",
      "email": "user@example.com",
      "fullName": "John Doe",
      "status": "ACTIVE",
      "mfaEnabled": true,
      "lastLogin": "2024-01-15T10:30:00Z"
    },
    "permissions": [
      "account:read",
      "account:write",
      "payment:read",
      "payment:write"
    ]
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

**Response (Error - 401 Unauthorized):**
```json
{
  "success": false,
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "Invalid email or password",
    "details": {
      "remainingAttempts": 4,
      "lockoutTime": null
    }
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **🔄 Token Refresh**

#### **POST** `/api/v1/auth/refresh`
**Refresh expired access token using refresh token**

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "deviceId": "dev_abc123"
}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 900,
    "refreshExpiresIn": 604800
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **🚪 User Logout**

#### **POST** `/api/v1/auth/logout`
**Logout user and invalidate tokens**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request Body:**
```json
{
  "deviceId": "dev_abc123",
  "logoutAllDevices": false
}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "message": "Successfully logged out",
    "loggedOutDevices": 1
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

## 🔐 **OAuth 2.1 Endpoints**

### **🔑 Authorization Endpoint**

#### **GET** `/oauth2/authorize`
**OAuth 2.1 authorization endpoint with PKCE support**

**Query Parameters:**
```
response_type=code
client_id={client_id}
redirect_uri={redirect_uri}
scope={scopes}
state={state}
code_challenge={code_challenge}
code_challenge_method=S256
```

**Example Request:**
```
GET /oauth2/authorize?response_type=code&client_id=web_app&redirect_uri=https://app.neobridge.com/callback&scope=read write&state=abc123&code_challenge=E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM&code_challenge_method=S256
```

**Response:**
- **User not authenticated**: Redirects to login page
- **User authenticated**: Shows consent screen
- **User consents**: Redirects to redirect_uri with authorization code

---

### **🎫 Token Endpoint**

#### **POST** `/oauth2/token`
**Exchange authorization code for access token**

**Request Body (Authorization Code Flow):**
```json
{
  "grant_type": "authorization_code",
  "code": "authorization_code_here",
  "redirect_uri": "https://app.neobridge.com/callback",
  "client_id": "web_app",
  "code_verifier": "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"
}
```

**Response (Success - 200 OK):**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 900,
  "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "scope": "read write",
  "user_id": "user_abc123"
}
```

---

## 🔑 **Multi-Factor Authentication (MFA)**

### **📱 Enable TOTP**

#### **POST** `/api/v1/mfa/totp/enable`
**Enable Time-based One-Time Password (TOTP) for MFA**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "secret": "JBSWY3DPEHPK3PXP",
    "qrCodeUrl": "otpauth://totp/NeoBridge:user@example.com?secret=JBSWY3DPEHPK3PXP&issuer=NeoBridge",
    "backupCodes": [
      "12345678",
      "87654321",
      "11223344",
      "44332211"
    ],
    "message": "TOTP enabled successfully. Scan the QR code with your authenticator app."
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **✅ Verify TOTP**

#### **POST** `/api/v1/mfa/totp/verify`
**Verify TOTP code during login or sensitive operations**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request Body:**
```json
{
  "code": "123456",
  "operation": "LOGIN"
}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "verified": true,
    "message": "TOTP verification successful"
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **📧 Enable Email MFA**

#### **POST** `/api/v1/mfa/email/enable`
**Enable email-based MFA**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "message": "Verification code sent to your email",
    "expiresIn": 300
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

## 👤 **User Management Endpoints**

### **👤 Get User Profile**

#### **GET** `/api/v1/users/profile`
**Retrieve current user's profile information**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "userId": "user_abc123",
    "email": "user@example.com",
    "fullName": "John Doe",
    "phoneNumber": "+1234567890",
    "dateOfBirth": "1990-01-15",
    "nationality": "US",
    "countryOfResidence": "US",
    "emailVerified": true,
    "phoneVerified": true,
    "mfaEnabled": true,
    "accountStatus": "ACTIVE",
    "lastLogin": "2024-01-15T10:30:00Z",
    "createdAt": "2024-01-01T00:00:00Z",
    "profile": {
      "profileType": "INDIVIDUAL",
      "occupation": "Software Engineer",
      "employer": "Tech Corp",
      "annualIncome": 75000.00,
      "riskTolerance": "MODERATE",
      "investmentExperience": "INTERMEDIATE"
    }
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **✏️ Update User Profile**

#### **PUT** `/api/v1/users/profile`
**Update user profile information**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request Body:**
```json
{
  "fullName": "John Smith",
  "phoneNumber": "+1234567899",
  "profile": {
    "occupation": "Senior Software Engineer",
    "employer": "Tech Corp Inc",
    "annualIncome": 85000.00
  }
}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "message": "Profile updated successfully",
    "updatedFields": ["fullName", "phoneNumber", "profile.occupation", "profile.employer", "profile.annualIncome"]
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **🔒 Change Password**

#### **POST** `/api/v1/users/change-password`
**Change user password with current password verification**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request Body:**
```json
{
  "currentPassword": "SecurePassword123!",
  "newPassword": "NewSecurePassword456!",
  "confirmPassword": "NewSecurePassword456!"
}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "message": "Password changed successfully",
    "passwordChangedAt": "2024-01-15T10:30:00Z"
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

## 🔍 **Security & Monitoring Endpoints**

### **📊 Get User Sessions**

#### **GET** `/api/v1/users/sessions`
**Retrieve all active user sessions**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "sessions": [
      {
        "sessionId": "sess_abc123",
        "deviceInfo": {
          "deviceId": "dev_abc123",
          "deviceType": "MOBILE",
          "os": "iOS 17.0",
          "appVersion": "1.0.0"
        },
        "ipAddress": "192.168.1.100",
        "userAgent": "NeoBridge/1.0.0 (iPhone; iOS 17.0)",
        "lastActivity": "2024-01-15T10:30:00Z",
        "createdAt": "2024-01-15T09:00:00Z",
        "isCurrent": true
      },
      {
        "sessionId": "sess_def456",
        "deviceInfo": {
          "deviceId": "dev_def456",
          "deviceType": "DESKTOP",
          "os": "Windows 11",
          "browser": "Chrome 120.0"
        },
        "ipAddress": "192.168.1.101",
        "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
        "lastActivity": "2024-01-15T08:00:00Z",
        "createdAt": "2024-01-15T07:00:00Z",
        "isCurrent": false
      }
    ],
    "totalSessions": 2,
    "activeSessions": 2
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **❌ Revoke Session**

#### **DELETE** `/api/v1/users/sessions/{sessionId}`
**Revoke a specific user session**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "message": "Session revoked successfully",
    "revokedSessionId": "sess_def456"
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

### **🔍 Get Security Events**

#### **GET** `/api/v1/users/security-events`
**Retrieve user security events and suspicious activities**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Query Parameters:**
```
?page=0&size=20&type=LOGIN_ATTEMPT&startDate=2024-01-01&endDate=2024-01-15
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "data": {
    "events": [
      {
        "eventId": "evt_abc123",
        "eventType": "LOGIN_ATTEMPT",
        "timestamp": "2024-01-15T10:30:00Z",
        "ipAddress": "192.168.1.100",
        "deviceInfo": {
          "deviceType": "MOBILE",
          "os": "iOS 17.0"
        },
        "location": "New York, US",
        "status": "SUCCESS",
        "riskScore": 0.1
      },
      {
        "eventId": "evt_def456",
        "eventType": "LOGIN_ATTEMPT",
        "timestamp": "2024-01-15T09:00:00Z",
        "ipAddress": "203.0.113.1",
        "deviceInfo": {
          "deviceType": "DESKTOP",
          "os": "Unknown"
        },
        "location": "Unknown",
        "status": "FAILED",
        "riskScore": 0.8,
        "reason": "Invalid credentials"
      }
    ],
    "pagination": {
      "page": 0,
      "size": 20,
      "totalElements": 45,
      "totalPages": 3
    }
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

## 🔐 **OAuth 2.1 Client Management**

### **📱 Register OAuth Client**

#### **POST** `/api/v1/oauth/clients`
**Register a new OAuth 2.1 client application**

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request Body:**
```json
{
  "clientName": "NeoBridge Mobile App",
  "clientType": "PUBLIC",
  "redirectUris": [
    "https://app.neobridge.com/callback",
    "com.neobridge.app://callback"
  ],
  "scopes": ["read", "write", "payment"],
  "grantTypes": ["authorization_code", "refresh_token"],
  "responseTypes": ["code"],
  "requirePkce": true
}
```

**Response (Success - 201 Created):**
```json
{
  "success": true,
  "data": {
    "clientId": "mobile_app_abc123",
    "clientSecret": "secret_xyz789",
    "clientName": "NeoBridge Mobile App",
    "clientType": "PUBLIC",
    "redirectUris": [
      "https://app.neobridge.com/callback",
      "com.neobridge.app://callback"
    ],
    "scopes": ["read", "write", "payment"],
    "grantTypes": ["authorization_code", "refresh_token"],
    "responseTypes": ["code"],
    "requirePkce": true,
    "status": "ACTIVE",
    "createdAt": "2024-01-15T10:30:00Z"
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

---

## 📊 **Health & Monitoring Endpoints**

### **❤️ Service Health**

#### **GET** `/actuator/health`
**Check authentication service health status**

**Response (Success - 200 OK):**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "SELECT 1"
      }
    },
    "redis": {
      "status": "UP",
      "details": {
        "version": "7.2.0"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 107374182400,
        "free": 53687091200,
        "threshold": 10485760
      }
    }
  }
}
```

---

### **📈 Service Metrics**

#### **GET** `/actuator/metrics`
**Retrieve service performance metrics**

**Response (Success - 200 OK):**
```json
{
  "names": [
    "http.server.requests",
    "jvm.memory.used",
    "jvm.threads.live",
    "process.cpu.usage",
    "authentication.attempts",
    "authentication.successes",
    "authentication.failures",
    "token.generated",
    "token.validated",
    "mfa.verifications"
  ]
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
      "timestamp": "Error occurrence time",
      "requestId": "Unique request identifier"
    }
  },
  "timestamp": "2024-01-15T10:30:00Z",
  "requestId": "req_abc123"
}
```

### **🔍 Common Error Codes**

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| `VALIDATION_ERROR` | 400 | Input validation failed |
| `INVALID_CREDENTIALS` | 401 | Invalid email/password |
| `UNAUTHORIZED` | 401 | Missing or invalid token |
| `FORBIDDEN` | 403 | Insufficient permissions |
| `RESOURCE_NOT_FOUND` | 404 | Requested resource not found |
| `ACCOUNT_LOCKED` | 423 | Account temporarily locked |
| `MFA_REQUIRED` | 428 | Multi-factor authentication required |
| `RATE_LIMIT_EXCEEDED` | 429 | Too many requests |
| `INTERNAL_SERVER_ERROR` | 500 | Internal server error |

---

## 🔒 **Security Considerations**

### **🛡️ Security Headers**
All responses include security headers:
```
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
Strict-Transport-Security: max-age=31536000; includeSubDomains
Content-Security-Policy: default-src 'self'
```

### **🔐 Token Security**
- **Access Token**: 15-minute expiration (900 seconds)
- **Refresh Token**: 7-day expiration (604,800 seconds)
- **JWT Signing**: RS256 with 4096-bit RSA keys
- **Token Storage**: Secure HTTP-only cookies
- **Token Rotation**: Automatic refresh token rotation

### **📱 MFA Security**
- **TOTP**: 6-digit codes, 30-second validity
- **Backup Codes**: 8-character alphanumeric codes
- **Rate Limiting**: Maximum 5 attempts per 15 minutes
- **Device Binding**: Tokens bound to specific devices
- **Audit Logging**: Complete MFA event logging

---

## 🚀 **Rate Limiting**

### **📊 Rate Limit Headers**
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1642233600
X-RateLimit-Reset-Time: 2024-01-15T11:00:00Z
```

### **⚡ Rate Limit Rules**
- **Authentication Endpoints**: 5 requests per minute
- **User Management**: 100 requests per hour
- **OAuth Endpoints**: 10 requests per minute
- **MFA Endpoints**: 10 requests per minute
- **General API**: 1000 requests per hour

---

## 🏁 **Conclusion**

The **NeoBridge Authentication Service API** provides comprehensive authentication and authorization capabilities with enterprise-grade security features. Built on OAuth 2.1 standards with advanced MFA support, it ensures secure access to all platform services.

**Key API strengths:**
- **Complete OAuth 2.1 implementation** with PKCE support
- **Advanced MFA capabilities** (TOTP, SMS, Email, Biometric)
- **Comprehensive security monitoring** and threat detection
- **Enterprise-grade token management** with automatic rotation
- **Detailed audit logging** for compliance and security

**Ready to secure the future of banking! 🔐🚀**

---

<div align="center">

**NeoBridge Authentication Service APIs**  
**Built by Mercuria Technologies for Harmony Q&Q GmbH**

</div>
