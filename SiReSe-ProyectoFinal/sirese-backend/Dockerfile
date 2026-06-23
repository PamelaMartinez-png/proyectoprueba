FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app
COPY SiReSe-ProyectoFinal/sirese-backend/pom.xml .
COPY SiReSe-ProyectoFinal/sirese-backend/src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-al2023
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT:-8080} app.jar"]
