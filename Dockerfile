# Etapa 1: Build (usando Maven Wrapper y Java 21)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos solo los archivos necesarios para el build
COPY pom.xml .
#COPY mvnw .
#COPY .mvn .mvn
COPY src src

# Damos permiso de ejecución al wrapper
#RUN chmod +x mvnw

# Compilamos el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: Runtime (imagen más liviana)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/target/*.jar app.jar

# Punto de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]
