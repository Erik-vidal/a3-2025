#!/bin/bash

# Get the JAR from the container
echo "Copying JAR from container..."
docker cp estoque_maven:/app/target/sistema-estoque-1.0-SNAPSHOT.jar ./sistema-estoque.jar

# Run the desktop application
echo "Starting the desktop application..."
java -jar sistema-estoque.jar 