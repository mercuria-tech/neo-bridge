# NeoBridge Platform - Monitoring Infrastructure

Comprehensive monitoring solution for the NeoBridge platform, providing real-time visibility into system health, performance, and alerts.

## 🌟 Features

### **Core Monitoring Components**
- **Prometheus**: Time-series database for metrics collection and storage
- **Grafana**: Visualization and dashboard platform
- **Alertmanager**: Alert routing and notification management
- **Custom Dashboards**: NeoBridge-specific monitoring views

### **Monitoring Coverage**
- **Platform Services**: Authentication, Account, Payment, Crypto, Admin services
- **Infrastructure**: Database, Redis, Kafka, Elasticsearch, Kong API Gateway
- **System Metrics**: CPU, Memory, Disk, Network, Load
- **Application Metrics**: Request rates, response times, error rates, throughput

### **Alerting & Notifications**
- **Multi-level Alerts**: Critical, Warning, Info
- **Team-based Routing**: Database, Services, Infrastructure teams
- **Notification Channels**: Email, Webhook, Slack integration
- **Alert Inhibition**: Smart alert grouping and suppression

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   NeoBridge     │    │   Kong API      │    │   Infrastructure│
│   Services      │    │   Gateway       │    │   Services      │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │      Prometheus          │
                    │   (Metrics Collection)   │
                    └─────────────┬─────────────┘
                                 │
          ┌──────────────────────┼──────────────────────┐
          │                      │                      │
┌─────────▼─────────┐  ┌─────────▼─────────┐  ┌─────────▼─────────┐
│    Grafana        │  │   Alertmanager    │  │   Custom Rules    │
│  (Dashboards)     │  │  (Alert Routing)  │  │   (Monitoring)    │
└───────────────────┘  └───────────────────┘  └───────────────────┘
```

## 🚀 Quick Start

### **1. Automated Setup**
```bash
# Run the monitoring setup script
./monitoring/setup-monitoring.sh
```

### **2. Manual Setup**
```bash
# Create monitoring directories
mkdir -p monitoring/{prometheus,grafana,alertmanager,logs}

# Start monitoring services
cd monitoring
docker-compose up -d
```

### **3. Verify Setup**
```bash
# Check Prometheus
curl http://localhost:9090/-/healthy

# Check Alertmanager
curl http://localhost:9093/-/healthy

# Check Grafana
curl http://localhost:3000/api/health
```

## 📊 Monitoring Services

### **Prometheus (Port 9090)**
- **Purpose**: Metrics collection and storage
- **Features**: Time-series database, query language, alerting rules
- **Access**: http://localhost:9090

### **Grafana (Port 3000)**
- **Purpose**: Visualization and dashboards
- **Features**: Custom dashboards, alert management, user management
- **Access**: http://localhost:3000
- **Login**: admin / admin

### **Alertmanager (Port 9093)**
- **Purpose**: Alert routing and notification
- **Features**: Alert grouping, inhibition, notification channels
- **Access**: http://localhost:9093

## 📈 Key Metrics

### **Service Metrics**
- `up` - Service availability (0 = down, 1 = up)
- `http_requests_total` - Total HTTP requests
- `http_request_duration_seconds` - Request response time
- `http_requests_total{status=~"4..|5.."}` - Error requests

### **System Metrics**
- `node_cpu_seconds_total` - CPU usage
- `node_memory_MemTotal_bytes` - Memory usage
- `node_filesystem_avail_bytes` - Disk space
- `node_load1/5/15` - System load averages

### **Database Metrics**
- `pg_stat_database_numbackends` - Active connections
- `pg_stat_database_xact_commit` - Transaction commits
- `pg_stat_database_blks_read` - Blocks read

### **Redis Metrics**
- `redis_connected_clients` - Connected clients
- `redis_memory_used_bytes` - Memory usage
- `redis_commands_processed_total` - Commands processed

## 🚨 Alerting Rules

### **Critical Alerts**
- **PlatformServiceDown**: Service unavailable for >1 minute
- **LowDiskSpace**: Disk space below 10%
- **ElasticsearchClusterRed**: Cluster health critical

### **Warning Alerts**
- **HighErrorRate**: Error rate >10% for 2 minutes
- **HighLatency**: 95th percentile latency >1 second
- **HighMemoryUsage**: Memory usage >80%
- **HighCPUUsage**: CPU usage >80%

### **Alert Routing**
- **Critical Alerts**: Immediate notification to dev team
- **Warning Alerts**: Delayed notification (30s grouping)
- **Service Alerts**: Route to service team
- **Infrastructure Alerts**: Route to infrastructure team

## 📋 Dashboard Panels

### **Platform Overview**
- Service health status
- Request rates and trends
- Response time percentiles
- Error rates and patterns

### **Performance Metrics**
- Memory usage by service
- CPU utilization
- Database connections
- Cache hit rates

### **Infrastructure Health**
- System load averages
- Disk usage and I/O
- Network performance
- Service dependencies

### **Business Metrics**
- User authentication rates
- Transaction volumes
- Payment processing
- Crypto operations

## ⚙️ Configuration

### **Prometheus Configuration**
```yaml
# monitoring/prometheus/prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'neobridge-services'
    static_configs:
      - targets: ['service:port']
    metrics_path: /actuator/prometheus
```

### **Alertmanager Configuration**
```yaml
# monitoring/alertmanager/alertmanager.yml
route:
  group_by: ['alertname', 'service', 'severity']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 1h

receivers:
  - name: 'critical-alerts'
    webhook_configs:
      - url: 'http://localhost:5001/critical'
```

### **Grafana Configuration**
```yaml
# monitoring/grafana/provisioning/datasources/prometheus.yml
datasources:
  prometheus:
    name: Prometheus
    type: prometheus
    url: http://prometheus:9090
    isDefault: true
```

## 🔧 Customization

### **Adding New Services**
1. Update `prometheus.yml` with new scrape targets
2. Add service-specific alerting rules
3. Create custom Grafana dashboards
4. Configure notification routing

### **Custom Metrics**
1. Implement Prometheus client in your service
2. Expose metrics at `/actuator/prometheus`
3. Define custom alerting rules
4. Add to monitoring dashboards

### **Notification Channels**
1. Configure SMTP for email alerts
2. Set up webhook endpoints
3. Integrate with Slack/Teams
4. Customize alert templates

## 📚 Monitoring Best Practices

### **Metric Design**
- Use descriptive metric names
- Include relevant labels
- Avoid high-cardinality labels
- Use appropriate metric types

### **Alerting Strategy**
- Set meaningful thresholds
- Use appropriate alert durations
- Group related alerts
- Provide actionable descriptions

### **Dashboard Design**
- Focus on key metrics
- Use appropriate visualizations
- Include context and thresholds
- Regular review and updates

## 🛠️ Troubleshooting

### **Common Issues**

#### **1. Prometheus Not Scraping**
```bash
# Check service endpoints
curl http://service:port/actuator/prometheus

# Verify network connectivity
docker network ls
docker network inspect neobridge-network
```

#### **2. Grafana No Data**
```bash
# Check datasource configuration
curl http://localhost:3000/api/datasources

# Verify Prometheus connectivity
curl http://prometheus:9090/api/v1/targets
```

#### **3. Alerts Not Firing**
```bash
# Check alerting rules
curl http://localhost:9090/api/v1/rules

# Verify Alertmanager configuration
curl http://localhost:9093/api/v1/status
```

### **Log Analysis**
```bash
# View Prometheus logs
docker-compose logs prometheus

# View Grafana logs
docker-compose logs grafana

# View Alertmanager logs
docker-compose logs alertmanager
```

## 📊 Performance Tuning

### **Prometheus Optimization**
- Adjust scrape intervals based on service needs
- Use recording rules for complex queries
- Optimize storage retention policies
- Monitor Prometheus resource usage

### **Grafana Optimization**
- Limit dashboard refresh rates
- Use appropriate time ranges
- Optimize query performance
- Monitor Grafana resource usage

### **Alert Optimization**
- Group related alerts
- Use appropriate inhibition rules
- Optimize notification timing
- Monitor alert volume

## 🔮 Future Enhancements

### **Planned Features**
- **Distributed Tracing**: Jaeger integration
- **Log Aggregation**: ELK stack integration
- **Advanced Analytics**: ML-based anomaly detection
- **Mobile Monitoring**: Mobile app dashboards

### **Integration Opportunities**
- **CI/CD**: Automated monitoring setup
- **Kubernetes**: Native K8s monitoring
- **Cloud Services**: AWS/GCP monitoring
- **Third-party Tools**: PagerDuty, OpsGenie

## 📄 License

This monitoring infrastructure is proprietary software developed by **Mercuria Technologies** for **NeoBridge, owned by Harmony Q&Q GmbH**.

---

**Built with ❤️ by the NeoBridge Development Team**
