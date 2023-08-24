FROM openjdk:latest
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} weather-service-1.0.0.jar
ENTRYPOINT ["java","-jar","/weather-service-1.0.0.jar"]