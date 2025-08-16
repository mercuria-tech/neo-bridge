# ☸️ NeoBridge Platform - Complete Kubernetes Deployment Guide

**Production-ready Kubernetes deployment manifests and configuration for the NeoBridge neobank platform**

---

## 🎯 **Kubernetes Deployment Overview**

The **NeoBridge Platform** is deployed on **Google Kubernetes Engine (GKE)** using production-grade Kubernetes manifests with advanced features including auto-scaling, health monitoring, security policies, and high availability configurations.

### **🌟 Deployment Features**
- **Multi-replica deployments** with auto-scaling
- **Load balancing** with ingress controllers
- **Health monitoring** with readiness/liveness probes
- **Security policies** with RBAC and network policies
- **Resource management** with limits and requests
- **Configuration management** with ConfigMaps and Secrets

---

## 🏗️ **Namespace & RBAC Configuration**

### **📁 Create NeoBridge Namespace**
```yaml
# namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: neobridge
  labels:
    name: neobridge
    environment: production
    team: platform
  annotations:
    description: "NeoBridge Platform Production Environment"
```

### **🔐 Service Account Configuration**
```yaml
# service-account.yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: neobridge-apps
  namespace: neobridge
  labels:
    app: neobridge
    component: service-account
---
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: neobridge-app-role
  namespace: neobridge
rules:
- apiGroups: [""]
  resources: ["configmaps", "secrets"]
  verbs: ["get", "list", "watch"]
- apiGroups: [""]
  resources: ["pods", "services"]
  verbs: ["get", "list", "watch"]
---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: neobridge-app-rolebinding
  namespace: neobridge
subjects:
- kind: ServiceAccount
  name: neobridge-apps
  namespace: neobridge
roleRef:
  kind: Role
  name: neobridge-app-role
  apiGroup: rbac.authorization.k8s.io
```

---

## 🔐 **Authentication Service Deployment**

### **📦 Authentication Service Deployment**
```yaml
# auth-service-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: neobridge-auth-service
  namespace: neobridge
  labels:
    app: neobridge
    component: auth-service
    version: v1.0.0
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  selector:
    matchLabels:
      app: neobridge-auth-service
  template:
    metadata:
      labels:
        app: neobridge-auth-service
        version: v1.0.0
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "8080"
        prometheus.io/path: "/actuator/prometheus"
    spec:
      serviceAccountName: neobridge-apps
      terminationGracePeriodSeconds: 30
      containers:
      - name: auth-service
        image: gcr.io/neobridge-platform/neobridge-auth:latest
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
          name: http
          protocol: TCP
        - containerPort: 9090
          name: metrics
          protocol: TCP
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: SERVER_PORT
          value: "8080"
        - name: MANAGEMENT_SERVER_PORT
          value: "9090"
        - name: DB_HOST
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: host
        - name: DB_PORT
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: port
        - name: DB_NAME
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: database
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: password
        - name: REDIS_HOST
          valueFrom:
            secretKeyRef:
              name: neobridge-redis-secret
              key: host
        - name: REDIS_PORT
          valueFrom:
            secretKeyRef:
              name: neobridge-redis-secret
              key: port
        - name: REDIS_PASSWORD
          valueFrom:
            secretKeyRef:
              name: neobridge-redis-secret
              key: password
        - name: JWT_SECRET_KEY
          valueFrom:
            secretKeyRef:
              name: neobridge-jwt-secret
              key: secret-key
        - name: JWT_PUBLIC_KEY
          valueFrom:
            secretKeyRef:
              name: neobridge-jwt-secret
              key: public-key
        - name: JWT_PRIVATE_KEY
          valueFrom:
            secretKeyRef:
              name: neobridge-jwt-secret
              key: private-key
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
          timeoutSeconds: 10
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 3
        startupProbe:
          httpGet:
            path: /actuator/health/startup
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 30
        volumeMounts:
        - name: config-volume
          mountPath: /app/config
        - name: logs-volume
          mountPath: /app/logs
        securityContext:
          runAsNonRoot: true
          runAsUser: 1000
          runAsGroup: 1000
          allowPrivilegeEscalation: false
          readOnlyRootFilesystem: true
          capabilities:
            drop:
            - ALL
      volumes:
      - name: config-volume
        configMap:
          name: neobridge-auth-config
      - name: logs-volume
        emptyDir: {}
      imagePullSecrets:
      - name: gcr-secret
      nodeSelector:
        dedicated: neobridge
      tolerations:
      - key: "dedicated"
        operator: "Equal"
        value: "neobridge"
        effect: "NoSchedule"
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 100
            podAffinityTerm:
              labelSelector:
                matchExpressions:
                - key: app
                  operator: In
                  values:
                  - neobridge-auth-service
              topologyKey: kubernetes.io/hostname
```

### **🔌 Authentication Service Service**
```yaml
# auth-service-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: neobridge-auth-service
  namespace: neobridge
  labels:
    app: neobridge
    component: auth-service
  annotations:
    prometheus.io/scrape: "true"
    prometheus.io/port: "9090"
spec:
  type: ClusterIP
  ports:
  - port: 80
    targetPort: 8080
    protocol: TCP
    name: http
  - port: 9090
    targetPort: 9090
    protocol: TCP
    name: metrics
  selector:
    app: neobridge-auth-service
  sessionAffinity: ClientIP
  sessionAffinityConfig:
    clientIP:
      timeoutSeconds: 10800
```

---

## 🏦 **Account Service Deployment**

### **📦 Account Service Deployment**
```yaml
# account-service-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: neobridge-account-service
  namespace: neobridge
  labels:
    app: neobridge
    component: account-service
    version: v1.0.0
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  selector:
    matchLabels:
      app: neobridge-account-service
  template:
    metadata:
      labels:
        app: neobridge-account-service
        version: v1.0.0
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "8080"
        prometheus.io/path: "/actuator/prometheus"
    spec:
      serviceAccountName: neobridge-apps
      terminationGracePeriodSeconds: 30
      containers:
      - name: account-service
        image: gcr.io/neobridge-platform/neobridge-account:latest
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
          name: http
          protocol: TCP
        - containerPort: 9090
          name: metrics
          protocol: TCP
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: SERVER_PORT
          value: "8080"
        - name: MANAGEMENT_SERVER_PORT
          value: "9090"
        - name: DB_HOST
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: host
        - name: DB_PORT
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: port
        - name: DB_NAME
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: database
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: password
        - name: SOLARISBANK_API_KEY
          valueFrom:
            secretKeyRef:
              name: neobridge-solarisbank-secret
              key: api-key
        - name: SOLARISBANK_API_URL
          valueFrom:
            configMapKeyRef:
              name: neobridge-config
              key: solarisbank.api.url
        - name: SWAN_API_KEY
          valueFrom:
            secretKeyRef:
              name: neobridge-swan-secret
              key: api-key
        - name: SWAN_API_URL
          valueFrom:
            configMapKeyRef:
              name: neobridge-config
              key: swan.api.url
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
          timeoutSeconds: 10
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 3
        startupProbe:
          httpGet:
            path: /actuator/health/startup
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 30
        volumeMounts:
        - name: config-volume
          mountPath: /app/config
        - name: logs-volume
          mountPath: /app/logs
        securityContext:
          runAsNonRoot: true
          runAsUser: 1000
          runAsGroup: 1000
          allowPrivilegeEscalation: false
          readOnlyRootFilesystem: true
          capabilities:
            drop:
            - ALL
      volumes:
      - name: config-volume
        configMap:
          name: neobridge-account-config
      - name: logs-volume
        emptyDir: {}
      imagePullSecrets:
      - name: gcr-secret
      nodeSelector:
        dedicated: neobridge
      tolerations:
      - key: "dedicated"
        operator: "Equal"
        value: "neobridge"
        effect: "NoSchedule"
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 100
            podAffinityTerm:
              labelSelector:
                matchExpressions:
                - key: app
                  operator: In
                  values:
                  - neobridge-account-service
              topologyKey: kubernetes.io/hostname
```

---

## 💸 **Payment Service Deployment**

### **📦 Payment Service Deployment**
```yaml
# payment-service-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: neobridge-payment-service
  namespace: neobridge
  labels:
    app: neobridge
    component: payment-service
    version: v1.0.0
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  selector:
    matchLabels:
      app: neobridge-payment-service
  template:
    metadata:
      labels:
        app: neobridge-payment-service
        version: v1.0.0
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "8080"
        prometheus.io/path: "/actuator/prometheus"
    spec:
      serviceAccountName: neobridge-apps
      terminationGracePeriodSeconds: 30
      containers:
      - name: payment-service
        image: gcr.io/neobridge-platform/neobridge-payment:latest
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
          name: http
          protocol: TCP
        - containerPort: 9090
          name: metrics
          protocol: TCP
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: SERVER_PORT
          value: "8080"
        - name: MANAGEMENT_SERVER_PORT
          value: "9090"
        - name: DB_HOST
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: host
        - name: DB_PORT
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: port
        - name: DB_NAME
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: database
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: password
        - name: KAFKA_BOOTSTRAP_SERVERS
          valueFrom:
            configMapKeyRef:
              name: neobridge-config
              key: kafka.bootstrap.servers
        - name: KAFKA_SECURITY_PROTOCOL
          valueFrom:
            configMapKeyRef:
              name: neobridge-config
              key: kafka.security.protocol
        - name: KAFKA_SASL_MECHANISM
          valueFrom:
            configMapKeyRef:
              name: neobridge-config
              key: kafka.sasl.mechanism
        - name: KAFKA_SASL_JAAS_CONFIG
          valueFrom:
            secretKeyRef:
              name: neobridge-kafka-secret
              key: sasl.jaas.config
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
          timeoutSeconds: 10
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 3
        startupProbe:
          httpGet:
            path: /actuator/health/startup
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 30
        volumeMounts:
        - name: config-volume
          mountPath: /app/config
        - name: logs-volume
          mountPath: /app/logs
        securityContext:
          runAsNonRoot: true
          runAsUser: 1000
          runAsGroup: 1000
          allowPrivilegeEscalation: false
          readOnlyRootFilesystem: true
          capabilities:
            drop:
            - ALL
      volumes:
      - name: config-volume
        configMap:
          name: neobridge-payment-config
      - name: logs-volume
        emptyDir: {}
      imagePullSecrets:
      - name: gcr-secret
      nodeSelector:
        dedicated: neobridge
      tolerations:
      - key: "dedicated"
        operator: "Equal"
        value: "neobridge"
        effect: "NoSchedule"
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 100
            podAffinityTerm:
              labelSelector:
                matchExpressions:
                - key: app
                  operator: In
                  values:
                  - neobridge-payment-service
              topologyKey: kubernetes.io/hostname
```

---

## 🤖 **AI Risk Service Deployment**

### **📦 AI Risk Service Deployment**
```yaml
# ai-risk-service-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: neobridge-ai-risk-service
  namespace: neobridge
  labels:
    app: neobridge
    component: ai-risk-service
    version: v1.0.0
spec:
  replicas: 2
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  selector:
    matchLabels:
      app: neobridge-ai-risk-service
  template:
    metadata:
      labels:
        app: neobridge-ai-risk-service
        version: v1.0.0
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "8080"
        prometheus.io/path: "/actuator/prometheus"
    spec:
      serviceAccountName: neobridge-apps
      terminationGracePeriodSeconds: 30
      containers:
      - name: ai-risk-service
        image: gcr.io/neobridge-platform/neobridge-ai-risk:latest
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
          name: http
          protocol: TCP
        - containerPort: 9090
          name: metrics
          protocol: TCP
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: SERVER_PORT
          value: "8080"
        - name: MANAGEMENT_SERVER_PORT
          value: "9090"
        - name: DB_HOST
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: host
        - name: DB_PORT
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: port
        - name: DB_NAME
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: database
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: neobridge-db-secret
              key: password
        - name: REDIS_HOST
          valueFrom:
            secretKeyRef:
              name: neobridge-redis-secret
              key: host
        - name: REDIS_PORT
          valueFrom:
            secretKeyRef:
              name: neobridge-redis-secret
              key: port
        - name: REDIS_PASSWORD
          valueFrom:
            secretKeyRef:
              name: neobridge-redis-secret
              key: password
        - name: ML_MODEL_PATH
          value: "/app/models"
        - name: TENSORFLOW_LOGGING_LEVEL
          value: "WARNING"
        resources:
          requests:
            memory: "2Gi"
            cpu: "1000m"
          limits:
            memory: "4Gi"
            cpu: "2000m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 120
          periodSeconds: 30
          timeoutSeconds: 10
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 3
        startupProbe:
          httpGet:
            path: /actuator/health/startup
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 30
        volumeMounts:
        - name: config-volume
          mountPath: /app/config
        - name: models-volume
          mountPath: /app/models
        - name: logs-volume
          mountPath: /app/logs
        securityContext:
          runAsNonRoot: true
          runAsUser: 1000
          runAsGroup: 1000
          allowPrivilegeEscalation: false
          readOnlyRootFilesystem: false
          capabilities:
            drop:
            - ALL
      volumes:
      - name: config-volume
        configMap:
          name: neobridge-ai-risk-config
      - name: models-volume
        persistentVolumeClaim:
          claimName: neobridge-models-pvc
      - name: logs-volume
        emptyDir: {}
      imagePullSecrets:
      - name: gcr-secret
      nodeSelector:
        dedicated: neobridge
      tolerations:
      - key: "dedicated"
        operator: "Equal"
        value: "neobridge"
        effect: "NoSchedule"
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 100
            podAffinityTerm:
              labelSelector:
                matchExpressions:
                - key: app
                  operator: In
                  values:
                  - neobridge-ai-risk-service
              topologyKey: kubernetes.io/hostname
```

---

## 🌐 **Ingress Configuration**

### **🔌 Main Ingress Controller**
```yaml
# ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: neobridge-ingress
  namespace: neobridge
  labels:
    app: neobridge
    component: ingress
  annotations:
    kubernetes.io/ingress.class: "gce"
    kubernetes.io/ingress.global-static-ip-name: "neobridge-ip"
    kubernetes.io/ingress.allow-http: "false"
    kubernetes.io/ingress.force-ssl-redirect: "true"
    cloud.google.com/load-balancer-type: "External"
    cloud.google.com/backend-config: '{"default": "neobridge-backend-config"}'
    cloud.google.com/ssl-certificates: "neobridge-ssl-cert"
    cloud.google.com/managed-certificates: "neobridge-managed-cert"
spec:
  tls:
  - secretName: neobridge-tls-secret
    hosts:
    - api.neobridge.com
    - app.neobridge.com
    - admin.neobridge.com
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
      - path: /api/v1/risk
        pathType: Prefix
        backend:
          service:
            name: neobridge-ai-risk-service
            port:
              number: 80
      - path: /api/v1/crypto
        pathType: Prefix
        backend:
          service:
            name: neobridge-crypto-service
            port:
              number: 80
      - path: /api/v1/investments
        pathType: Prefix
        backend:
          service:
            name: neobridge-investment-service
            port:
              number: 80
  - host: app.neobridge.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: neobridge-mobile-app
            port:
              number: 80
  - host: admin.neobridge.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: neobridge-admin-dashboard
            port:
              number: 80
```

### **⚙️ Backend Configuration**
```yaml
# backend-config.yaml
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
  iap:
    enabled: false
  securityPolicy:
    name: neobridge-security-policy
```

---

## 📊 **Horizontal Pod Autoscaler**

### **📈 Authentication Service HPA**
```yaml
# auth-service-hpa.yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: neobridge-auth-service-hpa
  namespace: neobridge
  labels:
    app: neobridge
    component: auth-service
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: neobridge-auth-service
  minReplicas: 3
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
  - type: Object
    object:
      metric:
        name: requests-per-second
      describedObject:
        apiVersion: v1
        kind: Service
        name: neobridge-auth-service
      target:
        type: AverageValue
        averageValue: 1000
  behavior:
    scaleUp:
      stabilizationWindowSeconds: 60
      policies:
      - type: Percent
        value: 100
        periodSeconds: 15
      - type: Pods
        value: 2
        periodSeconds: 15
      selectPolicy: Max
    scaleDown:
      stabilizationWindowSeconds: 300
      policies:
      - type: Percent
        value: 10
        periodSeconds: 60
      - type: Pods
        value: 1
        periodSeconds: 60
      selectPolicy: Min
```

---

## 🔒 **Network Policies**

### **🛡️ Service Isolation Policy**
```yaml
# network-policy.yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: neobridge-service-isolation
  namespace: neobridge
  labels:
    app: neobridge
    component: network-policy
spec:
  podSelector:
    matchLabels:
      app: neobridge
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
    - protocol: TCP
      port: 9090
  - from:
    - namespaceSelector:
        matchLabels:
          name: monitoring
    ports:
    - protocol: TCP
      port: 9090
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
    - protocol: UDP
      port: 53
  - to:
    - namespaceSelector:
        matchLabels:
          name: neobridge
    ports:
    - protocol: TCP
      port: 8080
```

---

## 📋 **ConfigMaps & Secrets**

### **⚙️ Main Configuration**
```yaml
# config.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: neobridge-config
  namespace: neobridge
  labels:
    app: neobridge
    component: config
data:
  # Database configuration
  db.host: "10.0.0.3"
  db.port: "5432"
  db.pool.initial-size: "5"
  db.pool.max-size: "20"
  db.pool.min-idle: "5"
  
  # Redis configuration
  redis.host: "10.0.0.4"
  redis.port: "6379"
  redis.timeout: "2000"
  redis.database: "0"
  
  # Kafka configuration
  kafka.bootstrap.servers: "10.0.0.5:9092"
  kafka.security.protocol: "SASL_SSL"
  kafka.sasl.mechanism: "PLAIN"
  kafka.acks: "all"
  kafka.retries: "3"
  
  # External service endpoints
  solarisbank.api.url: "https://api.solarisbank.com"
  swan.api.url: "https://api.swan.io"
  jumio.api.url: "https://api.jumio.com"
  onfido.api.url: "https://api.onfido.com"
  
  # Platform configuration
  platform.environment: "production"
  platform.region: "europe-west1"
  platform.version: "1.0.0"
  platform.timezone: "Europe/Berlin"
  
  # Security configuration
  security.jwt.expiration: "900"
  security.jwt.refresh-expiration: "604800"
  security.mfa.enabled: "true"
  security.rate-limit.enabled: "true"
  
  # Monitoring configuration
  monitoring.prometheus.enabled: "true"
  monitoring.logging.level: "INFO"
  monitoring.metrics.enabled: "true"
```

---

## 🚀 **Deployment Commands**

### **📋 Deploy All Services**
```bash
# Create namespace and RBAC
kubectl apply -f namespace.yaml
kubectl apply -f service-account.yaml

# Deploy services
kubectl apply -f auth-service-deployment.yaml
kubectl apply -f auth-service-service.yaml
kubectl apply -f account-service-deployment.yaml
kubectl apply -f payment-service-deployment.yaml
kubectl apply -f ai-risk-service-deployment.yaml

# Deploy ingress and configuration
kubectl apply -f config.yaml
kubectl apply -f backend-config.yaml
kubectl apply -f ingress.yaml

# Deploy autoscaling
kubectl apply -f auth-service-hpa.yaml

# Deploy network policies
kubectl apply -f network-policy.yaml

# Verify deployment
kubectl get all -n neobridge
kubectl get ingress -n neobridge
kubectl get hpa -n neobridge
```

### **🔍 Verify Deployment**
```bash
# Check pod status
kubectl get pods -n neobridge

# Check service endpoints
kubectl get endpoints -n neobridge

# Check ingress status
kubectl describe ingress neobridge-ingress -n neobridge

# Check service logs
kubectl logs -f deployment/neobridge-auth-service -n neobridge

# Check resource usage
kubectl top pods -n neobridge
```

---

## 🏁 **Conclusion**

The **NeoBridge Platform Kubernetes deployment** provides production-ready configurations with comprehensive security, monitoring, and scalability features. Built for enterprise-grade operations, it ensures high availability and optimal performance.

**Key deployment strengths:**
- **Production-ready configurations** with security best practices
- **Auto-scaling capabilities** for optimal resource utilization
- **Comprehensive monitoring** with health checks and metrics
- **Security policies** with RBAC and network isolation
- **High availability** with multi-replica deployments

**Ready for production deployment! 🚀**

---

<div align="center">

**NeoBridge Kubernetes Deployment**  
**Built by Mercuria Technologies for Harmony Q&Q GmbH**

</div>
