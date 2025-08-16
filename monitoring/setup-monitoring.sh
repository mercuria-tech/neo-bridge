#!/bin/bash

# NeoBridge Platform - Monitoring Setup Script
# Sets up Prometheus, Grafana, and Alertmanager

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
PROMETHEUS_PORT=9090
GRAFANA_PORT=3000
ALERTMANAGER_PORT=9093

# Logging functions
log() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log "Checking prerequisites..."
    
    # Check if Docker is running
    if ! docker info > /dev/null 2>&1; then
        error "Docker is not running. Please start Docker and try again."
        exit 1
    fi
    
    # Check if Docker Compose is available
    if ! command -v docker-compose > /dev/null 2>&1; then
        error "Docker Compose is not installed. Please install Docker Compose and try again."
        exit 1
    fi
    
    # Check if required ports are available
    local ports=("$PROMETHEUS_PORT" "$GRAFANA_PORT" "$ALERTMANAGER_PORT")
    for port in "${ports[@]}"; do
        if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
            warning "Port $port is already in use. Please free up the port and try again."
            exit 1
        fi
    done
    
    success "Prerequisites check passed"
}

# Create monitoring directories
create_directories() {
    log "Creating monitoring directories..."
    
    mkdir -p monitoring/{prometheus,grafana,alertmanager,logs}
    mkdir -p monitoring/prometheus/rules
    mkdir -p monitoring/grafana/provisioning/{datasources,dashboards}
    
    success "Monitoring directories created"
}

# Setup Prometheus
setup_prometheus() {
    log "Setting up Prometheus..."
    
    # Create Prometheus configuration
    cat > monitoring/prometheus/prometheus.yml << 'EOF'
# NeoBridge Platform - Prometheus Configuration
global:
  scrape_interval: 15s
  evaluation_interval: 15s
  external_labels:
    monitor: 'neobridge-platform'
    environment: 'development'

rule_files:
  - "rules/*.yml"

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']
    scrape_interval: 15s
    scrape_timeout: 5s

  - job_name: 'kong'
    static_configs:
      - targets: ['kong:8001']
    metrics_path: /metrics
    scrape_interval: 10s
    scrape_timeout: 5s

  - job_name: 'neobridge-database'
    static_configs:
      - targets: ['postgres:5432']
    metrics_path: /metrics
    scrape_interval: 30s
    scrape_timeout: 10s

  - job_name: 'redis'
    static_configs:
      - targets: ['redis:6379']
    metrics_path: /metrics
    scrape_interval: 15s
    scrape_timeout: 5s

  - job_name: 'kafka'
    static_configs:
      - targets: ['kafka:9101']
    metrics_path: /metrics
    scrape_interval: 15s
    scrape_timeout: 5s

  - job_name: 'elasticsearch'
    static_configs:
      - targets: ['elasticsearch:9200']
    metrics_path: /_prometheus/metrics
    scrape_interval: 30s
    scrape_timeout: 10s

alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093
EOF

    success "Prometheus configuration created"
}

# Setup Alertmanager
setup_alertmanager() {
    log "Setting up Alertmanager..."
    
    # Create Alertmanager configuration
    cat > monitoring/alertmanager/alertmanager.yml << 'EOF'
global:
  resolve_timeout: 5m
  smtp_smarthost: 'localhost:587'
  smtp_from: 'alertmanager@neobridge.com'

route:
  group_by: ['alertname', 'service', 'severity']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 1h
  receiver: 'web.hook'
  routes:
    - match:
        severity: critical
      receiver: 'critical-alerts'
      group_wait: 0s
      group_interval: 0s
      repeat_interval: 15m
      continue: true
    
    - match:
        severity: warning
      receiver: 'warning-alerts'
      group_wait: 30s
      group_interval: 30s
      repeat_interval: 2h
      continue: true

receivers:
  - name: 'web.hook'
    webhook_configs:
      - url: 'http://localhost:5001/'
        send_resolved: true

  - name: 'critical-alerts'
    webhook_configs:
      - url: 'http://localhost:5001/critical'
        send_resolved: true

  - name: 'warning-alerts'
    webhook_configs:
      - url: 'http://localhost:5001/warning'
        send_resolved: true
EOF

    success "Alertmanager configuration created"
}

# Setup Grafana
setup_grafana() {
    log "Setting up Grafana..."
    
    # Create Grafana datasource configuration
    cat > monitoring/grafana/provisioning/datasources/prometheus.yml << 'EOF'
# Grafana Datasource Configuration
# Prometheus datasource for NeoBridge platform monitoring

datasources:
  prometheus:
    name: Prometheus
    type: prometheus
    url: http://prometheus:9090
    isDefault: true
EOF

    # Create Grafana dashboard configuration
    cat > monitoring/grafana/provisioning/dashboards/dashboard.yml << 'EOF'
# Grafana Dashboard Configuration
# NeoBridge platform dashboard provisioning

apiVersion: 1

providers:
  - name: 'NeoBridge'
    orgId: 1
    folder: ''
    type: file
    disableDeletion: false
    updateIntervalSeconds: 10
    allowUiUpdates: true
    options:
      path: /etc/grafana/provisioning/dashboards
EOF

    success "Grafana configuration created"
}

# Create monitoring rules
create_monitoring_rules() {
    log "Creating monitoring rules..."
    
    # Create basic monitoring rules
    cat > monitoring/prometheus/rules/neobridge-rules.yml << 'EOF'
# NeoBridge Platform - Basic Monitoring Rules
groups:
  - name: neobridge-platform
    rules:
      - alert: PlatformServiceDown
        expr: up == 0
        for: 1m
        labels:
          severity: critical
          service: platform
        annotations:
          summary: "Service {{ $labels.job }} is down"
          description: "Service {{ $labels.job }} has been down for more than 1 minute"

      - alert: HighErrorRate
        expr: rate(http_requests_total{status=~"4..|5.."}[5m]) > 0.1
        for: 2m
        labels:
          severity: warning
          service: platform
        annotations:
          summary: "High error rate detected"
          description: "Error rate is {{ $value }} errors per second for the last 5 minutes"

      - alert: HighLatency
        expr: histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m])) > 1
        for: 2m
        labels:
          severity: warning
          service: platform
        annotations:
          summary: "High latency detected"
          description: "95th percentile latency is {{ $value }} seconds for the last 5 minutes"
EOF

    success "Monitoring rules created"
}

# Create monitoring Docker Compose
create_monitoring_compose() {
    log "Creating monitoring Docker Compose configuration..."
    
    # Create monitoring docker-compose.yml
    cat > monitoring/docker-compose.yml << 'EOF'
version: '3.8'

services:
  # Prometheus
  prometheus:
    image: prom/prometheus:v2.47.0
    container_name: neobridge-prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus:/etc/prometheus
      - prometheus_data:/prometheus
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.console.libraries=/etc/prometheus/console_libraries'
      - '--web.console.templates=/etc/prometheus/consoles'
      - '--storage.tsdb.retention.time=200h'
      - '--web.enable-lifecycle'
    networks:
      - neobridge-network
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:9090/-/healthy"]
      interval: 30s
      timeout: 10s
      retries: 3

  # Alertmanager
  alertmanager:
    image: prom/alertmanager:v0.26.0
    container_name: neobridge-alertmanager
    ports:
      - "9093:9093"
    volumes:
      - ./alertmanager:/etc/alertmanager
      - alertmanager_data:/alertmanager
    command:
      - '--config.file=/etc/alertmanager/alertmanager.yml'
      - '--storage.path=/alertmanager'
      - '--web.console.libraries=/etc/alertmanager/console_libraries'
      - '--web.console.templates=/etc/alertmanager/consoles'
      - '--web.enable-lifecycle'
    networks:
      - neobridge-network
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:9093/-/healthy"]
      interval: 30s
      timeout: 10s
      retries: 3

  # Grafana
  grafana:
    image: grafana/grafana:10.0.0
    container_name: neobridge-grafana
    ports:
      - "3000:3000"
    environment:
      GF_SECURITY_ADMIN_USER: admin
      GF_SECURITY_ADMIN_PASSWORD: admin
      GF_USERS_ALLOW_SIGN_UP: false
    volumes:
      - grafana_data:/var/lib/grafana
      - ./grafana/provisioning:/etc/grafana/provisioning
    networks:
      - neobridge-network
    healthcheck:
      test: ["CMD-SHELL", "curl -f http://localhost:3000/api/health || exit 1"]
      interval: 30s
      timeout: 10s
      retries: 3

volumes:
  prometheus_data:
  alertmanager_data:
  grafana_data:

networks:
  neobridge-network:
    external: true
EOF

    success "Monitoring Docker Compose configuration created"
}

# Start monitoring services
start_monitoring() {
    log "Starting monitoring services..."
    
    cd monitoring
    
    # Start services
    docker-compose up -d
    
    # Wait for services to be ready
    log "Waiting for services to be ready..."
    sleep 30
    
    # Check service health
    if curl -s http://localhost:9090/-/healthy > /dev/null 2>&1; then
        success "Prometheus is healthy"
    else
        warning "Prometheus health check failed"
    fi
    
    if curl -s http://localhost:9093/-/healthy > /dev/null 2>&1; then
        success "Alertmanager is healthy"
    else
        warning "Alertmanager health check failed"
    fi
    
    if curl -s http://localhost:3000/api/health > /dev/null 2>&1; then
        success "Grafana is healthy"
    else
        warning "Grafana health check failed"
    fi
    
    cd ..
}

# Display monitoring information
display_info() {
    echo ""
    echo "=========================================="
    echo "  NeoBridge Platform - Monitoring Setup"
    echo "=========================================="
    echo ""
    echo "Monitoring Services:"
    echo "  - Prometheus: http://localhost:9090"
    echo "  - Alertmanager: http://localhost:9093"
    echo "  - Grafana: http://localhost:3000"
    echo ""
    echo "Grafana Login:"
    echo "  Username: admin"
    echo "  Password: admin"
    echo ""
    echo "Monitoring Features:"
    echo "  - Service health monitoring"
    echo "  - Performance metrics"
    echo "  - Alert management"
    echo "  - Custom dashboards"
    echo ""
    echo "Next Steps:"
    echo "  1. Access Grafana at http://localhost:3000"
    echo "  2. Import NeoBridge dashboard"
    echo "  3. Configure additional alerts"
    echo "  4. Set up notification channels"
    echo ""
}

# Main setup function
main() {
    log "Starting NeoBridge Platform monitoring setup..."
    
    check_prerequisites
    create_directories
    setup_prometheus
    setup_alertmanager
    setup_grafana
    create_monitoring_rules
    create_monitoring_compose
    start_monitoring
    display_info
    
    success "NeoBridge Platform monitoring setup completed successfully!"
}

# Handle script interruption
trap 'error "Setup interrupted"; exit 1' INT TERM

# Run main function
main "$@"
