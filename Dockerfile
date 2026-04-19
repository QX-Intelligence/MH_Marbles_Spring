# -------- Stage 1: Dependencies --------
FROM maven:3.9-eclipse-temurin-17 AS deps
WORKDIR /build

# Copy only pom.xml to cache dependencies
COPY pom.xml .

# Download dependencies
RUN mvn -B -q -e -DskipTests dependency:go-offline

# -------- Stage 2: Build --------
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build

# Reuse dependencies from previous stage
COPY --from=deps /root/.m2 /root/.m2

# Copy source code
COPY src ./src
COPY pom.xml .

# Build application
RUN mvn -B -q -DskipTests clean package

# -------- Stage 3: Runtime --------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy only the built jar
COPY --from=builder /build/target/*.jar app.jar

# JVM options
ENV JAVA_OPTS="-Xms512m -Xmx1g"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
