FROM openjdk:11.0.6-jre-slim
ARG JAR_FILE=target/api-gateway-service.jar
ADD ${JAR_FILE} api-gateway-service.jar
ENTRYPOINT ["java","-jar","api-gateway-service.jar"]