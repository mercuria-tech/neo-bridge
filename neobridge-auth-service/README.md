# 🔐 NeoBridge Authentication Service

**Enterprise-grade authentication and authorization service with OAuth 2.1 and advanced security features**

---

## 🎯 **Service Overview**

The **NeoBridge Authentication Service** is the security cornerstone of the platform, providing comprehensive authentication, authorization, and identity management capabilities. Built with enterprise-grade security standards, it supports OAuth 2.1, JWT tokens, multi-factor authentication, and advanced security features.

### **🌟 Key Features**
- **OAuth 2.1 Authorization Server** (latest security standard)
- **JWT token management** with secure signing (RS256)
- **Multi-factor authentication (MFA)** support
- **Role-based access control (RBAC)** with fine-grained permissions
- **Biometric authentication** integration
- **Advanced security monitoring** and threat detection
- **Compliance ready** (PCI DSS, SOC 2, ISO 27001)

---

## 🏗️ **Service Architecture**

```
neobridge-auth-service/
├── src/main/java/com/neobridge/auth/
│   ├── controller/        # REST API controllers
│   ├── service/          # Business logic services
│   ├── repository/       # Data access layer
│   ├── entity/           # Domain entities
│   ├── dto/              # Data transfer objects
│   ├── config/           # Security configurations
│   ├── security/         # Security implementations
│   └── util/             # Utility classes
├── src/main/resources/   # Configuration files
├── pom.xml              # Maven configuration
└── README.md            # This file
```

---

## 🔒 **Security Features**

### **🔐 OAuth 2.1 Implementation**
- **Authorization Code Flow** with PKCE
- **Client Credentials Flow** for service-to-service
- **Resource Owner Password Flow** (legacy support)
- **Device Authorization Flow** for IoT devices
- **Token introspection** and revocation

### **🛡️ JWT Token Management**
- **RS256 signing** with 4096-bit RSA keys
- **Token refresh** mechanism
- **Token blacklisting** for security
- **Short-lived access tokens** (15 minutes)
- **Long-lived refresh tokens** (7 days)

### **🔑 Multi-Factor Authentication**
- **TOTP (Time-based One-Time Password)** support
- **SMS verification** for mobile devices
- **Email verification** for account recovery
- **Biometric authentication** (fingerprint, face ID)
- **Hardware security keys** (FIDO2/U2F)

### **👥 Role-Based Access Control**
- **Fine-grained permissions** system
- **Dynamic role assignment** based on user attributes
- **Permission inheritance** and delegation
- **Context-aware access control** (time, location, device)
- **Audit logging** for all access decisions

---

## 🚀 **Quick Start**

### **📋 Prerequisites**
- Java 21 JDK
- Maven 3.9+
- PostgreSQL 16
- Redis 7.2
- NeoBridge Common Module

### **🔧 Installation & Setup**
```bash
# Navigate to auth service
cd neobridge-auth-service

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
    url: jdbc:postgresql://localhost:5432/neobridge_auth
    username: neobridge_user
    password: neobridge_password
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

neobridge:
  auth:
    jwt:
      secret: your-rsa-private-key
      expiration: 900000  # 15 minutes
      refresh-expiration: 604800000  # 7 days
    mfa:
      enabled: true
      totp-issuer: NeoBridge
    security:
      password-encoder: bcrypt
      max-login-attempts: 5
      lockout-duration: 900000  # 15 minutes
```

---

## 💻 **API Endpoints**

### **🔐 Authentication Endpoints**
```http
# User Registration
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123!",
  "fullName": "John Doe",
  "phoneNumber": "+1234567890"
}

# User Login
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123!"
}

# OAuth 2.1 Authorization
GET /oauth2/authorize?response_type=code&client_id=client&redirect_uri=callback&scope=read write

# Token Endpoint
POST /oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&code=code&redirect_uri=callback&client_id=client&client_secret=secret
```

### **👤 User Management Endpoints**
```http
# Get User Profile
GET /api/v1/users/profile
Authorization: Bearer {access_token}

# Update User Profile
PUT /api/v1/users/profile
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "fullName": "John Smith",
  "phoneNumber": "+1234567890"
}

# Change Password
POST /api/v1/users/change-password
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "currentPassword": "OldPassword123!",
  "newPassword": "NewPassword123!"
}
```

### **🔑 MFA Endpoints**
```http
# Enable TOTP
POST /api/v1/mfa/totp/enable
Authorization: Bearer {access_token}

# Verify TOTP
POST /api/v1/mfa/totp/verify
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "code": "123456"
}

# Disable MFA
POST /api/v1/mfa/disable
Authorization: Bearer {access_token}
```

---

## 🏗️ **Data Models**

### **👤 User Entity**
```java
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "email_verified")
    private boolean emailVerified = false;
    
    @Column(name = "phone_verified")
    private boolean phoneVerified = false;
    
    @Column(name = "mfa_enabled")
    private boolean mfaEnabled = false;
    
    @Column(name = "account_locked")
    private boolean accountLocked = false;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
```

### **🎭 Role Entity**
```java
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
}
```

### **🔓 Permission Entity**
```java
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {
    
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    
    @Column(name = "resource", nullable = false)
    private String resource;
    
    @Column(name = "action", nullable = false)
    private String action;
    
    @Column(name = "description")
    private String description;
}
```

---

## 🔧 **Configuration Options**

### **🔐 JWT Configuration**
```yaml
neobridge:
  auth:
    jwt:
      # Token settings
      access-token-expiration: 900000      # 15 minutes
      refresh-token-expiration: 604800000  # 7 days
      issuer: NeoBridge
      audience: NeoBridge-Users
      
      # Key management
      key-store-path: classpath:keystore.p12
      key-store-password: ${KEYSTORE_PASSWORD}
      key-alias: neobridge-auth
      key-password: ${KEY_PASSWORD}
      
      # Security settings
      require-https: true
      allow-multiple-sessions: false
      session-timeout: 1800000  # 30 minutes
```

### **🛡️ Security Configuration**
```yaml
neobridge:
  auth:
    security:
      # Password policy
      password:
        min-length: 8
        require-uppercase: true
        require-lowercase: true
        require-numbers: true
        require-special-chars: true
        max-age-days: 90
      
      # Account lockout
      lockout:
        max-attempts: 5
        lockout-duration: 900000  # 15 minutes
        progressive-delay: true
        
      # Session management
      session:
        max-sessions: 1
        concurrent-sessions: false
        session-timeout: 1800000  # 30 minutes
```

### **📱 MFA Configuration**
```yaml
neobridge:
  auth:
    mfa:
      # TOTP settings
      totp:
        enabled: true
        issuer: NeoBridge
        algorithm: SHA1
        digits: 6
        period: 30
        
      # SMS settings
      sms:
        enabled: true
        provider: twilio
        template: "Your NeoBridge verification code is: {code}"
        
      # Email settings
      email:
        enabled: true
        provider: sendgrid
        template: "verification-code"
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

### **🔄 End-to-End Testing**
```bash
# Run E2E tests
mvn test -Dtest=*E2ETest

# Run with specific profile
mvn test -Dspring.profiles.active=test
```

---

## 📊 **Monitoring & Observability**

### **📈 Metrics**
- **Authentication attempts** (success/failure)
- **Token generation** and validation
- **MFA usage** statistics
- **Security events** and alerts
- **Performance metrics** (response times, throughput)

### **🔍 Health Checks**
```http
# Health check endpoint
GET /actuator/health

# Detailed health information
GET /actuator/health/auth

# Security health check
GET /actuator/health/security
```

### **📝 Logging**
- **Structured logging** with JSON format
- **Security event logging** for audit trails
- **Performance logging** for optimization
- **Error logging** with stack traces

---

## 🔒 **Security Best Practices**

### **🔐 Password Security**
- **Bcrypt hashing** with cost factor 12
- **Salt generation** for each password
- **Password history** to prevent reuse
- **Complexity requirements** enforcement
- **Regular password rotation** reminders

### **🛡️ Token Security**
- **Short-lived access tokens** (15 minutes)
- **Secure token storage** (httpOnly cookies)
- **Token rotation** on privilege escalation
- **Token binding** to device fingerprints
- **Automatic token revocation** on logout

### **🔑 Key Management**
- **Hardware Security Module (HSM)** integration
- **Key rotation** policies
- **Secure key storage** (Vault, AWS KMS)
- **Key backup** and recovery procedures
- **Key escrow** for compliance

---

## 📋 **Compliance Features**

### **🔍 Audit Logging**
- **User authentication** events
- **Authorization decisions** logging
- **Security policy** changes
- **Access pattern** analysis
- **Compliance reporting** generation

### **📊 Data Protection**
- **Personal data encryption** at rest
- **Data anonymization** for analytics
- **Data retention** policies
- **Data portability** support
- **Right to be forgotten** implementation

---

## 🚀 **Deployment**

### **🐳 Docker Deployment**
```dockerfile
FROM openjdk:21-jdk-slim

COPY target/neobridge-auth-service.jar app.jar
COPY keystore.p12 /app/keystore.p12

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

### **☸️ Kubernetes Deployment**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: neobridge-auth-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: neobridge-auth-service
  template:
    metadata:
      labels:
        app: neobridge-auth-service
    spec:
      containers:
      - name: auth-service
        image: neobridge/auth-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: auth-db-secret
              key: url
```

---

## 📚 **Documentation**

### **📖 API Documentation**
- **OpenAPI 3.0** specification
- **Swagger UI** integration
- **Postman collections** for testing
- **Code examples** in multiple languages

### **🔍 Security Documentation**
- **Security architecture** overview
- **Threat model** and risk assessment
- **Security controls** implementation
- **Incident response** procedures

---

## 🤝 **Contributing**

### **📝 Development Standards**
- Follow **Spring Security best practices**
- Implement **comprehensive security testing**
- Maintain **security documentation**
- Follow **OWASP guidelines**
- Regular **security code reviews**

---

## 📞 **Support**

### **👥 Security Team**
- **Security Lead**: Available for security questions
- **Incident Response**: 24/7 security support
- **Compliance Team**: Regulatory compliance support

---

## 📄 **License**

This service is part of the **NeoBridge platform** and is proprietary software developed by **Mercuria Technologies** for **Harmony Q&Q GmbH**.

---

## 🏁 **Conclusion**

The **NeoBridge Authentication Service** provides enterprise-grade security for the entire platform, ensuring that all user interactions are secure, compliant, and auditable. Built with modern security standards and best practices, it's ready for production deployment in regulated financial environments.

**Secure authentication for the future of banking! 🔐🚀**

---

<div align="center">

**Part of the NeoBridge Platform**  
**Built by Mercuria Technologies for Harmony Q&Q GmbH**

</div>
