# Use Maven for building and packaging the app
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /build
# Copy and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline  # Download dependencies to speed up builds
# Copy source code and build the application
COPY src ./src
# RUN mvn clean install -DskipTests  # Skip tests for faster build
# RUN mvn clean test # Run tests (Jacoco - target/site/jacoco/index.html)
RUN mvn clean install

# Build final image
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the built jar file
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
# Entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
