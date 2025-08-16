# NeoBridge Platform - Production Deployment Guide

## 🚀 Overview

This guide provides comprehensive instructions for deploying the NeoBridge platform to Google Cloud Platform (GCP) for production use. The platform includes all core banking services, advanced features, and enterprise-grade security.

## 📋 Prerequisites

### Required Accounts & Services
- **Google Cloud Platform Account**: `mks.alghafil@gmail.com`
- **Google Cloud Billing Account**: Active billing with sufficient credits
- **Domain Name**: For production URLs and SSL certificates
- **External API Keys**: For third-party integrations

### Required Tools
- **Google Cloud CLI (gcloud)**: Latest version
- **kubectl**: Kubernetes command-line tool
- **Terraform**: Infrastructure as Code tool
- **Docker**: For container management
- **Git**: For source code management

## 🏗️ Infrastructure Architecture

### Google Cloud Services Used
```
┌─────────────────────────────────────────────────────────────┐
│                    Google Cloud Platform                    │
├─────────────────────────────────────────────────────────────┤
│  Load Balancer (HTTPS) → API Gateway (Kong)               │
│                    ↓                                       │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Google Kubernetes Engine (GKE)         │   │
│  │  ┌─────────────┬─────────────┬─────────────┬─────┐  │   │
│  │  │ Auth Service│Account Svc │Payment Svc  │ ... │  │   │
│  │  └─────────────┴─────────────┴─────────────┴─────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
│                    ↓                                       │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Cloud SQL (PostgreSQL)                │   │
│  │              Cloud Memorystore (Redis)             │   │
│  │              Cloud Storage (Backups)               │   │
│  └─────────────────────────────────────────────────────┘   │
│                    ↓                                       │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Monitoring & Logging                   │   │
│  │  Prometheus + Grafana + Cloud Logging              │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## 🔧 Step-by-Step Deployment

### Phase 1: Google Cloud Project Setup

#### 1.1 Create New GCP Project
```bash
# Set project details
export PROJECT_ID="neobridge-production"
export PROJECT_NAME="NeoBridge Production Platform"
export BILLING_ACCOUNT="$(gcloud billing accounts list --format='value(ACCOUNT_ID)')"

# Create project
gcloud projects create $PROJECT_ID --name="$PROJECT_NAME"

# Set project as default
gcloud config set project $PROJECT_ID

# Link billing account
gcloud billing projects link $PROJECT_ID --billing-account=$BILLING_ACCOUNT

# Enable required APIs
gcloud services enable \
    compute.googleapis.com \
    container.googleapis.com \
    sqladmin.googleapis.com \
    redis.googleapis.com \
    storage.googleapis.com \
    monitoring.googleapis.com \
    logging.googleapis.com \
    secretmanager.googleapis.com \
    cloudkms.googleapis.com \
    iam.googleapis.com
```

#### 1.2 Configure IAM & Service Accounts
```bash
# Create service accounts
gcloud iam service-accounts create neobridge-platform \
    --display-name="NeoBridge Platform Service Account"

gcloud iam service-accounts create neobridge-database \
    --display-name="NeoBridge Database Service Account"

gcloud iam service-accounts create neobridge-monitoring \
    --display-name="NeoBridge Monitoring Service Account"

# Grant necessary roles
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:neobridge-platform@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/container.admin"

gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:neobridge-database@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/cloudsql.admin"

gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:neobridge-monitoring@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/monitoring.admin"
```

### Phase 2: Database & Storage Setup

#### 2.1 Create Cloud SQL Instance
```bash
# Create PostgreSQL instance
gcloud sql instances create neobridge-postgres \
    --database-version=POSTGRES_16 \
    --tier=db-custom-4-16 \
    --storage-type=SSD \
    --storage-size=100GB \
    --backup-start-time=02:00 \
    --backup-window=4 \
    --maintenance-window-day=SUN \
    --maintenance-window-hour=03 \
    --region=us-central1 \
    --root-password="$(openssl rand -base64 32)"

# Create databases
gcloud sql databases create neobridge_prod --instance=neobridge-postgres
gcloud sql databases create neobridge_analytics --instance=neobridge-postgres
gcloud sql databases create neobridge_audit --instance=neobridge-postgres

# Create users
gcloud sql users create neobridge_user \
    --instance=neobridge-postgres \
    --password="$(openssl rand -base64 32)"

gcloud sql users create neobridge_readonly \
    --instance=neobridge-postgres \
    --password="$(openssl rand -base64 32)"
```

#### 2.2 Create Cloud Memorystore (Redis)
```bash
# Create Redis instance
gcloud redis instances create neobridge-redis \
    --size=5 \
    --region=us-central1 \
    --redis-version=redis_7_0 \
    --tier=STANDARD_HA
```

#### 2.3 Create Cloud Storage Buckets
```bash
# Create storage buckets
gsutil mb -l us-central1 gs://neobridge-backups
gsutil mb -l us-central1 gs://neobridge-logs
gsutil mb -l us-central1 gs://neobridge-exports
gsutil mb -l us-central1 gs://neobridge-nft-storage

# Set lifecycle policies
gsutil lifecycle set lifecycle-policy.json gs://neobridge-backups
gsutil lifecycle set lifecycle-policy.json gs://neobridge-logs
```

### Phase 3: Kubernetes Cluster Setup

#### 3.1 Create GKE Cluster
```bash
# Create GKE cluster
gcloud container clusters create neobridge-cluster \
    --zone=us-central1-a \
    --num-nodes=3 \
    --min-nodes=3 \
    --max-nodes=10 \
    --machine-type=e2-standard-4 \
    --disk-size=100 \
    --disk-type=pd-ssd \
    --enable-autoscaling \
    --enable-autorepair \
    --enable-autoupgrade \
    --enable-network-policy \
    --enable-ip-alias \
    --create-subnetwork="" \
    --network=default \
    --addons=HttpLoadBalancing,HorizontalPodAutoscaling \
    --service-account=neobridge-platform@$PROJECT_ID.iam.gserviceaccount.com

# Get cluster credentials
gcloud container clusters get-credentials neobridge-cluster --zone=us-central1-a

# Verify cluster
kubectl cluster-info
kubectl get nodes
```

#### 3.2 Configure Cluster Add-ons
```bash
# Enable Workload Identity
gcloud container clusters update neobridge-cluster \
    --zone=us-central1-a \
    --workload-pool=$PROJECT_ID.svc.id.goog

# Install Helm
curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3.sh | bash

# Add Helm repositories
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo add kong https://charts.konghq.com
helm repo update
```

### Phase 4: Application Deployment

#### 4.1 Create Kubernetes Namespaces
```bash
# Create namespaces
kubectl create namespace neobridge-platform
kubectl create namespace neobridge-monitoring
kubectl create namespace neobridge-database

# Set default namespace
kubectl config set-context --current --namespace=neobridge-platform
```

#### 4.2 Deploy Core Infrastructure
```bash
# Deploy Kong API Gateway
helm install kong kong/kong \
    --namespace neobridge-platform \
    --set ingressController.installCRDs=false \
    --set proxy.type=LoadBalancer \
    --set proxy.loadBalancer.annotations."service\.beta\.kubernetes\.io/google-load-balancer-type"="External"

# Deploy PostgreSQL operator (if using)
kubectl apply -f k8s/postgres-operator/

# Deploy Redis operator (if using)
kubectl apply -f k8s/redis-operator/
```

#### 4.3 Deploy NeoBridge Services
```bash
# Deploy common infrastructure
kubectl apply -f k8s/common/

# Deploy core services
kubectl apply -f k8s/auth-service/
kubectl apply -f k8s/account-service/
kubectl apply -f k8s/payment-service/
kubectl apply -f k8s/crypto-service/
kubectl apply -f k8s/investment-service/

# Deploy advanced services
kubectl apply -f k8s/ai-risk-service/
kubectl apply -f k8s/nft-marketplace/
kubectl apply -f k8s/institutional-custody/
kubectl apply -f k8s/analytics-dashboard/

# Deploy supporting services
kubectl apply -f k8s/notification-service/
kubectl apply -f k8s/compliance-service/
kubectl apply -f k8s/admin-service/
```

#### 4.4 Deploy Monitoring Stack
```bash
# Deploy Prometheus
helm install prometheus prometheus-community/kube-prometheus-stack \
    --namespace neobridge-monitoring \
    --set grafana.enabled=true \
    --set grafana.adminPassword="$(openssl rand -base64 32)"

# Deploy Grafana dashboards
kubectl apply -f k8s/monitoring/grafana-dashboards/

# Deploy logging stack
kubectl apply -f k8s/monitoring/logging/
```

### Phase 5: Configuration & Secrets

#### 5.1 Create Secret Manager Secrets
```bash
# Database credentials
echo -n "$(openssl rand -base64 32)" | \
gcloud secrets create neobridge-db-password --data-file=-

# JWT secrets
echo -n "$(openssl rand -base64 64)" | \
gcloud secrets create neobridge-jwt-secret --data-file=-

# API keys
echo -n "your-api-key-here" | \
gcloud secrets create neobridge-external-api-key --data-file=-

# Blockchain private keys
echo -n "your-blockchain-key-here" | \
gcloud secrets create neobridge-blockchain-key --data-file=-
```

#### 5.2 Configure Environment Variables
```bash
# Create ConfigMap for common configuration
kubectl create configmap neobridge-config \
    --from-file=config/application-common.yml \
    --from-file=config/application-prod.yml \
    --namespace=neobridge-platform

# Create ConfigMap for service-specific configuration
kubectl create configmap auth-service-config \
    --from-file=config/auth-service/ \
    --namespace=neobridge-platform
```

### Phase 6: Load Balancer & SSL

#### 6.1 Configure Load Balancer
```bash
# Create static IP
gcloud compute addresses create neobridge-ip \
    --region=us-central1

# Get IP address
export LOAD_BALANCER_IP=$(gcloud compute addresses describe neobridge-ip \
    --region=us-central1 --format='value(address)')

echo "Load Balancer IP: $LOAD_BALANCER_IP"
```

#### 6.2 Configure SSL Certificate
```bash
# Create SSL certificate (using Let's Encrypt or Google-managed)
gcloud compute ssl-certificates create neobridge-ssl-cert \
    --domains=your-domain.com,www.your-domain.com \
    --global

# Or use Let's Encrypt with cert-manager
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.12.0/cert-manager.yaml
kubectl apply -f k8s/cert-manager/
```

### Phase 7: Testing & Validation

#### 7.1 Health Checks
```bash
# Check all pods are running
kubectl get pods -n neobridge-platform
kubectl get pods -n neobridge-monitoring

# Check services
kubectl get services -n neobridge-platform
kubectl get ingress -n neobridge-platform

# Check database connectivity
kubectl exec -it deployment/neobridge-auth-service -- \
    curl -f http://localhost:8080/actuator/health
```

#### 7.2 Performance Testing
```bash
# Run load tests
kubectl apply -f k8s/testing/load-test/

# Monitor performance
kubectl port-forward svc/prometheus-grafana 3000:80 -n neobridge-monitoring
# Open http://localhost:3000 in browser
```

### Phase 8: Production Hardening

#### 8.1 Security Configuration
```bash
# Enable network policies
kubectl apply -f k8s/security/network-policies/

# Configure RBAC
kubectl apply -f k8s/security/rbac/

# Enable pod security policies
kubectl apply -f k8s/security/pod-security-policies/
```

#### 8.2 Backup & Disaster Recovery
```bash
# Configure automated backups
kubectl apply -f k8s/backup/backup-cronjob/

# Test backup restoration
kubectl apply -f k8s/backup/restore-test/
```

## 🔍 Monitoring & Maintenance

### Health Monitoring
- **Grafana Dashboards**: http://your-domain.com/grafana
- **Prometheus Metrics**: http://your-domain.com/prometheus
- **Kong Admin API**: http://your-domain.com:8001

### Logging
- **Cloud Logging**: View logs in GCP Console
- **Application Logs**: Centralized logging with ELK stack

### Alerts
- **Prometheus Alerts**: Configured for critical metrics
- **Cloud Monitoring**: GCP-native alerting
- **Email/SMS Notifications**: For critical issues

## 🚨 Troubleshooting

### Common Issues
1. **Pod Startup Issues**: Check resource limits and environment variables
2. **Database Connection**: Verify Cloud SQL proxy and credentials
3. **Memory Issues**: Monitor resource usage and adjust limits
4. **Network Issues**: Check network policies and service mesh

### Debug Commands
```bash
# Check pod logs
kubectl logs -f deployment/neobridge-auth-service

# Check pod events
kubectl describe pod <pod-name>

# Check service endpoints
kubectl get endpoints -n neobridge-platform

# Check ingress status
kubectl describe ingress neobridge-ingress
```

## 📚 Additional Resources

- **NeoBridge Documentation**: `/Docs/` directory
- **Kubernetes Documentation**: https://kubernetes.io/docs/
- **Google Cloud Documentation**: https://cloud.google.com/docs/
- **Helm Charts**: https://helm.sh/docs/

## 🎯 Next Steps

After successful deployment:
1. **Configure DNS**: Point domain to Load Balancer IP
2. **Set up CI/CD**: Automated deployment pipeline
3. **Performance Tuning**: Optimize based on load testing
4. **Security Audit**: Penetration testing and security review
5. **Compliance Verification**: Regulatory compliance checks
6. **Client Onboarding**: Begin institutional client setup

---

**🎉 Congratulations! Your NeoBridge platform is now deployed to Google Cloud! 🎉**
