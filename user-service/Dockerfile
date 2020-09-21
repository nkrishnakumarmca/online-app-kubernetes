FROM openjdk:11.0.6-jre-slim
ARG JAR_FILE=target/user-service.jar
ADD ${JAR_FILE} user-service.jar
ENTRYPOINT ["java","-jar","user-service.jar"]