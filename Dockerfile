FROM maven:3.6.3-jdk-11-openj9 AS MAVEN_IMAGE
WORKDIR /app
ADD pom.xml /app
RUN mvn -e -U dependency:go-offline  --fail-never

COPY src /app/src
RUN mvn -e -U verify

FROM openjdk:11-jdk-slim
WORKDIR /app
COPY --from=MAVEN_IMAGE /app/target/*.jar /app/target/app.jar
ENTRYPOINT ["java", "-jar", "target/app.jar"]