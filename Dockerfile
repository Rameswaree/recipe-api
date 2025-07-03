# Builder
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# Application
FROM eclipse-temurin:21

WORKDIR /app
COPY --from=build /app/target/recipe-api-*.jar recipe-api.jar

EXPOSE 9999

ENTRYPOINT ["java","-jar","recipe-api.jar"]