#!/bin/bash

# NeoBridge API Gateway - Deployment Script
# This script deploys the Kong API Gateway with all necessary configurations

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
GATEWAY_NAME="neobridge-api-gateway"
KONG_VERSION="3.4"
POSTGRES_VERSION="15-alpine"
NETWORK_NAME="neobridge-network"

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
    local ports=("8000" "8001" "8002" "8443")
    for port in "${ports[@]}"; do
        if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
            warning "Port $port is already in use. Please free up the port and try again."
            exit 1
        fi
    done
    
    success "Prerequisites check passed"
}

# Create network if it doesn't exist
create_network() {
    log "Creating Docker network: $NETWORK_NAME"
    
    if ! docker network ls | grep -q "$NETWORK_NAME"; then
        docker network create "$NETWORK_NAME"
        success "Network $NETWORK_NAME created"
    else
        log "Network $NETWORK_NAME already exists"
    fi
}

# Create SSL certificates
create_ssl_certificates() {
    log "Creating SSL certificates..."
    
    local ssl_dir="./ssl"
    local cert_file="$ssl_dir/certs/kong.crt"
    local key_file="$ssl_dir/private/kong.key"
    
    # Create SSL directories
    mkdir -p "$ssl_dir/certs" "$ssl_dir/private"
    
    # Generate self-signed certificate for development
    if [[ ! -f "$cert_file" ]] || [[ ! -f "$key_file" ]]; then
        log "Generating self-signed SSL certificate..."
        openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
            -keyout "$key_file" \
            -out "$cert_file" \
            -subj "/C=US/ST=State/L=City/O=NeoBridge/OU=IT/CN=localhost" \
            -addext "subjectAltName=DNS:localhost,IP:127.0.0.1,IP:0.0.0.0"
        
        success "SSL certificate generated"
    else
        log "SSL certificates already exist"
    fi
}

# Deploy Kong API Gateway
deploy_kong() {
    log "Deploying Kong API Gateway..."
    
    # Stop existing containers
    log "Stopping existing containers..."
    docker-compose down --remove-orphans
    
    # Start Kong database
    log "Starting Kong database..."
    docker-compose up -d kong-database
    
    # Wait for database to be ready
    log "Waiting for database to be ready..."
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if docker-compose exec -T kong-database pg_isready -U kong -d kong > /dev/null 2>&1; then
            success "Database is ready"
            break
        fi
        
        if [ $attempt -eq $max_attempts ]; then
            error "Database failed to start within expected time"
            exit 1
        fi
        
        log "Waiting for database... (attempt $attempt/$max_attempts)"
        sleep 2
        ((attempt++))
    done
    
    # Run Kong migrations
    log "Running Kong migrations..."
    docker-compose up kong-migration
    
    # Check migration status
    if [ $? -eq 0 ]; then
        success "Kong migrations completed successfully"
    else
        error "Kong migrations failed"
        exit 1
    fi
    
    # Start Kong API Gateway
    log "Starting Kong API Gateway..."
    docker-compose up -d kong
    
    # Wait for Kong to be ready
    log "Waiting for Kong to be ready..."
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s http://localhost:8001/status > /dev/null 2>&1; then
            success "Kong API Gateway is ready"
            break
        fi
        
        if [ $attempt -eq $max_attempts ]; then
            error "Kong failed to start within expected time"
            exit 1
        fi
        
        log "Waiting for Kong... (attempt $attempt/$max_attempts)"
        sleep 2
        ((attempt++))
    done
    
    # Start Kong Admin GUI
    log "Starting Kong Admin GUI..."
    docker-compose up -d kong-admin-gui
    
    success "Kong API Gateway deployment completed"
}

# Configure Kong services and routes
configure_kong() {
    log "Configuring Kong services and routes..."
    
    # Wait for Kong to be fully ready
    sleep 5
    
    # Import Kong configuration
    if [ -f "./config/kong.yml" ]; then
        log "Importing Kong configuration..."
        curl -X POST http://localhost:8001/config \
            -F config=@./config/kong.yml \
            -F format=yaml
        
        if [ $? -eq 0 ]; then
            success "Kong configuration imported successfully"
        else
            warning "Failed to import Kong configuration. Manual configuration may be required."
        fi
    else
        warning "Kong configuration file not found. Manual configuration required."
    fi
}

# Verify deployment
verify_deployment() {
    log "Verifying deployment..."
    
    # Check Kong status
    if curl -s http://localhost:8001/status | grep -q "healthy"; then
        success "Kong API Gateway is healthy"
    else
        error "Kong API Gateway is not healthy"
        exit 1
    fi
    
    # Check Kong Admin GUI
    if curl -s http://localhost:8002 > /dev/null 2>&1; then
        success "Kong Admin GUI is accessible"
    else
        warning "Kong Admin GUI is not accessible"
    fi
    
    # Check proxy endpoint
    if curl -s http://localhost:8000 > /dev/null 2>&1; then
        success "Kong proxy is accessible"
    else
        error "Kong proxy is not accessible"
        exit 1
    fi
    
    success "Deployment verification completed"
}

# Display deployment information
display_info() {
    echo ""
    echo "=========================================="
    echo "  NeoBridge API Gateway - Deployment Info"
    echo "=========================================="
    echo ""
    echo "Gateway URL: http://localhost:8000"
    echo "Admin API: http://localhost:8001"
    echo "Admin GUI: http://localhost:8002"
    echo "SSL Proxy: https://localhost:8443"
    echo ""
    echo "Services:"
    echo "  - Authentication: http://localhost:8000/auth"
    echo "  - Accounts: http://localhost:8000/accounts"
    echo "  - Payments: http://localhost:8000/payments"
    echo "  - Crypto: http://localhost:8000/crypto"
    echo "  - Admin: http://localhost:8000/admin"
    echo ""
    echo "Monitoring:"
    echo "  - Prometheus metrics: http://localhost:8001/metrics"
    echo "  - Health check: http://localhost:8001/status"
    echo ""
    echo "Documentation:"
    echo "  - Kong Admin API: https://docs.konghq.com/gateway/latest/admin-api/"
    echo "  - Kong Plugin Hub: https://docs.konghq.com/hub/"
    echo ""
}

# Main deployment function
main() {
    log "Starting NeoBridge API Gateway deployment..."
    
    check_prerequisites
    create_network
    create_ssl_certificates
    deploy_kong
    configure_kong
    verify_deployment
    display_info
    
    success "NeoBridge API Gateway deployment completed successfully!"
}

# Handle script interruption
trap 'error "Deployment interrupted"; exit 1' INT TERM

# Run main function
main "$@"
