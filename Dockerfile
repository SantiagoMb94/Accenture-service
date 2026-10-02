# ==============================================================================
# ETAPA 1: BUILD (Compilación del artefacto JAR con Gradle y Temurin JDK 17)
# ==============================================================================
FROM gradle:8.10-jdk17-alpine AS builder

WORKDIR /app

# Optimización de caché de capas de Docker para dependencias
COPY build.gradle settings.gradle /app/
RUN gradle dependencies --no-daemon || true

# Copia del código fuente y recursos
COPY src /app/src

# Compilación y empaquetado del JAR ejecutable de Spring Boot
RUN gradle bootJar --no-daemon -x test

# ==============================================================================
# ETAPA 2: RUNTIME (Entorno de ejecución seguro y ligero con Temurin JRE 17)
# ==============================================================================
FROM eclipse-temurin:17-jre-alpine AS runner

WORKDIR /app

# Crear grupo y usuario sin privilegios (Non-Root User) por seguridad empresarial
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar el JAR generado desde la etapa de construcción
COPY --from=builder /app/build/libs/*.jar app.jar

# Ajustar permisos para el usuario sin privilegios
RUN chown -R appuser:appgroup /app

# Cambiar a usuario non-root
USER appuser

# Exponer el puerto del servicio
EXPOSE 8080

# Parámetros de optimización de JVM para entornos de contenedores
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"
ENV PORT=8080

# Healthcheck nativo consultando Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
