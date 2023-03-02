# Base image
FROM openjdk:17-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY ./target/*.jar /app/ged-service.jar

# Expose the default port
EXPOSE 8081

# Start the Spring Boot application
CMD ["java", "-jar", "ged-service.jar"]
