ARG FW_TAG=latest
FROM freethinking04/javaframework-m2:${FW_TAG} AS framework

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY --from=framework /root/.m2 /root/.m2
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]