#!/bin/bash

mvn clean install -DskipTests -f configuration-service/pom.xml
mvn clean install -DskipTests -f service-registry/pom.xml
mvn clean install -DskipTests -f api-gateway-service/pom.xml
mvn clean install -DskipTests -f order-service/pom.xml
mvn clean install -DskipTests -f product-service/pom.xml
mvn clean install -DskipTests -f user-service/pom.xml

cd configuration-service/
docker build -t nkrishnakumar/istio-config-server:latest .
cd ../

cd service-registry/
docker build -t nkrishnakumar/istio-service-registry:latest .
cd ../

cd api-gateway-service/
docker build -t nkrishnakumar/istio-api-gateway:latest .

cd ../
cd order-service/
docker build -t nkrishnakumar/istio-order-service:latest .

cd ../
cd product-service
docker build -t nkrishnakumar/istio-product-service:latest .

cd ../
cd user-service/
docker build -t nkrishnakumar/istio-user-service:latest .

docker push nkrishnakumar/istio-config-server:latest
docker push nkrishnakumar/istio-service-registry:latest
docker push nkrishnakumar/istio-api-gateway:latest
docker push nkrishnakumar/istio-order-service:latest
docker push nkrishnakumar/istio-product-service:latest
docker push nkrishnakumar/istio-user-service:latest
