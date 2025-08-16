# 🧩 NeoBridge Common Module

**Shared utilities, base classes, and common components for the NeoBridge platform**

---

## 🎯 **Module Overview**

The **NeoBridge Common Module** serves as the foundation for all other services in the platform. It provides shared utilities, base entities, common responses, and standardized components that ensure consistency across the entire microservices architecture.

### **🌟 Purpose**
- **Centralized utilities** for common operations
- **Standardized base classes** for entities and responses
- **Shared constants** and enums across services
- **Common exception handling** and error responses
- **Reusable components** to reduce code duplication

---

## 🏗️ **Module Structure**

```
neobridge-common/
├── src/main/java/com/neobridge/common/
│   ├── entity/           # Base entity classes
│   ├── response/         # Standardized API responses
│   ├── exception/        # Common exceptions
│   ├── util/            # Utility classes
│   ├── constant/        # Constants and enums
│   ├── config/          # Common configurations
│   └── annotation/      # Custom annotations
├── src/main/resources/   # Common resources
├── pom.xml              # Maven configuration
└── README.md            # This file
```

---

## 🔧 **Core Components**

### **🏗️ Base Entity Classes**
- **`BaseEntity`**: Abstract base class for all entities
- **`AuditableEntity`**: Base class with audit fields
- **`SoftDeleteEntity`**: Base class with soft delete capability

### **📡 API Response Classes**
- **`ApiResponse<T>`**: Standardized API response wrapper
- **`ApiError`**: Standardized error response
- **`PagedResponse<T>`**: Paginated response wrapper
- **`SuccessResponse`**: Success response without data

### **⚠️ Exception Classes**
- **`NeoBridgeException`**: Base exception class
- **`ValidationException`**: Data validation exceptions
- **`BusinessException`**: Business logic exceptions
- **`SecurityException`**: Security-related exceptions

### **🛠️ Utility Classes**
- **`DateUtils`**: Date and time utilities
- **`StringUtils`**: String manipulation utilities
- **`EncryptionUtils`**: Encryption and hashing utilities
- **`ValidationUtils`**: Data validation utilities

### **📋 Constants and Enums**
- **`CommonConstants`**: Platform-wide constants
- **`StatusEnum`**: Common status values
- **`CurrencyEnum`**: Supported currencies
- **`CountryEnum`**: Supported countries

---

## 🚀 **Quick Start**

### **📋 Prerequisites**
- Java 21 JDK
- Maven 3.9+
- Access to NeoBridge parent POM

### **🔧 Installation**
```bash
# Navigate to common module
cd neobridge-common

# Install to local Maven repository
mvn clean install
```

### **📦 Usage in Other Services**
```xml
<!-- Add dependency to service pom.xml -->
<dependency>
    <groupId>com.neobridge</groupId>
    <artifactId>neobridge-common</artifactId>
    <version>${project.version}</version>
</dependency>
```

---

## 💻 **Usage Examples**

### **🏗️ Extending Base Entity**
```java
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    // Getters and setters
}
```

### **📡 Using Standard API Response**
```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ApiResponse.success(user);
    }
    
    @PostMapping("/users")
    public ApiResponse<User> createUser(@RequestBody User user) {
        User createdUser = userService.create(user);
        return ApiResponse.success(createdUser, "User created successfully");
    }
}
```

### **⚠️ Custom Exception Handling**
```java
@Service
public class UserService {
    
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BusinessException(
                "User not found with ID: " + id,
                ErrorCode.USER_NOT_FOUND
            ));
    }
}
```

### **🛠️ Using Utility Classes**
```java
@Service
public class ValidationService {
    
    public void validateEmail(String email) {
        if (!ValidationUtils.isValidEmail(email)) {
            throw new ValidationException("Invalid email format");
        }
    }
    
    public void validatePassword(String password) {
        if (!ValidationUtils.isStrongPassword(password)) {
            throw new ValidationException("Password does not meet security requirements");
        }
    }
}
```

---

## 🔒 **Security Features**

### **🔐 Encryption Utilities**
- **AES-256-GCM** encryption for sensitive data
- **SHA-256** hashing for passwords
- **Secure random** generation for tokens
- **Key derivation** functions (PBKDF2)

### **🛡️ Validation Utilities**
- **Input sanitization** to prevent injection attacks
- **Data validation** with comprehensive rules
- **Security validation** for sensitive operations
- **Rate limiting** support utilities

---

## 📊 **Performance Optimizations**

### **⚡ Caching Support**
- **Cache annotations** for method results
- **Cache key generation** utilities
- **Cache eviction** strategies
- **Distributed cache** support

### **🔄 Connection Pooling**
- **Database connection** pooling utilities
- **HTTP client** connection pooling
- **Resource management** utilities
- **Connection monitoring** tools

---

## 🧪 **Testing Support**

### **🧩 Test Utilities**
- **Test data builders** for entities
- **Mock response generators** for APIs
- **Test constants** and fixtures
- **Integration test** helpers

### **📋 Test Annotations**
- **`@TestConfiguration`**: Test-specific configurations
- **`@MockBean`**: Mock bean definitions
- **`@TestPropertySource`**: Test property sources
- **`@Transactional`**: Test transaction management

---

## 🔧 **Configuration**

### **⚙️ Common Properties**
```yaml
# Common configuration properties
neobridge:
  common:
    encryption:
      algorithm: AES-256-GCM
      key-size: 256
    validation:
      password:
        min-length: 8
        require-uppercase: true
        require-lowercase: true
        require-numbers: true
        require-special-chars: true
    audit:
      enabled: true
      track-changes: true
```

### **🔧 Environment-Specific Configs**
- **Development**: Local development settings
- **Testing**: Test environment configurations
- **Staging**: Pre-production settings
- **Production**: Live environment configs

---

## 📚 **Documentation**

### **📖 API Documentation**
- **JavaDoc** for all public classes and methods
- **Usage examples** in code comments
- **Best practices** documentation
- **Migration guides** for version updates

### **🔍 Code Examples**
- **Integration examples** for each component
- **Configuration examples** for different scenarios
- **Testing examples** for quality assurance
- **Performance examples** for optimization

---

## 🤝 **Contributing**

### **📝 Development Standards**
- Follow **Java coding conventions**
- Use **Spring Boot best practices**
- Implement **comprehensive testing**
- Maintain **backward compatibility**
- Document **all public APIs**

### **🔍 Code Review Process**
- All changes require **peer review**
- **Automated testing** must pass
- **Documentation updates** required
- **Performance impact** assessment

---

## 📊 **Dependencies**

### **🔗 Internal Dependencies**
- **Spring Boot Starter**: Core Spring Boot functionality
- **Spring Boot Starter Web**: Web application support
- **Spring Boot Starter Data JPA**: JPA and database support
- **Spring Boot Starter Validation**: Bean validation

### **🔗 External Dependencies**
- **Jackson**: JSON processing
- **Apache Commons**: Utility libraries
- **SLF4J**: Logging facade
- **JUnit 5**: Unit testing framework

---

## 🚀 **Version History**

### **📈 Version 1.0.0**
- Initial release with base components
- Core entity and response classes
- Basic utility and validation classes
- Standard exception handling

### **🔄 Upcoming Features**
- **Enhanced encryption** utilities
- **Advanced validation** rules
- **Performance monitoring** utilities
- **Distributed tracing** support

---

## 📞 **Support**

### **👥 Development Team**
- **Module Owner**: NeoBridge Core Team
- **Technical Lead**: Available for technical questions
- **Code Reviewers**: Senior developers

### **📧 Contact Information**
- **Technical Issues**: Create GitHub issue
- **Feature Requests**: Submit enhancement proposal
- **Documentation**: Request documentation updates
- **Security Issues**: Contact security team directly

---

## 📄 **License**

This module is part of the **NeoBridge platform** and is proprietary software developed by **Mercuria Technologies** for **Harmony Q&Q GmbH**.

---

## 🏁 **Conclusion**

The **NeoBridge Common Module** provides the foundation for building consistent, secure, and maintainable microservices. By using these shared components, developers can focus on business logic while ensuring platform-wide consistency and quality.

**Ready to build robust NeoBridge services! 🚀**

---

<div align="center">

**Part of the NeoBridge Platform**  
**Built by Mercuria Technologies for Harmony Q&Q GmbH**

</div>
