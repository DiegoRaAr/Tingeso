# Etapa 1: Compilar la aplicación
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la app
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Puerto interno donde corre Spring Boot
EXPOSE 8090

# Ejecuta la app
ENTRYPOINT ["java", "-jar", "app.jar"]
