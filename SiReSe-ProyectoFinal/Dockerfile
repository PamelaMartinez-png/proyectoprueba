FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY sirese-backend/pom.xml .
COPY sirese-backend/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/sirese-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]