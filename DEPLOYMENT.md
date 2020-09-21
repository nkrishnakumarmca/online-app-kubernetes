# Installation
 - Install Helm from this link https://helm.sh/docs/intro/install/ (OR) Run get_helm.sh file
 
 - Install Istio
    - https://docs.microsoft.com/en-us/azure/aks/servicemesh-istio-install?pivots=client-operating-system-macos
    - use `istioctl install` command
    - Enable proxy injection for name space `kubectl label namespace kk istio-injection=enabled`
    - Validate installation `istioctl analyze --all-namespaces`
    - Before deploying services enable istio, if not need to recreate the services
    
 - Install kafka using helm 
    - `helm repo add bitnami https://charts.bitnami.com/bitnami`
    - `helm install kafka bitnami/kafka` - Here kafka is the name of service, it can be anything of your choice
    - `helm status kafka` - To get the producer and consumer details at any time
    
 - Command to set name space at context level
 
    `kubectl config set-context --current --namespace kk`
    
  - Configure JWT
    
    - Follow this link 
    https://istio.io/latest/docs/tasks/security/authorization/authz-jwt/
 
 
#Pre-requisite
   - Should have a valid subscription in Azure
   - Create a Kubernetes cluster in Azure

#Build Docker image
- Use the script `build.sh` in parent directory to build and push the code to hub.docker.com to get the latest image


# Manifest Files:
 - All K8 files are placed under k8s folder 
 - Deploy dependent services mysql, mongo, redis and kafka services using the below files in k8s/apps folder
        
        - kubectl apply -f mongodb-stateful.yml
        - kubectl apply -f mysql-deployment.yml
        - kubectl apply -f mysql-pv.yml
        - kubectl apply -f redis-deployment.yml
        
        
 - Deploying micro services in kubernetes inside namespace kk from k8s/apps folder
 
        - kubectl apply -f configservice-k8s.yml
        - kubectl apply -f serviceregistry-k8s.yml
        - kubectl apply -f orderservice-k8s.yml
        - kubectl apply -f productservice-k8s.yml
        - kubectl apply -f userservice-k8s.yml
        
 - Apply Gateway related files in k8s folder
 
        - Configure ingress gateway service using gateway.yml file
        - Configure virtual service using virtual-service.yml and configure the routes
