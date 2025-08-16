# 🚀 NeoBridge Platform - Developer Quick Start Guide

**Get up and running with NeoBridge development in under 30 minutes!**

---

## 🎯 **Quick Start Overview**

This guide will get you **up and running with NeoBridge development** in the fastest way possible. You'll have a complete development environment with all services running locally, ready for development and testing.

### **⏱️ Time to Complete: 30 minutes**
- **Environment Setup**: 10 minutes
- **Service Startup**: 15 minutes
- **First API Call**: 5 minutes

---

## 📋 **Prerequisites**

### **🛠️ Required Software**
- **Java 21 JDK** (LTS version)
- **Maven 3.9+** (latest stable)
- **Docker & Docker Compose** (latest)
- **Git** (latest version)
- **Node.js 20+** (for frontend development)

### **💻 System Requirements**
- **RAM**: 8GB minimum (16GB recommended)
- **Storage**: 20GB free space
- **OS**: macOS, Linux, or Windows with WSL2
- **Network**: Internet connection for dependencies

---

## 🚀 **Step 1: Environment Setup (5 minutes)**

### **1️⃣ Clone the Repository**
```bash
# Clone the NeoBridge repository
git clone <repository-url>
cd neobridge-platform

# Verify the structure
ls -la
```

### **2️⃣ Install Dependencies**
```bash
# Install Java dependencies (all services)
mvn clean install -DskipTests

# Install frontend dependencies
cd mobile-app && npm install && cd ..
cd admin-dashboard && npm install && cd ..
```

---

## 🐳 **Step 2: Start Development Environment (10 minutes)**

### **1️⃣ Start All Services**
```bash
# Start the complete development environment
docker-compose up -d

# Verify all services are running
docker-compose ps
```

### **2️⃣ Wait for Services to Initialize**
```bash
# Monitor service startup
docker-compose logs -f

# Wait until you see "Started" messages for all services
# This typically takes 5-10 minutes on first run
```

### **3️⃣ Verify Service Health**
```bash
# Check service health endpoints
curl http://localhost:8080/actuator/health  # Auth Service
curl http://localhost:8081/actuator/health  # Account Service
curl http://localhost:8082/actuator/health  # Payment Service

# Check monitoring stack
curl http://localhost:9090/-/healthy        # Prometheus
curl http://localhost:3000/api/health       # Grafana
```

---

## 🔧 **Step 3: Configure Development Environment (5 minutes)**

### **1️⃣ Set Environment Variables**
```bash
# Create environment file
cp .env.example .env

# Edit environment variables
nano .env
```

**Key Environment Variables:**
```bash
# Database
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DB=neobridge_dev
POSTGRES_USER=neobridge_user
POSTGRES_PASSWORD=neobridge_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# API Gateway
KONG_ADMIN_URL=http://localhost:8001
KONG_PROXY_URL=http://localhost:8000
```

### **2️⃣ Verify Database Connection**
```bash
# Connect to PostgreSQL
docker exec -it neobridge-postgres psql -U neobridge_user -d neobridge_dev

# Check tables
\dt

# Exit
\q
```

---

## 🧪 **Step 4: First API Call (5 minutes)**

### **1️⃣ Get Authentication Token**
```bash
# Register a new user
curl -X POST http://localhost:8000/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "developer@neobridge.com",
    "password": "SecurePass123!",
    "fullName": "Developer User",
    "phoneNumber": "+1234567890"
  }'

# Login to get access token
curl -X POST http://localhost:8000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "developer@neobridge.com",
    "password": "SecurePass123!"
  }'
```

### **2️⃣ Create Your First Account**
```bash
# Use the access token from login response
ACCESS_TOKEN="your_access_token_here"

# Create a checking account
curl -X POST http://localhost:8000/api/v1/accounts \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "accountType": "CHECKING",
    "currency": "EUR",
    "accountName": "My First Account",
    "initialDeposit": 1000.00
  }'
```

### **3️⃣ Verify Account Creation**
```bash
# Get account details
curl -X GET http://localhost:8000/api/v1/accounts \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

---

## 🔍 **Step 5: Explore the Platform (5 minutes)**

### **1️⃣ Access Development Tools**
| Service | URL | Credentials |
|---------|-----|-------------|
| **Kong Admin** | http://localhost:8001 | None required |
| **Kong GUI** | http://localhost:8002 | None required |
| **Prometheus** | http://localhost:9090 | None required |
| **Grafana** | http://localhost:3000 | admin/admin |
| **pgAdmin** | http://localhost:5050 | admin/admin |

### **2️⃣ Check Service Logs**
```bash
# View all service logs
docker-compose logs

# View specific service logs
docker-compose logs auth-service
docker-compose logs account-service
docker-compose logs payment-service

# Follow logs in real-time
docker-compose logs -f auth-service
```

### **3️⃣ Monitor System Health**
```bash
# Check system resources
docker stats

# Check service status
docker-compose ps

# Check service health
curl http://localhost:8000/actuator/health
```

---

## 🚀 **Step 6: Start Development**

### **1️⃣ Run Services Locally**
```bash
# Run authentication service locally
cd neobridge-auth-service
mvn spring-boot:run

# In another terminal, run account service
cd neobridge-account-service
mvn spring-boot:run

# In another terminal, run payment service
cd neobridge-payment-service
mvn spring-boot:run
```

### **2️⃣ Frontend Development**
```bash
# Start mobile app development
cd mobile-app
npm start

# Start admin dashboard development
cd admin-dashboard
npm run dev
```

### **3️⃣ Database Development**
```bash
# Connect to database for development
docker exec -it neobridge-postgres psql -U neobridge_user -d neobridge_dev

# Create development data
INSERT INTO users (email, full_name, status) VALUES ('dev@test.com', 'Dev User', 'ACTIVE');

# Check data
SELECT * FROM users;
```

---

## 🔧 **Development Workflow**

### **📝 Code Changes**
```bash
# Make code changes in your IDE
# NeoBridge uses hot reload, so changes are reflected immediately

# For database changes, create migrations
cd neobridge-common
mvn flyway:migrate

# For API changes, test with curl or Postman
curl -X GET http://localhost:8000/api/v1/users \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

### **🧪 Testing**
```bash
# Run unit tests
mvn test

# Run integration tests
mvn test -Dtest=*IntegrationTest

# Run with coverage
mvn jacoco:report
```

### **🔍 Debugging**
```bash
# Enable debug logging
# Add to application.yml
logging:
  level:
    com.neobridge: DEBUG
    org.springframework.web: DEBUG

# Use IDE debugger
# Set breakpoints and debug your code
```

---

## 📊 **Monitoring & Debugging**

### **📈 Performance Monitoring**
```bash
# Check service metrics
curl http://localhost:8080/actuator/metrics

# Check specific metrics
curl http://localhost:8080/actuator/metrics/http.server.requests

# View Grafana dashboards
# Open http://localhost:3000 and explore pre-built dashboards
```

### **🔍 Log Analysis**
```bash
# Search logs for errors
docker-compose logs | grep ERROR

# Search logs for specific user
docker-compose logs | grep "developer@neobridge.com"

# View structured logs
docker-compose logs --tail=100 | jq .
```

---

## 🚨 **Troubleshooting**

### **❌ Common Issues**

#### **Services Won't Start**
```bash
# Check Docker resources
docker system df
docker system prune -f

# Check port conflicts
netstat -tulpn | grep :8080
lsof -i :8080

# Restart services
docker-compose down
docker-compose up -d
```

#### **Database Connection Issues**
```bash
# Check PostgreSQL status
docker-compose logs postgres

# Reset database
docker-compose down -v
docker-compose up -d

# Check database connectivity
docker exec -it neobridge-postgres pg_isready -U neobridge_user
```

#### **API Gateway Issues**
```bash
# Check Kong status
docker-compose logs kong

# Restart Kong
docker-compose restart kong

# Check Kong health
curl http://localhost:8001/status
```

### **🔧 Reset Environment**
```bash
# Complete reset (use with caution)
docker-compose down -v
docker system prune -af
docker-compose up -d
```

---

## 📚 **Next Steps**

### **🎯 What You've Accomplished**
- ✅ **Complete development environment** running locally
- ✅ **All microservices** operational and healthy
- ✅ **Database and infrastructure** services running
- ✅ **First API calls** successful
- ✅ **Development tools** accessible and configured

### **🚀 Continue Learning**
- **API Documentation**: [Complete API Guide](../API/README.md)
- **Architecture**: [System Architecture](../Architecture/System-Architecture.md)
- **Service Development**: [Adding New Services](Adding-New-Services.md)
- **Testing**: [Testing Guidelines](Testing-Guidelines.md)
- **Deployment**: [Production Deployment](../Deployment/README.md)

### **🔧 Development Tasks**
- **Explore APIs**: Test all service endpoints
- **Build Features**: Add new functionality to services
- **Create Tests**: Write comprehensive test coverage
- **Optimize Performance**: Profile and optimize services
- **Security Review**: Implement security best practices

---

## 📞 **Getting Help**

### **👥 Support Channels**
- **Documentation**: Comprehensive guides in this folder
- **GitHub Issues**: Report bugs and request features
- **Development Team**: Direct technical support
- **Community Forum**: Developer discussions and help

### **🔍 Self-Service Resources**
- **Service Logs**: Real-time debugging information
- **Health Checks**: Service status monitoring
- **Metrics**: Performance and usage analytics
- **API Documentation**: Complete endpoint reference

---

## 🏁 **Congratulations!**

You've successfully **set up the complete NeoBridge development environment** and made your first API calls! You now have:

- 🏗️ **Full microservices architecture** running locally
- 🔐 **Complete authentication system** operational
- 🏦 **Banking services** ready for development
- 📊 **Monitoring and observability** tools accessible
- 🧪 **Testing environment** configured and ready

**Ready to build the future of banking with NeoBridge! 🚀**

---

<div align="center">

**NeoBridge Developer Quick Start**  
**Built by Mercuria Technologies for Harmony Q&Q GmbH**

</div>
