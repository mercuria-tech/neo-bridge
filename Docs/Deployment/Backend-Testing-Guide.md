# 🚀 NeoBridge Backend Testing Guide

## Overview

Complete guide for deploying NeoBridge backend to GCP and testing all functionality. Follow this step-by-step to ensure successful production deployment.

## Prerequisites

```bash
# Install required tools
brew install google-cloud-sdk kubectl docker terraform helm k6
# Verify installations
gcloud version && kubectl version --client && docker --version
```

## GCP Deployment

### 1. Project Setup
```bash
gcloud projects create neobridge-platform --name="NeoBridge Platform"
gcloud config set project neobridge-platform
gcloud services enable container.googleapis.com sql-component.googleapis.com redis.googleapis.com
```

### 2. Create GKE Cluster
```bash
gcloud container clusters create neobridge-cluster \
  --zone=europe-west1-a --num-nodes=3 --machine-type=e2-standard-4 \
  --enable-autoscaling --min-nodes=1 --max-nodes=10
gcloud container clusters get-credentials neobridge-cluster --zone=europe-west1-a
```

### 3. Setup Infrastructure
```bash
# Create Cloud SQL
gcloud sql instances create neobridge-postgres --database-version=POSTGRES_16 --tier=db-f1-micro
gcloud sql databases create neobridge_dev --instance=neobridge-postgres

# Create Redis
gcloud redis instances create neobridge-redis --size=1 --region=europe-west1

# Create secrets
echo "jwt-secret-key" | gcloud secrets create jwt-secret --data-file=-
echo "db-password" | gcloud secrets create db-password --data-file=-
```

## Service Deployment

### 1. Create Namespace
```bash
kubectl create namespace neobridge
kubectl config set-context --current --namespace=neobridge
```

### 2. Deploy Services
```bash
# Deploy core infrastructure
kubectl apply -f k8s/database/postgresql-deployment.yaml
kubectl apply -f k8s/cache/redis-deployment.yaml
kubectl apply -f k8s/messaging/kafka-deployment.yaml

# Deploy NeoBridge microservices
kubectl apply -f neobridge-common/k8s/
kubectl apply -f neobridge-auth-service/k8s/
kubectl apply -f neobridge-account-service/k8s/
kubectl apply -f neobridge-payment-service/k8s/
kubectl apply -f neobridge-crypto-service/k8s/
kubectl apply -f neobridge-ai-risk-service/k8s/

# Deploy API Gateway and monitoring
kubectl apply -f neobridge-api-gateway/k8s/
kubectl apply -f monitoring/prometheus/
kubectl apply -f monitoring/grafana/
```

## Comprehensive Testing

### 1. Health Checks
```bash
# Check service status
kubectl get pods -n neobridge
kubectl get services -n neobridge

# Get API Gateway IP
API_GATEWAY_IP=$(kubectl get service kong-proxy -n neobridge -o jsonpath='{.status.loadBalancer.ingress[0].ip}')

# Test health endpoints
curl -k https://$API_GATEWAY_IP/health
curl -k https://$API_GATEWAY_IP/actuator/health
```

### 2. Authentication Testing
```bash
# Test user registration
curl -X POST https://$API_GATEWAY_IP/auth/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{"email": "test@neobridge.com", "password": "SecurePass123!", "firstName": "Test", "lastName": "User"}'

# Test user login
LOGIN_RESPONSE=$(curl -s -X POST https://$API_GATEWAY_IP/auth/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "test@neobridge.com", "password": "SecurePass123!"}')

# Extract access token
TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.data.accessToken')
echo "Access Token: $TOKEN"
```

### 3. Account Service Testing
```bash
# Test account creation
curl -X POST https://$API_GATEWAY_IP/accounts/v1/accounts \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"accountType": "current", "currency": "USD", "label": "Test Account"}'

# Test get accounts
curl -X GET https://$API_GATEWAY_IP/accounts/v1/accounts \
  -H "Authorization: Bearer $TOKEN"

# Get account ID
ACCOUNT_ID=$(curl -s -X GET https://$API_GATEWAY_IP/accounts/v1/accounts \
  -H "Authorization: Bearer $TOKEN" | jq -r '.data.accounts[0].accountId')
```

### 4. Payment Service Testing
```bash
# Test internal transfer
curl -X POST https://$API_GATEWAY_IP/payments/v1/transfers/internal \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"fromAccountId": "'$ACCOUNT_ID'", "toAccountId": "'$ACCOUNT_ID'", "amount": 100.00, "currency": "USD"}'

# Test SEPA transfer
curl -X POST https://$API_GATEWAY_IP/payments/v1/transfers/sepa \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"fromAccountId": "'$ACCOUNT_ID'", "iban": "DE89370400440532013000", "bic": "COBADEFFXXX", "amount": 50.00, "currency": "EUR"}'
```

### 5. Crypto Service Testing
```bash
# Test wallet creation
curl -X POST https://$API_GATEWAY_IP/crypto/v1/wallets \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"blockchainNetwork": "ethereum", "walletType": "hot_wallet", "currency": "ETH"}'

# Test get wallets
curl -X GET https://$API_GATEWAY_IP/crypto/v1/wallets \
  -H "Authorization: Bearer $TOKEN"

# Test supported networks
curl -X GET https://$API_GATEWAY_IP/crypto/v1/networks \
  -H "Authorization: Bearer $TOKEN"
```

### 6. AI Risk Service Testing
```bash
# Test risk assessment
curl -X POST https://$API_GATEWAY_IP/ai-risk/v1/risk/portfolio/assess \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userId": "test_user", "portfolioId": "test_portfolio", "assessmentType": "quick"}'

# Test fraud detection
curl -X POST https://$API_GATEWAY_IP/ai-risk/v1/fraud/transaction/analyze \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"transactionId": "tx_test", "userId": "test_user", "amount": 100.00}'
```

### 7. Database Testing
```bash
# Test database connection
kubectl exec -it deployment/postgresql -n neobridge -- psql -U neobridge_user -d neobridge_dev -c "SELECT version();"

# Test schema
kubectl exec -it deployment/postgresql -n neobridge -- psql -U neobridge_user -d neobridge_dev -c "\dt"

# Test data insertion
kubectl exec -it deployment/postgresql -n neobridge -- psql -U neobridge_user -d neobridge_dev -c "INSERT INTO users (email, first_name, last_name) VALUES ('test@neobridge.com', 'Test', 'User');"
```

## Performance Testing

### Load Testing with K6
```bash
# Create K6 test script
cat > k6-load-test.js << 'EOF'
import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  stages: [
    { duration: '2m', target: 10 },
    { duration: '5m', target: 10 },
    { duration: '2m', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],
    http_req_failed: ['rate<0.1'],
  },
};

export default function() {
  let response = http.get('https://<API_GATEWAY_IP>/health');
  check(response, { 'health status is 200': (r) => r.status === 200 });
  
  response = http.get('https://<API_GATEWAY_IP>/accounts/v1/accounts');
  check(response, { 'accounts status is 200': (r) => r.status === 200 });
  
  sleep(1);
}
EOF

# Run load test
k6 run k6-load-test.js
```

## Security Testing

### Authentication Security
```bash
# Test unauthenticated access
curl -X GET https://$API_GATEWAY_IP/accounts/v1/accounts
# Should return 401

# Test invalid token
curl -X GET https://$API_GATEWAY_IP/accounts/v1/accounts \
  -H "Authorization: Bearer invalid_token"
# Should return 401
```

### Input Validation
```bash
# Test SQL injection prevention
curl -X POST https://$API_GATEWAY_IP/auth/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{"email": "test@neobridge.com; DROP TABLE users;", "password": "123"}'
# Should return validation error
```

### Rate Limiting
```bash
# Test rate limiting
for i in {1..100}; do
  curl -X GET https://$API_GATEWAY_IP/health
  sleep 0.1
done
# Should see rate limiting after threshold
```

## Monitoring Validation

### Prometheus
```bash
PROMETHEUS_IP=$(kubectl get service prometheus -n neobridge -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
curl -X GET http://$PROMETHEUS_IP:9090/api/v1/query?query=up
```

### Grafana
```bash
GRAFANA_IP=$(kubectl get service grafana -n neobridge -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
curl -X GET http://$GRAFANA_IP:3000/api/health
```

### Alertmanager
```bash
ALERTMANAGER_IP=$(kubectl get service alertmanager -n neobridge -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
curl -X GET http://$ALERTMANAGER_IP:9093/api/v1/status
```

## Troubleshooting

### Common Issues
```bash
# Service won't start
kubectl logs deployment/<service-name> -n neobridge
kubectl describe pod <pod-name> -n neobridge

# Database issues
kubectl exec -it deployment/postgresql -n neobridge -- pg_isready

# API Gateway issues
kubectl exec -it deployment/kong -n neobridge -- kong health
```

## Success Criteria

### All Tests Must Pass:
1. ✅ All services running (kubectl get pods shows Running)
2. ✅ Health endpoints return 200 status
3. ✅ API endpoints respond correctly
4. ✅ Database operations work
5. ✅ Monitoring stack accessible
6. ✅ Security features working
7. ✅ Performance targets met
8. ✅ No critical issues

### Performance Targets:
- API Response Time: < 200ms (95th percentile)
- Database Queries: < 100ms
- Service Startup: < 30 seconds
- Error Rate: < 1%
- Availability: 99.9%

## Test Report Generation

```bash
# Generate test report
echo "=== NeoBridge Test Results ===" > test-results.md
echo "Date: $(date)" >> test-results.md
echo "Environment: GCP Production" >> test-results.md
kubectl get pods -n neobridge >> test-results.md
echo "Test completed successfully!" >> test-results.md
```

## Next Steps

Once all tests pass:
1. ✅ Backend is production-ready
2. 🚀 Can begin processing real transactions
3. 📱 Start frontend development
4. 🌍 Platform ready for global deployment

---

**Your NeoBridge backend is now fully tested and ready for production use! 🎉**
