#!/bin/bash

# Delete all deployments except "kubernetes"
echo "Deleting deployments..."
deployments=$(kubectl get deployments -o custom-columns=NAME:.metadata.name --no-headers)

for deployment in $deployments; do
  if [ "$deployment" != "kubernetes" ]; then
    echo "Deleting deployment: $deployment"
    kubectl delete deployment "$deployment"
  else
    echo "Skipping deployment: $deployment"
  fi
done

# Delete all services except "kubernetes"
echo "Deleting services..."
services=$(kubectl get services -o custom-columns=NAME:.metadata.name --no-headers)

for service in $services; do
  if [ "$service" != "kubernetes" ]; then
    echo "Deleting service: $service"
    kubectl delete service "$service"
  else
    echo "Skipping service: $service"
  fi
done

echo "All specified deployments and services have been deleted."
