FROM openjdk:11.0.6-jre-slim
ARG JAR_FILE=target/configuration-service.jar
ADD ${JAR_FILE} configuration-service.jar
ENTRYPOINT ["java","-jar","configuration-service.jar"]