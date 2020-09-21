FROM tomcat:jdk11-adoptopenjdk-openj9
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./target/product-service.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh","run"]