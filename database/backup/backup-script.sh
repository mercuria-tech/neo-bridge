#!/bin/bash

# NeoBridge Platform Database Backup Script
# This script creates automated backups of the PostgreSQL database

# Configuration
DB_NAME="neobridge_dev"
DB_USER="neobridge_user"
DB_HOST="localhost"
DB_PORT="5432"
BACKUP_DIR="/var/backups/neobridge"
RETENTION_DAYS=30
DATE_FORMAT=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="neobridge_backup_${DATE_FORMAT}.sql"
COMPRESSED_FILE="${BACKUP_FILE}.gz"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Logging function
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}"
}

warning() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

# Check if PostgreSQL is running
check_postgres() {
    if ! pg_isready -h $DB_HOST -p $DB_PORT -U $DB_USER > /dev/null 2>&1; then
        error "PostgreSQL is not running or not accessible"
        exit 1
    fi
    log "PostgreSQL connection verified"
}

# Create backup directory if it doesn't exist
create_backup_dir() {
    if [ ! -d "$BACKUP_DIR" ]; then
        mkdir -p "$BACKUP_DIR"
        log "Created backup directory: $BACKUP_DIR"
    fi
}

# Perform database backup
perform_backup() {
    log "Starting database backup..."
    
    # Create backup with pg_dump
    if pg_dump -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME \
        --verbose --clean --create --no-owner --no-privileges \
        --file="$BACKUP_DIR/$BACKUP_FILE" 2>/dev/null; then
        
        log "Database backup completed successfully: $BACKUP_FILE"
        
        # Compress the backup file
        if gzip "$BACKUP_DIR/$BACKUP_FILE"; then
            log "Backup compressed: $COMPRESSED_FILE"
            BACKUP_FILE="$COMPRESSED_FILE"
        else
            warning "Failed to compress backup file"
        fi
        
        # Get backup file size
        BACKUP_SIZE=$(du -h "$BACKUP_DIR/$BACKUP_FILE" | cut -f1)
        log "Backup size: $BACKUP_SIZE"
        
    else
        error "Database backup failed"
        exit 1
    fi
}

# Clean up old backups
cleanup_old_backups() {
    log "Cleaning up backups older than $RETENTION_DAYS days..."
    
    # Find and remove old backup files
    find "$BACKUP_DIR" -name "neobridge_backup_*.sql.gz" -type f -mtime +$RETENTION_DAYS -delete
    
    # Count remaining backups
    REMAINING_BACKUPS=$(find "$BACKUP_DIR" -name "neobridge_backup_*.sql.gz" -type f | wc -l)
    log "Remaining backups: $REMAINING_BACKUPS"
}

# Verify backup integrity
verify_backup() {
    log "Verifying backup integrity..."
    
    # Extract and test the backup
    TEMP_DIR=$(mktemp -d)
    cd "$TEMP_DIR"
    
    if gunzip -c "$BACKUP_DIR/$BACKUP_FILE" > temp_backup.sql; then
        # Check if the file contains valid SQL
        if head -n 10 temp_backup.sql | grep -q "PostgreSQL database dump"; then
            log "Backup verification successful"
        else
            warning "Backup verification warning - file may be corrupted"
        fi
        
        # Clean up temp files
        rm -f temp_backup.sql
        cd - > /dev/null
        rmdir "$TEMP_DIR"
    else
        error "Backup verification failed"
        exit 1
    fi
}

# Send backup notification (if configured)
send_notification() {
    # This function can be extended to send notifications via email, Slack, etc.
    log "Backup notification sent (if configured)"
}

# Main execution
main() {
    log "=== NeoBridge Database Backup Started ==="
    
    # Check prerequisites
    check_postgres
    create_backup_dir
    
    # Perform backup operations
    perform_backup
    verify_backup
    cleanup_old_backups
    
    # Send notification
    send_notification
    
    log "=== NeoBridge Database Backup Completed Successfully ==="
    log "Backup file: $BACKUP_DIR/$BACKUP_FILE"
}

# Handle script interruption
trap 'error "Backup interrupted"; exit 1' INT TERM

# Run main function
main "$@"
