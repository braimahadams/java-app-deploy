#!/bin/bash

# Start the application in the background
java -jar build/libs/JavaApp-Deployment-Project-1.0.0-SNAPSHOT.jar &

# Wait for the application to start (adjust the sleep time if necessary)
sleep 10

# Check the HTTP status code
HTTP_STATUS=$(curl -o /dev/null -s -w "%{http_code}\n" http://localhost:8080)

# Check if the status code is 200
if [ "$HTTP_STATUS" -eq 200 ]; then
  echo "Application is running and returned HTTP 200 OK."
  exit 0
else
  echo "Application returned HTTP $HTTP_STATUS. Failing the build."
  exit 1
fi

