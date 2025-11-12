# Use official lightweight Java runtime
FROM eclipse-temurin:17-jdk-alpine

# Set working directory in container
WORKDIR /app

# Copy Spring Boot JAR into container
COPY target/fast-bank-0.0.1-SNAPSHOT.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]


# To build the Docker image, run:
# docker build -t fast-bank:latest .

# To run the Docker container, use:
# docker run -p 8080:8080 fast-bank:latest

