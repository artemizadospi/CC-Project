#!/bin/bash

# Directory where your YAML files are stored
DIR="./"  # Change this to your directory if needed

# Loop through all service YAML files and apply them
for service_file in "$DIR"*.yaml; do
  if [ "$service_file" != "./kind-config-three-nodes.yaml" ]; then
    echo "Applying $service_file..."
    kubectl apply -f "$service_file"
  fi
done

echo "All services applied successfully!"
