# ☁️ NeoBridge Platform - Google Cloud Production Deployment Guide

**Complete step-by-step guide for deploying NeoBridge on Google Cloud Platform with production-ready infrastructure**

---

## 🎯 **Deployment Overview**

This guide provides **complete instructions** for deploying the NeoBridge platform on **Google Cloud Platform (GCP)** with enterprise-grade infrastructure. The deployment includes Kubernetes orchestration, managed databases, monitoring, and security best practices.

### **🌟 What You'll Deploy**
- **Google Kubernetes Engine (GKE)** cluster for microservices
- **Cloud SQL** for PostgreSQL database management
- **Cloud Memorystore** for Redis caching
- **Cloud Storage** for file storage and backups
- **Cloud Load Balancing** for traffic distribution
- **Cloud Monitoring** for observability and alerting
- **Cloud Security** for enterprise-grade protection

---

## 📋 **Prerequisites**

### **🛠️ Required Tools**
- **Google Cloud CLI** (gcloud) installed and configured
- **kubectl** for Kubernetes management
- **Docker** for container image management
- **Terraform** (optional) for infrastructure as code
- **Git** for source code management

### **💳 Google Cloud Requirements**
- **Active Google Cloud Project** with billing enabled
- **Sufficient Quotas** for compute, networking, and storage
- **IAM Permissions** for project administration
- **Budget Alerts** configured to avoid unexpected costs

### **💰 Estimated Costs**
- **GKE Cluster**: $300-500/month
- **Cloud SQL**: $200-400/month
- **Cloud Storage**: $50-100/month
- **Load Balancer**: $20-50/month
- **Monitoring**: $50-100/month
- **Total**: $620-1,150/month

---

## 🚀 **Step 1: Project Setup & Configuration**

### **1️⃣ Create and Configure Google Cloud Project**
```bash
# Create new project (if needed)
gcloud projects create neobridge-platform --name="NeoBridge Platform"

# Set project as default
gcloud config set project neobridge-platform

# Enable required APIs
gcloud services enable \
  container.googleapis.com \
  sql-component.googleapis.com \
  sqladmin.googleapis.com \
  redis.googleapis.com \
  storage-component.googleapis.com \
  compute.googleapis.com \
  monitoring.googleapis.com \
  logging.googleapis.com \
  secretmanager.googleapis.com \
  cloudkms.googleapis.com

# Verify APIs are enabled
gcloud services list --enabled
```

### **2️⃣ Configure IAM and Service Accounts**
```bash
# Create service account for GKE
gcloud iam service-accounts create neobridge-gke \
  --display-name="NeoBridge GKE Service Account"

# Grant necessary roles
gcloud projects add-iam-policy-binding neobridge-platform \
  --member="serviceAccount:neobridge-gke@neobridge-platform.iam.gserviceaccount.com" \
  --role="roles/container.admin"

gcloud projects add-iam-policy-binding neobridge-platform \
  --member="serviceAccount:neobridge-gke@neobridge-platform.iam.gserviceaccount.com" \
  --role="roles/storage.admin"

gcloud projects add-iam-policy-binding neobridge-platform \
  --member="serviceAccount:neobridge-gke@neobridge-platform.iam.gserviceaccount.com" \
  --role="roles/monitoring.admin"

# Create service account for applications
gcloud iam service-accounts create neobridge-apps \
  --display-name="NeoBridge Applications Service Account"

# Grant application roles
gcloud projects add-iam-policy-binding neobridge-platform \
  --member="serviceAccount:neobridge-apps@neobridge-platform.iam.gserviceaccount.com" \
  --role="roles/cloudsql.client"

gcloud projects add-iam-policy-binding neobridge-platform \
  --member="serviceAccount:neobridge-apps@neobridge-platform.iam.gserviceaccount.com" \
  --role="roles/redis.admin"
```

---

## 🏗️ **Step 2: Network Infrastructure Setup**

### **1️⃣ Create VPC Network**
```bash
# Create custom VPC
gcloud compute networks create neobridge-vpc \
  --subnet-mode=custom \
  --bgp-routing-mode=regional

# Create subnets for different regions
gcloud compute networks subnets create neobridge-subnet-europe-west1 \
  --network=neobridge-vpc \
  --region=europe-west1 \
  --range=10.0.1.0/24 \
  --enable-private-ip-google-access

gcloud compute networks subnets create neobridge-subnet-europe-west2 \
  --network=neobridge-vpc \
  --region=europe-west2 \
  --range=10.0.2.0/24 \
  --enable-private-ip-google-access

gcloud compute networks subnets create neobridge-subnet-europe-west3 \
  --network=neobridge-vpc \
  --region=europe-west3 \
  --range=10.0.3.0/24 \
  --enable-private-ip-google-access

# Create firewall rules
gcloud compute firewall-rules create neobridge-allow-internal \
  --network=neobridge-vpc \
  --allow=tcp,udp,icmp \
  --source-ranges=10.0.0.0/8

gcloud compute firewall-rules create neobridge-allow-ssh \
  --network=neobridge-vpc \
  --allow=tcp:22 \
  --source-ranges=0.0.0.0/0

gcloud compute firewall-rules create neobridge-allow-https \
  --network=neobridge-vpc \
  --allow=tcp:443 \
  --source-ranges=0.0.0.0/0
```

### **2️⃣ Configure Cloud NAT**
```bash
# Create Cloud Router
gcloud compute routers create neobridge-router \
  --network=neobridge-vpc \
  --region=europe-west1

# Configure Cloud NAT
gcloud compute routers nats create neobridge-nat \
  --router=neobridge-router \
  --region=europe-west1 \
  --nat-all-subnet-ip-ranges \
  --source-subnetwork-ip-ranges-to-nat=ALL_SUBNETWORKS_ALL_IP_RANGES
```

---

## ☸️ **Step 3: Google Kubernetes Engine (GKE) Setup**

### **1️⃣ Create GKE Cluster**
```bash
# Create production GKE cluster
gcloud container clusters create neobridge-cluster \
  --region=europe-west1 \
  --node-locations=europe-west1-a,europe-west1-b,europe-west1-c \
  --num-nodes=3 \
  --min-nodes=3 \
  --max-nodes=10 \
  --enable-autoscaling \
  --enable-autorepair \
  --enable-autoupgrade \
  --enable-shielded-nodes \
  --enable-network-policy \
  --enable-ip-alias \
  --network=neobridge-vpc \
  --subnetwork=neobridge-subnet-europe-west1 \
  --master-cidr=172.16.0.0/28 \
  --enable-private-nodes \
  --enable-master-authorized-networks \
  --master-authorized-networks=0.0.0.0/0 \
  --service-account=neobridge-gke@neobridge-platform.iam.gserviceaccount.com \
  --addons=HttpLoadBalancing,HorizontalPodAutoscaling,NetworkPolicy \
  --workload-pool=neobridge-platform.svc.id.goog \
  --enable-stackdriver-kubernetes \
  --enable-ip-alias \
  --default-max-pods-per-node=110 \
  --enable-tpu \
  --enable-vertical-pod-autoscaling

# Get cluster credentials
gcloud container clusters get-credentials neobridge-cluster \
  --region=europe-west1

# Verify cluster status
kubectl cluster-info
kubectl get nodes
```

### **2️⃣ Configure Node Pools**
```bash
# Create dedicated node pool for database workloads
gcloud container node-pools create database-pool \
  --cluster=neobridge-cluster \
  --region=europe-west1 \
  --node-locations=europe-west1-a \
  --num-nodes=2 \
  --min-nodes=2 \
  --max-nodes=5 \
  --enable-autoscaling \
  --machine-type=e2-standard-4 \
  --disk-size=100 \
  --disk-type=pd-ssd \
  --node-taints=dedicated=database:NoSchedule

# Create dedicated node pool for monitoring
gcloud container node-pools create monitoring-pool \
  --cluster=neobridge-cluster \
  --region=europe-west1 \
  --node-locations=europe-west1-b \
  --num-nodes=2 \
  --min-nodes=2 \
  --max-nodes=4 \
  --enable-autoscaling \
  --machine-type=e2-standard-2 \
  --disk-size=50 \
  --disk-type=pd-standard \
  --node-taints=dedicated=monitoring:NoSchedule

# Verify node pools
kubectl get nodes --show-labels
```

---

## 🗄️ **Step 4: Database Infrastructure**

### **1️⃣ Cloud SQL PostgreSQL Setup**
```bash
# Create Cloud SQL instance
gcloud sql instances create neobridge-postgres \
  --database-version=POSTGRES_16 \
  --tier=db-custom-8-32 \
  --region=europe-west1 \
  --storage-type=SSD \
  --storage-size=500GB \
  --backup-start-time=02:00 \
  --backup-retention-days=7 \
  --maintenance-window-day=SUN \
  --maintenance-window-hour=03 \
  --availability-type=REGIONAL \
  --enable-point-in-time-recovery \
  --enable-backup \
  --enable-binary-logging \
  --require-ssl \
  --authorized-networks=10.0.0.0/8 \
  --root-password=NeoBridgeSecurePass123!

# Create databases for different services
gcloud sql databases create neobridge_auth --instance=neobridge-postgres
gcloud sql databases create neobridge_accounts --instance=neobridge-postgres
gcloud sql databases create neobridge_payments --instance=neobridge-postgres
gcloud sql databases create neobridge_crypto --instance=neobridge-postgres
gcloud sql databases create neobridge_investments --instance=neobridge-postgres

# Create users for different services
gcloud sql users create neobridge_auth_user \
  --instance=neobridge-postgres \
  --password=AuthServicePass123!

gcloud sql users create neobridge_accounts_user \
  --instance=neobridge-postgres \
  --password=AccountsServicePass123!

gcloud sql users create neobridge_payments_user \
  --instance=neobridge-postgres \
  --password=PaymentsServicePass123!

# Grant permissions
gcloud sql users set-password neobridge_auth_user \
  --instance=neobridge-postgres \
  --password=AuthServicePass123!

# Get connection information
gcloud sql instances describe neobridge-postgres \
  --format="value(connectionName)"
```

### **2️⃣ Cloud Memorystore Redis Setup**
```bash
# Create Redis instance
gcloud redis instances create neobridge-redis \
  --size=5 \
  --region=europe-west1 \
  --redis-version=redis_7_0 \
  --tier=STANDARD_HA \
  --connect-mode=PRIVATE_SERVICE_ACCESS \
  --network=neobridge-vpc \
  --enable-auth \
  --auth-string=NeoBridgeRedisPass123!

# Get Redis connection information
gcloud redis instances describe neobridge-redis \
  --region=europe-west1 \
  --format="value(host,port,authString)"
```

---

## 📦 **Step 5: Container Registry & Image Management**

### **1️⃣ Configure Container Registry**
```bash
# Configure Docker for GCR
gcloud auth configure-docker

# Create repository for NeoBridge images
gcloud container images list-tags gcr.io/neobridge-platform

# Build and push service images
cd neobridge-auth-service
docker build -t gcr.io/neobridge-platform/neobridge-auth:latest .
docker push gcr.io/neobridge-platform/neobridge-auth:latest

cd ../neobridge-account-service
docker build -t gcr.io/neobridge-platform/neobridge-account:latest .
docker push gcr.io/neobridge-platform/neobridge-account:latest

cd ../neobridge-payment-service
docker build -t gcr.io/neobridge-platform/neobridge-payment:latest .
docker push gcr.io/neobridge-platform/neobridge-payment:latest

cd ../neobridge-crypto-service
docker build -t gcr.io/neobridge-platform/neobridge-crypto:latest .
docker push gcr.io/neobridge-platform/neobridge-crypto:latest

cd ../neobridge-ai-risk-service
docker build -t gcr.io/neobridge-platform/neobridge-ai-risk:latest .
docker push gcr.io/neobridge-platform/neobridge-ai-risk:latest
```

### **2️⃣ Create Image Pull Secrets**
```bash
# Create image pull secret
kubectl create secret docker-registry gcr-secret \
  --docker-server=gcr.io \
  --docker-username=_json_key \
  --docker-password="$(cat neobridge-platform-key.json)" \
  --docker-email=admin@neobridge.com

# Verify secret creation
kubectl get secrets
```

---

## 🔐 **Step 6: Secrets & Configuration Management**

### **1️⃣ Google Secret Manager Setup**
```bash
# Create secrets for different services
echo "NeoBridgeAuthSecret123!" | gcloud secrets create neobridge-auth-secret \
  --data-file=-

echo "NeoBridgeAccountsSecret123!" | gcloud secrets create neobridge-accounts-secret \
  --data-file=-

echo "NeoBridgePaymentsSecret123!" | gcloud secrets create neobridge-payments-secret \
  --data-file=-

echo "NeoBridgeCryptoSecret123!" | gcloud secrets create neobridge-crypto-secret \
  --data-file=-

echo "NeoBridgeAIRiskSecret123!" | gcloud secrets create neobridge-ai-risk-secret \
  --data-file=-

# Create database connection secrets
echo "jdbc:postgresql://10.0.0.3:5432/neobridge_auth" | gcloud secrets create neobridge-auth-db-url \
  --data-file=-

echo "jdbc:postgresql://10.0.0.3:5432/neobridge_accounts" | gcloud secrets create neobridge-accounts-db-url \
  --data-file=-

echo "jdbc:postgresql://10.0.0.3:5432/neobridge_payments" | gcloud secrets create neobridge-payments-db-url \
  --data-file=-

# Create external API secrets
echo "SolarisbankAPIKey123!" | gcloud secrets create neobridge-solarisbank-api-key \
  --data-file=-

echo "JumioAPIKey123!" | gcloud secrets create neobridge-jumio-api-key \
  --data-file=-

echo "CoinbaseCustodyKey123!" | gcloud secrets create neobridge-coinbase-custody-key \
  --data-file=-
```

### **2️⃣ Kubernetes Secrets Configuration**
```bash
# Create ConfigMap for common configuration
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: ConfigMap
metadata:
  name: neobridge-config
data:
  # Database configuration
  DB_HOST: "10.0.0.3"
  DB_PORT: "5432"
  
  # Redis configuration
  REDIS_HOST: "10.0.0.4"
  REDIS_PORT: "6379"
  
  # External service endpoints
  SOLARISBANK_API_URL: "https://api.solarisbank.com"
  JUMIO_API_URL: "https://api.jumio.com"
  COINBASE_CUSTODY_URL: "https://api.custody.coinbase.com"
  
  # Platform configuration
  PLATFORM_ENVIRONMENT: "production"
  PLATFORM_REGION: "europe-west1"
  PLATFORM_VERSION: "1.0.0"
EOF

# Verify ConfigMap creation
kubectl get configmaps
kubectl describe configmap neobridge-config
```

---

## 🚀 **Step 7: Service Deployment**

### **1️⃣ Deploy Authentication Service**
```bash
# Create namespace for NeoBridge services
kubectl create namespace neobridge

# Deploy authentication service
cat <<EOF | kubectl apply -f -
apiVersion: apps/v1
kind: Deployment
metadata:
  name: neobridge-auth-service
  namespace: neobridge
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
      serviceAccountName: neobridge-apps
      containers:
      - name: auth-service
        image: gcr.io/neobridge-platform/neobridge-auth:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: DB_URL
          valueFrom:
            secretKeyRef:
              name: neobridge-auth-db-url
              key: latest
        - name: DB_USERNAME
          value: "neobridge_auth_user"
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: neobridge-auth-secret
              key: latest
        - name: REDIS_HOST
          value: "10.0.0.4"
        - name: REDIS_PORT
          value: "6379"
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
      imagePullSecrets:
      - name: gcr-secret
---
apiVersion: v1
kind: Service
metadata:
  name: neobridge-auth-service
  namespace: neobridge
spec:
  selector:
    app: neobridge-auth-service
  ports:
  - port: 80
    targetPort: 8080
  type: ClusterIP
EOF

# Verify deployment
kubectl get deployments -n neobridge
kubectl get pods -n neobridge
kubectl get services -n neobridge
```

### **2️⃣ Deploy Account Service**
```bash
# Deploy account service
cat <<EOF | kubectl apply -f -
apiVersion: apps/v1
kind: Deployment
metadata:
  name: neobridge-account-service
  namespace: neobridge
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
      serviceAccountName: neobridge-apps
      containers:
      - name: account-service
        image: gcr.io/neobridge-platform/neobridge-account:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: DB_URL
          valueFrom:
            secretKeyRef:
              name: neobridge-accounts-db-url
              key: latest
        - name: DB_USERNAME
          value: "neobridge_accounts_user"
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: neobridge-accounts-secret
              key: latest
        - name: SOLARISBANK_API_KEY
          valueFrom:
            secretKeyRef:
              name: neobridge-solarisbank-api-key
              key: latest
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
      imagePullSecrets:
      - name: gcr-secret
---
apiVersion: v1
kind: Service
metadata:
  name: neobridge-account-service
  namespace: neobridge
spec:
  selector:
    app: neobridge-account-service
  ports:
  - port: 80
    targetPort: 8080
  type: ClusterIP
EOF
```

---

## 🌐 **Step 8: Load Balancer & Ingress Setup**

### **1️⃣ Configure Cloud Load Balancer**
```bash
# Create ingress configuration
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: neobridge-ingress
  namespace: neobridge
  annotations:
    kubernetes.io/ingress.class: "gce"
    kubernetes.io/ingress.global-static-ip-name: "neobridge-ip"
    kubernetes.io/ingress.allow-http: "false"
    kubernetes.io/ingress.force-ssl-redirect: "true"
    cloud.google.com/load-balancer-type: "External"
    cloud.google.com/backend-config: '{"default": "neobridge-backend-config"}'
spec:
  tls:
  - secretName: neobridge-tls-secret
  rules:
  - host: api.neobridge.com
    http:
      paths:
      - path: /api/v1/auth
        pathType: Prefix
        backend:
          service:
            name: neobridge-auth-service
            port:
              number: 80
      - path: /api/v1/accounts
        pathType: Prefix
        backend:
          service:
            name: neobridge-account-service
            port:
              number: 80
      - path: /api/v1/payments
        pathType: Prefix
        backend:
          service:
            name: neobridge-payment-service
            port:
              number: 80
      - path: /api/v1/crypto
        pathType: Prefix
        backend:
          service:
            name: neobridge-crypto-service
            port:
              number: 80
      - path: /api/v1/risk
        pathType: Prefix
        backend:
          service:
            name: neobridge-ai-risk-service
            port:
              number: 80
EOF

# Create backend configuration
cat <<EOF | kubectl apply -f -
apiVersion: cloud.google.com/v1
kind: BackendConfig
metadata:
  name: neobridge-backend-config
  namespace: neobridge
spec:
  healthCheck:
    checkIntervalSec: 30
    timeoutSec: 5
    healthyThreshold: 1
    unhealthyThreshold: 3
    type: HTTP
    requestPath: /actuator/health
    port: 8080
  connectionDraining:
    drainingTimeoutSec: 60
  timeoutSec: 30
  logging:
    enable: true
    sampleRate: 1.0
EOF

# Reserve static IP address
gcloud compute addresses create neobridge-ip \
  --global \
  --ip-version=IPV4

# Get the reserved IP address
gcloud compute addresses describe neobridge-ip \
  --global \
  --format="value(address)"
```

---

## 📊 **Step 9: Monitoring & Observability**

### **1️⃣ Deploy Prometheus & Grafana**
```bash
# Add Prometheus Helm repository
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

# Install Prometheus stack
helm install neobridge-monitoring prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --create-namespace \
  --set prometheus.prometheusSpec.storageSpec.volumeClaimTemplate.spec.storageClassName=standard-rwo \
  --set prometheus.prometheusSpec.storageSpec.volumeClaimTemplate.spec.resources.requests.storage=50Gi \
  --set grafana.persistence.enabled=true \
  --set grafana.persistence.size=10Gi \
  --set grafana.adminPassword=NeoBridgeAdmin123!

# Verify monitoring deployment
kubectl get pods -n monitoring
kubectl get services -n monitoring
```

### **2️⃣ Configure Cloud Monitoring**
```bash
# Enable Cloud Monitoring for GKE
gcloud container clusters update neobridge-cluster \
  --region=europe-west1 \
  --enable-stackdriver-kubernetes \
  --enable-vertical-pod-autoscaling

# Create custom dashboards
gcloud monitoring dashboards create \
  --project=neobridge-platform \
  --config-from-file=neobridge-dashboard.json

# Create alerting policies
gcloud alpha monitoring policies create \
  --project=neobridge-platform \
  --policy-from-file=neobridge-alerts.json
```

---

## 🔒 **Step 10: Security & Compliance**

### **1️⃣ Configure Network Policies**
```bash
# Create network policies for service isolation
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: neobridge-auth-network-policy
  namespace: neobridge
spec:
  podSelector:
    matchLabels:
      app: neobridge-auth-service
  policyTypes:
  - Ingress
  - Egress
  ingress:
  - from:
    - namespaceSelector:
        matchLabels:
          name: neobridge
    ports:
    - protocol: TCP
      port: 8080
  egress:
  - to:
    - namespaceSelector:
        matchLabels:
          name: monitoring
    ports:
    - protocol: TCP
      port: 9090
  - to: []
    ports:
    - protocol: TCP
      port: 53
EOF

# Create additional network policies for other services
# ... (similar policies for account, payment, crypto services)
```

### **2️⃣ Configure Pod Security Policies**
```bash
# Create Pod Security Policy
cat <<EOF | kubectl apply -f -
apiVersion: policy/v1beta1
kind: PodSecurityPolicy
metadata:
  name: neobridge-psp
spec:
  privileged: false
  allowPrivilegeEscalation: false
  requiredDropCapabilities:
  - ALL
  volumes:
  - 'configMap'
  - 'emptyDir'
  - 'projected'
  - 'secret'
  - 'downwardAPI'
  - 'persistentVolumeClaim'
  hostNetwork: false
  hostIPC: false
  hostPID: false
  runAsUser:
    rule: 'MustRunAsNonRoot'
  seLinux:
    rule: 'RunAsAny'
  supplementalGroups:
    rule: 'MustRunAs'
    ranges:
    - min: 1
      max: 65535
  fsGroup:
    rule: 'MustRunAs'
    ranges:
    - min: 1
      max: 65535
  readOnlyRootFilesystem: true
EOF
```

---

## 🧪 **Step 11: Testing & Validation**

### **1️⃣ Verify Service Health**
```bash
# Check all services are running
kubectl get pods -n neobridge
kubectl get services -n neobridge
kubectl get ingress -n neobridge

# Test service endpoints
kubectl port-forward svc/neobridge-auth-service 8080:80 -n neobridge
curl http://localhost:8080/actuator/health

# Test database connectivity
kubectl exec -it deployment/neobridge-auth-service -n neobridge -- \
  curl http://localhost:8080/actuator/health/db

# Test external connectivity
kubectl exec -it deployment/neobridge-account-service -n neobridge -- \
  curl http://localhost:8080/actuator/health
```

### **2️⃣ Load Testing**
```bash
# Install load testing tools
kubectl run load-test --image=busybox --rm -it --restart=Never -- \
  wget -qO- http://neobridge-auth-service/actuator/health

# Run performance tests
kubectl run k6-load-test --image=loadimpact/k6 --rm -it --restart=Never -- \
  k6 run -e BASE_URL=http://neobridge-auth-service /scripts/load-test.js
```

---

## 📋 **Step 12: Production Checklist**

### **✅ Infrastructure Verification**
- [ ] **GKE Cluster**: Running with 3+ nodes
- [ ] **Database**: Cloud SQL PostgreSQL operational
- [ ] **Cache**: Redis instance accessible
- [ ] **Storage**: Cloud Storage buckets configured
- [ ] **Load Balancer**: External IP assigned and working
- [ ] **Monitoring**: Prometheus and Grafana operational

### **✅ Security Verification**
- [ ] **Network Policies**: Service isolation configured
- [ ] **Pod Security**: Non-root containers enforced
- [ ] **Secrets**: All sensitive data in Secret Manager
- [ ] **TLS**: HTTPS enforced with valid certificates
- [ ] **IAM**: Service accounts properly configured
- [ ] **Backup**: Automated backups enabled

### **✅ Application Verification**
- [ ] **Services**: All microservices deployed and healthy
- [ ] **Health Checks**: Liveness and readiness probes working
- [ ] **Logging**: Centralized logging operational
- [ ] **Metrics**: Application metrics being collected
- [ ] **Alerts**: Monitoring alerts configured
- [ ] **Scaling**: Auto-scaling working properly

---

## 🚨 **Troubleshooting**

### **❌ Common Issues**

#### **Services Not Starting**
```bash
# Check pod status
kubectl get pods -n neobridge

# Check pod logs
kubectl logs -f deployment/neobridge-auth-service -n neobridge

# Check events
kubectl get events -n neobridge --sort-by='.lastTimestamp'

# Check resource usage
kubectl top pods -n neobridge
```

#### **Database Connection Issues**
```bash
# Verify Cloud SQL instance
gcloud sql instances describe neobridge-postgres

# Check network connectivity
kubectl exec -it deployment/neobridge-auth-service -n neobridge -- \
  nc -zv 10.0.0.3 5432

# Test database connection
kubectl exec -it deployment/neobridge-auth-service -n neobridge -- \
  psql -h 10.0.0.3 -U neobridge_auth_user -d neobridge_auth
```

#### **Load Balancer Issues**
```bash
# Check ingress status
kubectl describe ingress neobridge-ingress -n neobridge

# Verify backend services
kubectl get endpoints -n neobridge

# Check load balancer health
gcloud compute backend-services list
gcloud compute health-checks list
```

---

## 🏁 **Conclusion**

You've successfully **deployed the complete NeoBridge platform on Google Cloud Platform** with enterprise-grade infrastructure! The deployment includes:

- **☸️ Kubernetes orchestration** with auto-scaling and high availability
- **🗄️ Managed databases** with automated backups and monitoring
- **🌐 Load balancing** with SSL termination and global distribution
- **📊 Comprehensive monitoring** with Prometheus, Grafana, and Cloud Monitoring
- **🔒 Enterprise security** with network policies and pod security
- **📈 Auto-scaling** for optimal performance and cost management

**Your NeoBridge platform is now running in production on Google Cloud! 🚀**

---

<div align="center">

**NeoBridge Google Cloud Deployment**  
**Built by Mercuria Technologies for Harmony Q&Q GmbH**

</div>
