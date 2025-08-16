# NeoBridge API Gateway

The NeoBridge API Gateway is built on Kong Enterprise, providing a robust, scalable, and secure entry point for all NeoBridge platform services.

## 🌟 Features

### **Core Gateway Features**
- **Unified API Entry Point**: Single gateway for all NeoBridge services
- **Load Balancing**: Intelligent routing and load distribution
- **SSL/TLS Termination**: Secure HTTPS communication
- **Rate Limiting**: Per-service and global rate limiting
- **CORS Management**: Cross-origin resource sharing configuration
- **Request/Response Transformation**: Header and payload modification

### **Security Features**
- **JWT Authentication**: Token-based authentication for protected endpoints
- **IP Restrictions**: Configurable IP allow/deny lists
- **Request Size Limiting**: Protection against large payload attacks
- **API Key Management**: Secure API key authentication
- **OAuth2 Integration**: Authorization server integration

### **Monitoring & Analytics**
- **Prometheus Metrics**: Comprehensive monitoring and alerting
- **Real-time Analytics**: Request/response metrics and latency tracking
- **Health Checks**: Service availability monitoring
- **Performance Metrics**: Response time and throughput analysis
- **Error Tracking**: Detailed error logging and reporting

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client Apps   │    │   Mobile Apps   │    │   Web Apps      │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │   Kong API Gateway       │
                    │   (Port 8000/8443)       │
                    └─────────────┬─────────────┘
                                 │
          ┌──────────────────────┼──────────────────────┐
          │                      │                      │
┌─────────▼─────────┐  ┌─────────▼─────────┐  ┌─────────▼─────────┐
│ Auth Service      │  │ Account Service   │  │ Payment Service   │
│ (Port 8081)       │  │ (Port 8082)       │  │ (Port 8083)       │
└───────────────────┘  └───────────────────┘  └───────────────────┘
          │                      │                      │
┌─────────▼─────────┐  ┌─────────▼─────────┐  ┌─────────▼─────────┐
│ Crypto Service    │  │ Admin Service     │  │ Other Services    │
│ (Port 8084)       │  │ (Port 8085)       │  │ (Port 8086+)      │
└───────────────────┘  └───────────────────┘  └───────────────────┘
```

## 🚀 Quick Start

### **Prerequisites**
- Docker and Docker Compose
- OpenSSL (for SSL certificate generation)
- curl (for testing endpoints)

### **1. Clone and Navigate**
```bash
cd neobridge-api-gateway
```

### **2. Deploy the Gateway**
```bash
./scripts/deploy.sh
```

### **3. Verify Deployment**
```bash
# Check gateway status
curl http://localhost:8001/status

# Test proxy endpoint
curl http://localhost:8000

# Access admin GUI
open http://localhost:8002
```

## 📋 Service Endpoints

### **Authentication Service**
- **URL**: `http://localhost:8000/auth`
- **Rate Limit**: 100 requests/minute, 1000 requests/hour
- **Features**: CORS enabled, request transformation

### **Account Service**
- **URL**: `http://localhost:8000/accounts`
- **Rate Limit**: 200 requests/minute, 2000 requests/hour
- **Features**: JWT authentication required, CORS enabled

### **Payment Service**
- **URL**: `http://localhost:8000/payments`
- **Rate Limit**: 300 requests/minute, 3000 requests/hour
- **Features**: JWT authentication required, CORS enabled

### **Crypto Service**
- **URL**: `http://localhost:8000/crypto`
- **Rate Limit**: 150 requests/minute, 1500 requests/hour
- **Features**: JWT authentication required, CORS enabled

### **Admin Service**
- **URL**: `http://localhost:8000/admin`
- **Rate Limit**: 50 requests/minute, 500 requests/hour
- **Features**: JWT authentication required, CORS enabled

## ⚙️ Configuration

### **Kong Configuration Files**
- `config/kong.yml` - Main Kong configuration with services and routes
- `config/kong.conf` - Kong environment configuration
- `config/prometheus.yml` - Prometheus monitoring configuration
- `config/kong-rules.yml` - Alerting rules for monitoring

### **Environment Variables**
```bash
# Kong Database
KONG_DATABASE=postgres
KONG_PG_HOST=kong-database
KONG_PG_USER=kong
KONG_PG_PASSWORD=kong_password
KONG_PG_DATABASE=kong

# JWT Configuration
KONG_JWT_SECRET=your-jwt-secret
KONG_JWT_EXPIRATION=86400000
KONG_JWT_REFRESH_EXPIRATION=604800000

# SSL Configuration
KONG_SSL=on
KONG_SSL_CERT=/etc/ssl/certs/kong.crt
KONG_SSL_CERT_KEY=/etc/ssl/private/kong.key
```

## 🔒 Security Configuration

### **JWT Authentication**
```yaml
- name: jwt
  config:
    uri_param_names: ["jwt"]
    cookie_names: ["jwt"]
    header_names: ["Authorization"]
    claims_to_verify: ["exp"]
    key_claim_name: "iss"
    secret_is_base64: false
    anonymous: null
    run_on_preflight: true
```

### **Rate Limiting**
```yaml
- name: rate-limiting
  config:
    minute: 100
    hour: 1000
    policy: local
```

### **CORS Configuration**
```yaml
- name: cors
  config:
    origins: ["*"]
    methods: ["GET", "POST", "PUT", "DELETE", "OPTIONS"]
    headers: ["Content-Type", "Authorization", "X-Requested-With"]
    exposed_headers: ["X-Total-Count"]
    credentials: true
    max_age: 3600
```

## 📊 Monitoring & Metrics

### **Prometheus Metrics**
- **Endpoint**: `http://localhost:8001/metrics`
- **Metrics**: Request count, latency, bandwidth, error rates
- **Alerts**: High error rates, latency spikes, service failures

### **Health Checks**
- **Gateway Health**: `http://localhost:8001/status`
- **Service Health**: Individual service health endpoints
- **Database Health**: PostgreSQL connection monitoring

### **Key Metrics**
- `kong_http_requests_total` - Total HTTP requests
- `kong_http_request_duration_ms` - Request latency
- `kong_http_requests_total{code=~"4..|5.."}` - Error rates
- `kong_upstream_health_checks_failed` - Upstream failures

## 🛠️ Administration

### **Kong Admin API**
- **URL**: `http://localhost:8001`
- **Documentation**: [Kong Admin API Docs](https://docs.konghq.com/gateway/latest/admin-api/)

### **Kong Admin GUI**
- **URL**: `http://localhost:8002`
- **Features**: Visual service management, plugin configuration, monitoring

### **Common Admin Operations**
```bash
# List all services
curl http://localhost:8001/services

# List all routes
curl http://localhost:8001/routes

# List all plugins
curl http://localhost:8001/plugins

# Get service health
curl http://localhost:8001/health
```

## 🔧 Troubleshooting

### **Common Issues**

#### **1. Gateway Not Starting**
```bash
# Check container logs
docker-compose logs kong

# Check database connectivity
docker-compose exec kong-database pg_isready -U kong -d kong
```

#### **2. Service Unreachable**
```bash
# Check service health
curl http://localhost:8001/health

# Verify service configuration
curl http://localhost:8001/services
```

#### **3. SSL Certificate Issues**
```bash
# Regenerate SSL certificates
rm -rf ssl/
./scripts/deploy.sh
```

### **Log Analysis**
```bash
# View Kong logs
docker-compose logs -f kong

# View database logs
docker-compose logs -f kong-database

# Check specific service logs
docker-compose logs -f kong | grep "ERROR"
```

## 📚 Additional Resources

### **Documentation**
- [Kong Gateway Documentation](https://docs.konghq.com/gateway/)
- [Kong Plugin Hub](https://docs.konghq.com/hub/)
- [Kong Admin API Reference](https://docs.konghq.com/gateway/latest/admin-api/)

### **Community**
- [Kong Community Forum](https://discuss.konghq.com/)
- [Kong GitHub Repository](https://github.com/Kong/kong)
- [Kong Slack Channel](https://kong-community.slack.com/)

### **Support**
- **NeoBridge Development Team**: dev@neobridge.com
- **Kong Enterprise Support**: Available with Kong Enterprise license

## 📄 License

This project is proprietary software developed by **Mercuria Technologies** for **NeoBridge, owned by Harmony Q&Q GmbH**.

---

**Built with ❤️ by the NeoBridge Development Team**
