package com.neobridge.analytics.entity;

import com.neobridge.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AnalyticsDashboard entity representing a customizable analytics dashboard in the NeoBridge platform.
 * Supports multiple chart types, real-time data, and personalized configurations.
 */
@Entity
@Table(name = "analytics_dashboards", indexes = {
    @Index(name = "idx_analytics_dashboards_dashboard_id", columnList = "dashboard_id"),
    @Index(name = "idx_analytics_dashboards_owner_id", columnList = "owner_id"),
    @Index(name = "idx_analytics_dashboards_dashboard_type", columnList = "dashboard_type"),
    @Index(name = "idx_analytics_dashboards_status", columnList = "status"),
    @Index(name = "idx_analytics_dashboards_created_at", columnList = "created_at")
})
public class AnalyticsDashboard extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "dashboard_id", unique = true, nullable = false)
    private String dashboardId;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @NotBlank
    @Size(max = 200)
    @Column(name = "dashboard_name", nullable = false)
    private String dashboardName;

    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "dashboard_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DashboardType dashboardType;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DashboardStatus status = DashboardStatus.ACTIVE;

    @NotNull
    @Column(name = "dashboard_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private DashboardCategory dashboardCategory;

    @Size(max = 50)
    @Column(name = "subcategory")
    private String subcategory;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "is_shared")
    private Boolean isShared = false;

    @Column(name = "is_template")
    private Boolean isTemplate = false;

    @Column(name = "is_favorite")
    private Boolean isFavorite = false;

    @Column(name = "is_pinned")
    private Boolean isPinned = false;

    // Layout & Design
    @Column(name = "layout_type")
    @Enumerated(EnumType.STRING)
    private LayoutType layoutType = LayoutType.GRID;

    @Column(name = "grid_columns")
    private Integer gridColumns = 12;

    @Column(name = "grid_rows")
    private Integer gridRows = 8;

    @Column(name = "theme")
    @Enumerated(EnumType.STRING)
    private DashboardTheme theme = DashboardTheme.LIGHT;

    @Column(name = "color_scheme")
    private String colorScheme = "default";

    @Column(name = "font_family")
    private String fontFamily = "Inter";

    @Column(name = "font_size")
    private String fontSize = "14px";

    // Data Sources & Refresh
    @Column(name = "data_sources", columnDefinition = "TEXT")
    private String dataSources;

    @Column(name = "refresh_interval")
    private Integer refreshInterval = 300; // 5 minutes

    @Column(name = "auto_refresh")
    private Boolean autoRefresh = true;

    @Column(name = "real_time_enabled")
    private Boolean realTimeEnabled = false;

    @Column(name = "last_data_refresh")
    private LocalDateTime lastDataRefresh;

    @Column(name = "next_data_refresh")
    private LocalDateTime nextDataRefresh;

    // Performance & Caching
    @Column(name = "cache_enabled")
    private Boolean cacheEnabled = true;

    @Column(name = "cache_ttl")
    private Integer cacheTtl = 300; // 5 minutes

    @Column(name = "query_timeout")
    private Integer queryTimeout = 30; // 30 seconds

    @Column(name = "max_data_points")
    private Integer maxDataPoints = 10000;

    @Column(name = "data_aggregation")
    @Enumerated(EnumType.STRING)
    private DataAggregation dataAggregation = DataAggregation.AUTO;

    // Access Control & Permissions
    @Column(name = "access_level")
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel = AccessLevel.PRIVATE;

    @Column(name = "view_permissions")
    private String viewPermissions;

    @Column(name = "edit_permissions")
    private String editPermissions;

    @Column(name = "share_permissions")
    private String sharePermissions;

    @Column(name = "export_permissions")
    private String exportPermissions;

    // White-Label & Customization
    @Column(name = "is_white_label")
    private Boolean isWhiteLabel = false;

    @Column(name = "white_label_partner")
    private String whiteLabelPartner;

    @Column(name = "custom_css")
    private String customCss;

    @Column(name = "custom_js")
    private String customJs;

    @Column(name = "custom_logo")
    private String customLogo;

    @Column(name = "custom_favicon")
    private String customFavicon;

    // Multi-Tenant Support
    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "tenant_tier")
    @Enumerated(EnumType.STRING)
    private TenantTier tenantTier = TenantTier.STANDARD;

    // Analytics & Metrics
    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "interaction_count")
    private Long interactionCount = 0L;

    @Column(name = "export_count")
    private Long exportCount = 0L;

    @Column(name = "share_count")
    private Long shareCount = 0L;

    @Column(name = "last_viewed")
    private LocalDateTime lastViewed;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    // Scheduling & Automation
    @Column(name = "schedule_enabled")
    private Boolean scheduleEnabled = false;

    @Column(name = "schedule_cron")
    private String scheduleCron;

    @Column(name = "schedule_timezone")
    private String scheduleTimezone = "UTC";

    @Column(name = "last_scheduled_run")
    private LocalDateTime lastScheduledRun;

    @Column(name = "next_scheduled_run")
    private LocalDateTime nextScheduledRun;

    // Export & Sharing
    @Column(name = "export_formats")
    private String exportFormats = "PDF,EXCEL,CSV,JSON";

    @Column(name = "export_scheduling")
    private Boolean exportScheduling = false;

    @Column(name = "email_notifications")
    private Boolean emailNotifications = false;

    @Column(name = "notification_recipients")
    private String notificationRecipients;

    // Relationships
    @OneToMany(mappedBy = "analyticsDashboard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DashboardWidget> widgets = new ArrayList<>();

    @OneToMany(mappedBy = "analyticsDashboard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DashboardFilter> filters = new ArrayList<>();

    @OneToMany(mappedBy = "analyticsDashboard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DashboardExport> exports = new ArrayList<>();

    // Enums
    public enum DashboardType {
        EXECUTIVE("Executive Dashboard"),
        OPERATIONAL("Operational Dashboard"),
        ANALYTICAL("Analytical Dashboard"),
        STRATEGIC("Strategic Dashboard"),
        TACTICAL("Tactical Dashboard"),
        FINANCIAL("Financial Dashboard"),
        RISK("Risk Management Dashboard"),
        COMPLIANCE("Compliance Dashboard"),
        CUSTOMER("Customer Analytics Dashboard"),
        PRODUCT("Product Analytics Dashboard"),
        MARKETING("Marketing Analytics Dashboard"),
        SALES("Sales Analytics Dashboard"),
        HR("Human Resources Dashboard"),
        IT("IT Operations Dashboard"),
        SECURITY("Security Dashboard"),
        PERFORMANCE("Performance Dashboard"),
        QUALITY("Quality Assurance Dashboard"),
        INVENTORY("Inventory Management Dashboard"),
        SUPPLY_CHAIN("Supply Chain Dashboard"),
        LOGISTICS("Logistics Dashboard"),
        CUSTOM("Custom Dashboard");

        private final String displayName;

        DashboardType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DashboardStatus {
        ACTIVE("Active"),
        INACTIVE("Inactive"),
        DRAFT("Draft"),
        PUBLISHED("Published"),
        ARCHIVED("Archived"),
        UNDER_REVIEW("Under Review"),
        APPROVED("Approved"),
        REJECTED("Rejected"),
        MAINTENANCE("Maintenance"),
        DEPRECATED("Deprecated");

        private final String displayName;

        DashboardStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DashboardCategory {
        BUSINESS_INTELLIGENCE("Business Intelligence"),
        FINANCIAL_ANALYTICS("Financial Analytics"),
        OPERATIONAL_ANALYTICS("Operational Analytics"),
        CUSTOMER_ANALYTICS("Customer Analytics"),
        PRODUCT_ANALYTICS("Product Analytics"),
        MARKETING_ANALYTICS("Marketing Analytics"),
        SALES_ANALYTICS("Sales Analytics"),
        RISK_ANALYTICS("Risk Analytics"),
        COMPLIANCE_ANALYTICS("Compliance Analytics"),
        SECURITY_ANALYTICS("Security Analytics"),
        PERFORMANCE_ANALYTICS("Performance Analytics"),
        QUALITY_ANALYTICS("Quality Analytics"),
        SUPPLY_CHAIN_ANALYTICS("Supply Chain Analytics"),
        LOGISTICS_ANALYTICS("Logistics Analytics"),
        HR_ANALYTICS("HR Analytics"),
        IT_ANALYTICS("IT Analytics"),
        RESEARCH_ANALYTICS("Research Analytics"),
        ACADEMIC_ANALYTICS("Academic Analytics"),
        SCIENTIFIC_ANALYTICS("Scientific Analytics"),
        SOCIAL_ANALYTICS("Social Analytics"),
        ENVIRONMENTAL_ANALYTICS("Environmental Analytics"),
        HEALTHCARE_ANALYTICS("Healthcare Analytics"),
        EDUCATIONAL_ANALYTICS("Educational Analytics"),
        GOVERNMENT_ANALYTICS("Government Analytics"),
        NON_PROFIT_ANALYTICS("Non-Profit Analytics");

        private final String displayName;

        DashboardCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum LayoutType {
        GRID("Grid Layout"),
        FLEXIBLE("Flexible Layout"),
        FIXED("Fixed Layout"),
        RESPONSIVE("Responsive Layout"),
        MOBILE_FIRST("Mobile First Layout"),
        DESKTOP_FIRST("Desktop First Layout"),
        TABLET_OPTIMIZED("Tablet Optimized Layout"),
        CUSTOM("Custom Layout");

        private final String displayName;

        LayoutType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DashboardTheme {
        LIGHT("Light Theme"),
        DARK("Dark Theme"),
        AUTO("Auto Theme"),
        HIGH_CONTRAST("High Contrast Theme"),
        COLORBLIND_FRIENDLY("Colorblind Friendly Theme"),
        MINIMAL("Minimal Theme"),
        CORPORATE("Corporate Theme"),
        CREATIVE("Creative Theme"),
        MODERN("Modern Theme"),
        CLASSIC("Classic Theme");

        private final String displayName;

        DashboardTheme(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DataAggregation {
        AUTO("Automatic Aggregation"),
        MANUAL("Manual Aggregation"),
        REAL_TIME("Real-Time Data"),
        BATCH("Batch Processing"),
        STREAMING("Streaming Data"),
        HYBRID("Hybrid Approach");

        private final String displayName;

        DataAggregation(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum AccessLevel {
        PRIVATE("Private"),
        PUBLIC("Public"),
        SHARED("Shared"),
        RESTRICTED("Restricted"),
        CONFIDENTIAL("Confidential"),
        INTERNAL("Internal"),
        EXTERNAL("External");

        private final String displayName;

        AccessLevel(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum TenantTier {
        BASIC("Basic"),
        STANDARD("Standard"),
        PREMIUM("Premium"),
        ENTERPRISE("Enterprise"),
        INSTITUTIONAL("Institutional"),
        GOVERNMENT("Government"),
        CUSTOM("Custom");

        private final String displayName;

        TenantTier(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public AnalyticsDashboard() {}

    public AnalyticsDashboard(String dashboardId, UUID ownerId, String dashboardName, 
                             DashboardType dashboardType, DashboardCategory dashboardCategory) {
        this.dashboardId = dashboardId;
        this.ownerId = ownerId;
        this.dashboardName = dashboardName;
        this.dashboardType = dashboardType;
        this.dashboardCategory = dashboardCategory;
        this.lastModified = LocalDateTime.now();
    }

    // Helper methods
    public boolean isPubliclyAccessible() {
        return isPublic && status == DashboardStatus.PUBLISHED;
    }

    public boolean isSharedWithOthers() {
        return isShared && !viewPermissions.isEmpty();
    }

    public boolean isTemplateAvailable() {
        return isTemplate && status == DashboardStatus.PUBLISHED;
    }

    public boolean isWhiteLabelEnabled() {
        return isWhiteLabel && whiteLabelPartner != null;
    }

    public boolean isMultiTenant() {
        return tenantId != null && tenantName != null;
    }

    public boolean isScheduled() {
        return scheduleEnabled && scheduleCron != null;
    }

    public boolean isRealTime() {
        return realTimeEnabled && autoRefresh;
    }

    public void incrementViewCount() {
        this.viewCount++;
        this.lastViewed = LocalDateTime.now();
    }

    public void incrementInteractionCount() {
        this.interactionCount++;
        this.lastModified = LocalDateTime.now();
    }

    public void incrementExportCount() {
        this.exportCount++;
        this.lastModified = LocalDateTime.now();
    }

    public void incrementShareCount() {
        this.shareCount++;
        this.lastModified = LocalDateTime.now();
    }

    public void updateLastModified() {
        this.lastModified = LocalDateTime.now();
    }

    public void updateDataRefresh() {
        this.lastDataRefresh = LocalDateTime.now();
        if (autoRefresh && refreshInterval > 0) {
            this.nextDataRefresh = this.lastDataRefresh.plusSeconds(refreshInterval);
        }
    }

    // Getters and Setters
    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DashboardType getDashboardType() {
        return dashboardType;
    }

    public void setDashboardType(DashboardType dashboardType) {
        this.dashboardType = dashboardType;
    }

    public DashboardStatus getStatus() {
        return status;
    }

    public void setStatus(DashboardStatus status) {
        this.status = status;
    }

    public DashboardCategory getDashboardCategory() {
        return dashboardCategory;
    }

    public void setDashboardCategory(DashboardCategory dashboardCategory) {
        this.dashboardCategory = dashboardCategory;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(Boolean isShared) {
        this.isShared = isShared;
    }

    public Boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
    }

    public Integer getGridColumns() {
        return gridColumns;
    }

    public void setGridColumns(Integer gridColumns) {
        this.gridColumns = gridColumns;
    }

    public Integer getGridRows() {
        return gridRows;
    }

    public void setGridRows(Integer gridRows) {
        this.gridRows = gridRows;
    }

    public DashboardTheme getTheme() {
        return theme;
    }

    public void setTheme(DashboardTheme theme) {
        this.theme = theme;
    }

    public String getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(String colorScheme) {
        this.colorScheme = colorScheme;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getDataSources() {
        return dataSources;
    }

    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }

    public Integer getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(Integer refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public Boolean getAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(Boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public Boolean getRealTimeEnabled() {
        return realTimeEnabled;
    }

    public void setRealTimeEnabled(Boolean realTimeEnabled) {
        this.realTimeEnabled = realTimeEnabled;
    }

    public LocalDateTime getLastDataRefresh() {
        return lastDataRefresh;
    }

    public void setLastDataRefresh(LocalDateTime lastDataRefresh) {
        this.lastDataRefresh = lastDataRefresh;
    }

    public LocalDateTime getNextDataRefresh() {
        return nextDataRefresh;
    }

    public void setNextDataRefresh(LocalDateTime nextDataRefresh) {
        this.nextDataRefresh = nextDataRefresh;
    }

    public Boolean getCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(Boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public Integer getCacheTtl() {
        return cacheTtl;
    }

    public void setCacheTtl(Integer cacheTtl) {
        this.cacheTtl = cacheTtl;
    }

    public Integer getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(Integer queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public Integer getMaxDataPoints() {
        return maxDataPoints;
    }

    public void setMaxDataPoints(Integer maxDataPoints) {
        this.maxDataPoints = maxDataPoints;
    }

    public DataAggregation getDataAggregation() {
        return dataAggregation;
    }

    public void setDataAggregation(DataAggregation dataAggregation) {
        this.dataAggregation = dataAggregation;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getViewPermissions() {
        return viewPermissions;
    }

    public void setViewPermissions(String viewPermissions) {
        this.viewPermissions = viewPermissions;
    }

    public String getEditPermissions() {
        return editPermissions;
    }

    public void setEditPermissions(String editPermissions) {
        this.editPermissions = editPermissions;
    }

    public String getSharePermissions() {
        return sharePermissions;
    }

    public void setSharePermissions(String sharePermissions) {
        this.sharePermissions = sharePermissions;
    }

    public String getExportPermissions() {
        return exportPermissions;
    }

    public void setExportPermissions(String exportPermissions) {
        this.exportPermissions = exportPermissions;
    }

    public Boolean getIsWhiteLabel() {
        return isWhiteLabel;
    }

    public void setIsWhiteLabel(Boolean isWhiteLabel) {
        this.isWhiteLabel = isWhiteLabel;
    }

    public String getWhiteLabelPartner() {
        return whiteLabelPartner;
    }

    public void setWhiteLabelPartner(String whiteLabelPartner) {
        this.whiteLabelPartner = whiteLabelPartner;
    }

    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public String getCustomJs() {
        return customJs;
    }

    public void setCustomJs(String customJs) {
        this.customJs = customJs;
    }

    public String getCustomLogo() {
        return customLogo;
    }

    public void setCustomLogo(String customLogo) {
        this.customLogo = customLogo;
    }

    public String getCustomFavicon() {
        return customFavicon;
    }

    public void setCustomFavicon(String customFavicon) {
        this.customFavicon = customFavicon;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public TenantTier getTenantTier() {
        return tenantTier;
    }

    public void setTenantTier(TenantTier tenantTier) {
        this.tenantTier = tenantTier;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getInteractionCount() {
        return interactionCount;
    }

    public void setInteractionCount(Long interactionCount) {
        this.interactionCount = interactionCount;
    }

    public Long getExportCount() {
        return exportCount;
    }

    public void setExportCount(Long exportCount) {
        this.exportCount = exportCount;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public LocalDateTime getLastViewed() {
        return lastViewed;
    }

    public void setLastViewed(LocalDateTime lastViewed) {
        this.lastViewed = lastViewed;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getScheduleEnabled() {
        return scheduleEnabled;
    }

    public void setScheduleEnabled(Boolean scheduleEnabled) {
        this.scheduleEnabled = scheduleEnabled;
    }

    public String getScheduleCron() {
        return scheduleCron;
    }

    public void setScheduleCron(String scheduleCron) {
        this.scheduleCron = scheduleCron;
    }

    public String getScheduleTimezone() {
        return scheduleTimezone;
    }

    public void setScheduleTimezone(String scheduleTimezone) {
        this.scheduleTimezone = scheduleTimezone;
    }

    public LocalDateTime getLastScheduledRun() {
        return lastScheduledRun;
    }

    public void setLastScheduledRun(LocalDateTime lastScheduledRun) {
        this.lastScheduledRun = lastScheduledRun;
    }

    public LocalDateTime getNextScheduledRun() {
        return nextScheduledRun;
    }

    public void setNextScheduledRun(LocalDateTime nextScheduledRun) {
        this.nextScheduledRun = nextScheduledRun;
    }

    public String getExportFormats() {
        return exportFormats;
    }

    public void setExportFormats(String exportFormats) {
        this.exportFormats = exportFormats;
    }

    public Boolean getExportScheduling() {
        return exportScheduling;
    }

    public void setExportScheduling(Boolean exportScheduling) {
        this.exportScheduling = exportScheduling;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public String getNotificationRecipients() {
        return notificationRecipients;
    }

    public void setNotificationRecipients(String notificationRecipients) {
        this.notificationRecipients = notificationRecipients;
    }

    public List<DashboardWidget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<DashboardWidget> widgets) {
        this.widgets = widgets;
    }

    public List<DashboardFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<DashboardFilter> filters) {
        this.filters = filters;
    }

    public List<DashboardExport> getExports() {
        return exports;
    }

    public void setExports(List<DashboardExport> exports) {
        this.exports = exports;
    }

    @Override
    public String toString() {
        return "AnalyticsDashboard{" +
                "id=" + getId() +
                ", dashboardId='" + dashboardId + '\'' +
                ", dashboardName='" + dashboardName + '\'' +
                ", dashboardType=" + dashboardType +
                ", dashboardCategory=" + dashboardCategory +
                ", status=" + status +
                '}';
    }
}
