# Use Maven with OpenJDK 11 to build the app
FROM maven:3.8.4-openjdk-11 AS build

WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the app with Maven, skipping tests
RUN mvn clean package -DskipTests

# Use OpenJDK 11 to run the app
FROM openjdk:11-jdk-slim
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the app
CMD ["java", "-jar", "app.jar"]
