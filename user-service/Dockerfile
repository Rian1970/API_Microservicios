# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "user-service-0.0.1-SNAPSHOT.jar"]
