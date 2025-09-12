# First stage: Build the JAR file
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies to cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the project files into the container
COPY src ./src

# Run Maven to build the project and create the JAR file
#RUN mvn clean package -DskipTests
RUN mvn -Pprod clean install -DskipTests

# Second stage: Create the final image with the JAR file
FROM openjdk:21-jdk
WORKDIR /app

# Copy the JAR file from the first stage
#COPY --from=build /app/target/tracker-0.0.1-SNAPSHOT.war /app/tracker-0.0.1-SNAPSHOT.war
COPY --from=build /app/target/tracker-0.0.1-SNAPSHOT.war app.war

# Expose the application port
EXPOSE 8081
EXPOSE 7500

# Define a volume for the source code
VOLUME ["/app/src"]

# Run the JAR file  
#ENTRYPOINT ["java", "-jar", "/app/workconnectv2-backend-0.0.1-SNAPSHOT.jar"]
ENTRYPOINT ["java", "-jar", "app.war"]
