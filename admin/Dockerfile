# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /workspace

# Copy the entire multi-module project (parent pom + all modules)
COPY pom.xml .
COPY core/    core/
COPY admin/   admin/
COPY public/  public/

# Build only the admin module (core is compiled as a dependency automatically)
RUN mvn clean package -pl admin -am -DskipTests

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /workspace/admin/target/admin-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
