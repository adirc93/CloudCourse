#! /bin/bash

#First, we wil start deployment running thr image and expose it as a Service using the *YAML configuration
kubectl apply -f hw2_resources.yaml

sleep 6


#get the ip of the server
#also possible to get with grep
serverIpAddr=$(kubectl get service/hw2 -o jsonpath='{.spec.clusterIP}')

#increase load up to 20 pods
#query the HTTP server
SECONDS=$(date +%s)
end=$((SECONDS+20))
while [ ${SECONDS} -lt ${end} ]; do wget $serverIpAddr -q -O- ;
done

#*******Upgrade to version 2********

#update the relevant scope
sed -ie 's/reutdockerbgu\/k8s:hw2_v1/reutdockerbgu\/k8s:hw2_v2/' hw2_resources.yaml
echo "changed to v2"
kubectl apply -f hw2_resources.yaml

sleep 10

serverIpAddr=$(kubectl get Service/hw2 -o jsonpath='{.spec.clusterIP}')
#query the server and print the output
sleep 6
wget $serverIpAddr -q -O-

#rollback to previous version
sed -ie	's/reutdockerbgu\/k8s:hw2_v2/reutdockerbgu\/k8s:hw2_v1/' hw2_resources.yaml
kubectl	apply -f hw2_resources.yaml
echo "changed to v1"

sleep 6

serverIpAddr=$(kubectl get service/hw2 -o jsonpath='{.spec.clusterIP}')
sleep 6
wget $serverIpAddr -q -O-

kubectl delete hpa hw2        
kubectl delete services hw2   
kubectl delete deployments hw2

echo great Success
