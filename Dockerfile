# BUILD STAGE
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# PACKAGE STAGE
FROM openjdk:17
COPY --from=build /app/target/assessment-0.0.1-SNAPSHOT.jar assessment.jar
EXPOSE 9003
CMD ["java","-jar","assessment.jar"]