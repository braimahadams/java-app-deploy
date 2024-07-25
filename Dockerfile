# Use a base image with JDK for building the application
FROM eclipse-temurin:21.0.1_12-jre

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle build files and source code
COPY src ./src

# Copy the built JAR file from the builder stage
COPY build/libs/*.jar /app/app.jar

# Expose the port the application will run on
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
