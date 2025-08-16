# 📊 NeoBridge Platform - Complete Monitoring Setup Guide

**Production-ready monitoring configuration with Prometheus, Grafana, and comprehensive alerting**

---

## 🎯 **Monitoring Overview**

The **NeoBridge Platform** implements enterprise-grade monitoring using **Prometheus**, **Grafana**, and **Alertmanager** with custom dashboards, alerting rules, and comprehensive metrics collection for all services.

### **🌟 Monitoring Features**
- **Real-time metrics** collection from all microservices
- **Custom dashboards** for business and technical KPIs
- **Automated alerting** with escalation policies
- **Performance tracking** with historical data analysis
- **Infrastructure monitoring** with resource utilization tracking

---

## 📈 **Prometheus Configuration**

### **⚙️ Main Prometheus Configuration**
```yaml
# prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s
  external_labels:
    cluster: neobridge-production
    environment: production

rule_files:
  - "rules/*.yml"

alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093

scrape_configs:
  # Prometheus itself
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']

  # NeoBridge Services
  - job_name: 'neobridge-services'
    kubernetes_sd_configs:
      - role: pod
        namespaces:
          names:
            - neobridge
    relabel_configs:
      - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_scrape]
        action: keep
        regex: true
      - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_path]
        action: replace
        target_label: __metrics_path__
        regex: (.+)
      - source_labels: [__address__, __meta_kubernetes_pod_annotation_prometheus_io_port]
        action: replace
        regex: ([^:]+)(?::\d+)?;(\d+)
        replacement: $1:$2
        target_label: __address__
      - source_labels: [__meta_kubernetes_namespace]
        action: replace
        target_label: kubernetes_namespace
      - source_labels: [__meta_kubernetes_pod_name]
        action: replace
        target_label: kubernetes_pod_name
      - source_labels: [__meta_kubernetes_pod_label_app]
        action: replace
        target_label: app

  # Kubernetes API Server
  - job_name: 'kubernetes-apiservers'
    kubernetes_sd_configs:
      - role: endpoints
    scheme: https
    tls_config:
      ca_file: /var/run/secrets/kubernetes.io/serviceaccount/ca.crt
      insecure_skip_verify: true
    bearer_token_file: /var/run/secrets/kubernetes.io/serviceaccount/token
    relabel_configs:
      - source_labels: [__meta_kubernetes_namespace, __meta_kubernetes_service_name, __meta_kubernetes_endpoint_port_name]
        action: keep
        regex: default;kubernetes;https

  # Kubernetes Nodes
  - job_name: 'kubernetes-nodes'
    kubernetes_sd_configs:
      - role: node
    scheme: https
    tls_config:
      ca_file: /var/run/secrets/kubernetes.io/serviceaccount/ca.crt
      insecure_skip_verify: true
    bearer_token_file: /var/run/secrets/kubernetes.io/serviceaccount/token
    relabel_configs:
      - action: labelmap
        regex: __meta_kubernetes_node_label_(.+)

  # Kubernetes Pods
  - job_name: 'kubernetes-pods'
    kubernetes_sd_configs:
      - role: pod
    relabel_configs:
      - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_scrape]
        action: keep
        regex: true
      - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_path]
        action: replace
        target_label: __metrics_path__
        regex: (.+)
      - source_labels: [__address__, __meta_kubernetes_pod_annotation_prometheus_io_port]
        action: replace
        regex: ([^:]+)(?::\d+)?;(\d+)
        replacement: $1:$2
        target_label: __address__
      - action: labelmap
        regex: __meta_kubernetes_pod_label_(.+)
      - source_labels: [__meta_kubernetes_namespace]
        action: replace
        target_label: kubernetes_namespace
      - source_labels: [__meta_kubernetes_pod_name]
        action: replace
        target_label: kubernetes_pod_name

  # NeoBridge Database
  - job_name: 'neobridge-postgres'
    static_configs:
      - targets: ['10.0.0.3:5432']
    metrics_path: /metrics
    scrape_interval: 30s

  # NeoBridge Redis
  - job_name: 'neobridge-redis'
    static_configs:
      - targets: ['10.0.0.4:6379']
    metrics_path: /metrics
    scrape_interval: 30s

  # NeoBridge Kafka
  - job_name: 'neobridge-kafka'
    static_configs:
      - targets: ['10.0.0.5:9092']
    metrics_path: /metrics
    scrape_interval: 30s
```

---

## 📊 **Custom Metrics & Rules**

### **📋 NeoBridge Business Metrics**
```yaml
# rules/neobridge-business.yml
groups:
  - name: neobridge-business
    rules:
      # User Registration Metrics
      - record: neobridge_users_registered_total
        expr: sum(increase(neobridge_auth_registrations_total[24h])) by (status)
      
      - record: neobridge_users_active_total
        expr: sum(neobridge_users_status{status="ACTIVE"})
      
      # Account Creation Metrics
      - record: neobridge_accounts_created_total
        expr: sum(increase(neobridge_accounts_created_total[24h])) by (type, currency)
      
      - record: neobridge_accounts_balance_total
        expr: sum(neobridge_accounts_balance) by (currency)
      
      # Payment Processing Metrics
      - record: neobridge_payments_processed_total
        expr: sum(increase(neobridge_payments_processed_total[24h])) by (type, status)
      
      - record: neobridge_payments_amount_total
        expr: sum(neobridge_payments_amount) by (type, currency)
      
      # Transaction Volume Metrics
      - record: neobridge_transactions_volume_24h
        expr: sum(increase(neobridge_transactions_total[24h])) by (type)
      
      - record: neobridge_transactions_value_24h
        expr: sum(increase(neobridge_transactions_value[24h])) by (currency)
      
      # Risk Assessment Metrics
      - record: neobridge_risk_assessments_total
        expr: sum(increase(neobridge_risk_assessments_total[24h])) by (risk_level)
      
      - record: neobridge_fraud_detections_total
        expr: sum(increase(neobridge_fraud_detections_total[24h])) by (type)
      
      # Performance Metrics
      - record: neobridge_api_response_time_p95
        expr: histogram_quantile(0.95, sum(rate(http_request_duration_seconds_bucket[5m])) by (le, service))
      
      - record: neobridge_api_error_rate
        expr: sum(rate(http_requests_total{status=~"5.."}[5m])) by (service) / sum(rate(http_requests_total[5m])) by (service)
      
      # Infrastructure Metrics
      - record: neobridge_pod_memory_usage_percent
        expr: (container_memory_usage_bytes{container!=""} / container_spec_memory_limit_bytes{container!=""}) * 100
      
      - record: neobridge_pod_cpu_usage_percent
        expr: (rate(container_cpu_usage_seconds_total{container!=""}[5m]) / container_spec_cpu_quota{container!=""}) * 100
```

### **🚨 Alerting Rules**
```yaml
# rules/neobridge-alerts.yml
groups:
  - name: neobridge-alerts
    rules:
      # High Error Rate Alerts
      - alert: HighAPIErrorRate
        expr: neobridge_api_error_rate > 0.05
        for: 5m
        labels:
          severity: warning
          service: api
        annotations:
          summary: "High API error rate detected"
          description: "API error rate is {{ $value | humanizePercentage }} for the last 5 minutes"
          runbook_url: "https://docs.neobridge.com/runbooks/high-api-error-rate"
      
      # High Response Time Alerts
      - alert: HighAPIResponseTime
        expr: neobridge_api_response_time_p95 > 1.0
        for: 5m
        labels:
          severity: warning
          service: api
        annotations:
          summary: "High API response time detected"
          description: "95th percentile response time is {{ $value }}s for the last 5 minutes"
          runbook_url: "https://docs.neobridge.com/runbooks/high-api-response-time"
      
      # Service Down Alerts
      - alert: ServiceDown
        expr: up{job="neobridge-services"} == 0
        for: 1m
        labels:
          severity: critical
          service: "{{ $labels.app }}"
        annotations:
          summary: "Service {{ $labels.app }} is down"
          description: "Service {{ $labels.app }} has been down for more than 1 minute"
          runbook_url: "https://docs.neobridge.com/runbooks/service-down"
      
      # High Memory Usage Alerts
      - alert: HighMemoryUsage
        expr: neobridge_pod_memory_usage_percent > 85
        for: 5m
        labels:
          severity: warning
          service: "{{ $labels.app }}"
        annotations:
          summary: "High memory usage detected"
          description: "Pod {{ $labels.pod }} is using {{ $value | humanizePercentage }} memory"
          runbook_url: "https://docs.neobridge.com/runbooks/high-memory-usage"
      
      # High CPU Usage Alerts
      - alert: HighCPUUsage
        expr: neobridge_pod_cpu_usage_percent > 80
        for: 5m
        labels:
          severity: warning
          service: "{{ $labels.app }}"
        annotations:
          summary: "High CPU usage detected"
          description: "Pod {{ $labels.pod }} is using {{ $value | humanizePercentage }} CPU"
          runbook_url: "https://docs.neobridge.com/runbooks/high-cpu-usage"
      
      # Database Connection Alerts
      - alert: DatabaseConnectionIssues
        expr: pg_up == 0
        for: 1m
        labels:
          severity: critical
          service: database
        annotations:
          summary: "Database connection issues detected"
          description: "Cannot connect to PostgreSQL database"
          runbook_url: "https://docs.neobridge.com/runbooks/database-connection-issues"
      
      # Redis Connection Alerts
      - alert: RedisConnectionIssues
        expr: redis_up == 0
        for: 1m
        labels:
          severity: critical
          service: redis
        annotations:
          summary: "Redis connection issues detected"
          description: "Cannot connect to Redis cache"
          runbook_url: "https://docs.neobridge.com/runbooks/redis-connection-issues"
      
      # Payment Processing Alerts
      - alert: PaymentProcessingIssues
        expr: rate(neobridge_payments_failed_total[5m]) > 0.1
        for: 2m
        labels:
          severity: critical
          service: payments
        annotations:
          summary: "Payment processing issues detected"
          description: "High rate of failed payments detected"
          runbook_url: "https://docs.neobridge.com/runbooks/payment-processing-issues"
      
      # Fraud Detection Alerts
      - alert: HighFraudDetectionRate
        expr: rate(neobridge_fraud_detections_total[5m]) > 0.05
        for: 2m
        labels:
          severity: critical
          service: security
        annotations:
          summary: "High fraud detection rate"
          description: "Unusually high rate of fraud detections"
          runbook_url: "https://docs.neobridge.com/runbooks/high-fraud-detection-rate"
```

---

## 📊 **Grafana Dashboard Configuration**

### **🏦 NeoBridge Business Dashboard**
```json
{
  "dashboard": {
    "id": null,
    "title": "NeoBridge Business Overview",
    "tags": ["neobridge", "business", "overview"],
    "timezone": "browser",
    "panels": [
      {
        "id": 1,
        "title": "User Registration Trends",
        "type": "graph",
        "targets": [
          {
            "expr": "neobridge_users_registered_total",
            "legendFormat": "{{status}}"
          }
        ],
        "fieldConfig": {
          "defaults": {
            "color": {
              "mode": "palette-classic"
            },
            "custom": {
              "axisLabel": "",
              "axisPlacement": "auto",
              "barAlignment": 0,
              "drawStyle": "line",
              "fillOpacity": 10,
              "gradientMode": "none",
              "hideFrom": {
                "legend": false,
                "tooltip": false,
                "vis": false
              },
              "lineInterpolation": "linear",
              "lineWidth": 1,
              "pointSize": 5,
              "scaleDistribution": {
                "type": "linear"
              },
              "showPoints": "never",
              "spanNulls": false,
              "stacking": {
                "group": "A",
                "mode": "none"
              },
              "thresholdsStyle": {
                "mode": "off"
              }
            },
            "mappings": [],
            "thresholds": {
              "mode": "absolute",
              "steps": [
                {
                  "color": "green",
                  "value": null
                }
              ]
            },
            "unit": "short"
          }
        },
        "gridPos": {
          "h": 8,
          "w": 12,
          "x": 0,
          "y": 0
        }
      },
      {
        "id": 2,
        "title": "Account Balance by Currency",
        "type": "stat",
        "targets": [
          {
            "expr": "neobridge_accounts_balance_total",
            "legendFormat": "{{currency}}"
          }
        ],
        "fieldConfig": {
          "defaults": {
            "color": {
              "mode": "thresholds"
            },
            "custom": {
              "align": "auto",
              "cellOptions": {
                "type": "auto"
              },
              "inspect": false
            },
            "mappings": [],
            "thresholds": {
              "mode": "absolute",
              "steps": [
                {
                  "color": "green",
                  "value": null
                }
              ]
            },
            "unit": "currencyEUR"
          }
        },
        "gridPos": {
          "h": 8,
          "w": 12,
          "x": 12,
          "y": 0
        }
      },
      {
        "id": 3,
        "title": "Payment Processing Volume",
        "type": "graph",
        "targets": [
          {
            "expr": "neobridge_payments_processed_total",
            "legendFormat": "{{type}} - {{status}}"
          }
        ],
        "fieldConfig": {
          "defaults": {
            "color": {
              "mode": "palette-classic"
            },
            "custom": {
              "axisLabel": "",
              "axisPlacement": "auto",
              "barAlignment": 0,
              "drawStyle": "bars",
              "fillOpacity": 100,
              "gradientMode": "none",
              "hideFrom": {
                "legend": false,
                "tooltip": false,
                "vis": false
              },
              "lineInterpolation": "linear",
              "lineWidth": 1,
              "pointSize": 5,
              "scaleDistribution": {
                "type": "linear"
              },
              "showPoints": "never",
              "spanNulls": false,
              "stacking": {
                "group": "A",
                "mode": "normal"
              },
              "thresholdsStyle": {
                "mode": "off"
              }
            },
            "mappings": [],
            "thresholds": {
              "mode": "absolute",
              "steps": [
                {
                  "color": "green",
                  "value": null
                }
              ]
            },
            "unit": "short"
          }
        },
        "gridPos": {
          "h": 8,
          "w": 24,
          "x": 0,
          "y": 8
        }
      }
    ],
    "time": {
      "from": "now-24h",
      "to": "now"
    },
    "refresh": "30s"
  }
}
```

---

## 🔔 **Alertmanager Configuration**

### **⚙️ Alertmanager Rules**
```yaml
# alertmanager.yml
global:
  resolve_timeout: 5m
  slack_api_url: 'https://hooks.slack.com/services/YOUR_SLACK_WEBHOOK'
  smtp_smarthost: 'smtp.gmail.com:587'
  smtp_from: 'alerts@neobridge.com'
  smtp_auth_username: 'alerts@neobridge.com'
  smtp_auth_password: 'YOUR_SMTP_PASSWORD'

route:
  group_by: ['alertname', 'cluster', 'service']
  group_wait: 30s
  group_interval: 5m
  repeat_interval: 4h
  receiver: 'slack-notifications'
  routes:
    - match:
        severity: critical
      receiver: 'pager-duty-critical'
      continue: true
    - match:
        severity: warning
      receiver: 'slack-notifications'
    - match:
        service: payments
      receiver: 'payment-team'
      continue: true
    - match:
        service: security
      receiver: 'security-team'
      continue: true

receivers:
  - name: 'slack-notifications'
    slack_configs:
      - channel: '#neobridge-alerts'
        title: '{{ template "slack.title" . }}'
        text: '{{ template "slack.text" . }}'
        send_resolved: true
        actions:
          - type: button
            text: 'View in Grafana'
            url: '{{ template "slack.grafana" . }}'
          - type: button
            text: 'Runbook'
            url: '{{ template "slack.runbook" . }}'

  - name: 'pager-duty-critical'
    pagerduty_configs:
      - routing_key: 'YOUR_PAGERDUTY_KEY'
        description: '{{ template "pagerduty.description" . }}'
        client: 'NeoBridge Platform'
        client_url: '{{ template "pagerduty.clientURL" . }}'
        severity: '{{ if eq .GroupLabels.severity "critical" }}critical{{ else }}warning{{ end }}'

  - name: 'payment-team'
    slack_configs:
      - channel: '#neobridge-payments'
        title: '{{ template "slack.title" . }}'
        text: '{{ template "slack.text" . }}'
        send_resolved: true

  - name: 'security-team'
    slack_configs:
      - channel: '#neobridge-security'
        title: '{{ template "slack.title" . }}'
        text: '{{ template "slack.text" . }}'
        send_resolved: true

templates:
  - '/etc/alertmanager/template/*.tmpl'

inhibit_rules:
  - source_match:
      severity: 'critical'
    target_match:
      severity: 'warning'
    equal: ['alertname', 'cluster', 'service']
```

---

## 🚀 **Deployment Commands**

### **📋 Deploy Monitoring Stack**
```bash
# Create monitoring namespace
kubectl create namespace monitoring

# Deploy Prometheus
kubectl apply -f prometheus-configmap.yaml
kubectl apply -f prometheus-deployment.yaml
kubectl apply -f prometheus-service.yaml

# Deploy Alertmanager
kubectl apply -f alertmanager-configmap.yaml
kubectl apply -f alertmanager-deployment.yaml
kubectl apply -f alertmanager-service.yaml

# Deploy Grafana
kubectl apply -f grafana-configmap.yaml
kubectl apply -f grafana-deployment.yaml
kubectl apply -f grafana-service.yaml

# Deploy custom rules
kubectl apply -f rules/

# Verify deployment
kubectl get pods -n monitoring
kubectl get services -n monitoring
kubectl get configmaps -n monitoring
```

---

## 🏁 **Conclusion**

The **NeoBridge Platform monitoring setup** provides comprehensive observability with real-time metrics, automated alerting, and custom dashboards. Built for production operations, it ensures proactive issue detection and resolution.

**Key monitoring strengths:**
- **Real-time metrics** collection from all services
- **Custom business dashboards** for KPIs and insights
- **Automated alerting** with escalation policies
- **Performance tracking** with historical analysis
- **Infrastructure monitoring** with resource utilization

**Ready for production monitoring! 📊🚀**

---

<div align="center">

**NeoBridge Monitoring Setup**  
**Built by Mercuria Technologies for Harmony Q&Q GmbH**

</div>
