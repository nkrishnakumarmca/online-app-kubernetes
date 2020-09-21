FROM openjdk:11.0.6-jre-slim
EXPOSE 9005
ARG JAR_FILE=target/order-service.jar
ADD ${JAR_FILE} order-service.jar
ENTRYPOINT ["java","-jar","/order-service.jar"]
