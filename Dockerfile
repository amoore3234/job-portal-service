FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY mvnw .

COPY .mvn .mvn

COPY pom.xml .

RUN chmod +x mvnw

# Download dependencies separately to leverage Docker's layer caching
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src src

# Build the Spring Boot application JAR file (this typically creates 'target/your-app.jar')
RUN ./mvnw clean install -DskipTests
# If using Gradle: RUN gradle build -x test

# Use a minimal JRE base image for running the application (smaller size, better security)
FROM eclipse-temurin:21-jre-jammy

# Set up non-root user
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser
USER appuser

# Expose the default Spring Boot port
EXPOSE 8081 5433

# Copy the JAR file from the 'build' stage into the final image's root directory
COPY target/job-portal-service-0.0.1-SNAPSHOT.jar ./JobPortalServiceApplication.jar

# Define the command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "./JobPortalServiceApplication.jar"]