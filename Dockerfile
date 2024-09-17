# BUILD
FROM maven:3.9-eclipse-temurin-21-alpine AS build

COPY src /app/src
COPY pom.xml /app

RUN mkdir "/astro"
RUN mkdir "/astro/public"
RUN mkdir "/astro/private"

ENV PUBLIC_PATH=/astro/public
ENV PRIVATE_PATH=/astro/private

RUN mvn -f /app/pom.xml clean package -Dspring.profiles.active=docker

# PACKAGE
FROM eclipse-temurin:21-alpine

COPY --from=build /app/target/*.jar app.jar

# Expose ports
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]