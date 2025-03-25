# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy built JAR file
COPY target/micrometer-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8888

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
