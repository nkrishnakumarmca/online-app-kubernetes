FROM openjdk:11.0.6-jre-slim
ARG JAR_FILE=target/service-registry.jar
ADD ${JAR_FILE} service-registry.jar
ENTRYPOINT ["java","-jar","service-registry.jar"]