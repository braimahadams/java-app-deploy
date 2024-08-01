#!/bin/bash

# Start the application in the background and redirect output to a log file
java -jar build/libs/JavaApp-Deployment-Project-1.0.0-SNAPSHOT.jar &

# Wait for the application to start (adjust the sleep time if necessary)
sleep 30

# Check the HTTP status code
HTTP_STATUS=$(curl -o /dev/null -s -w "%{http_code}\n" http://localhost:8080)

# Check if the status code is 200
if [ "$HTTP_STATUS" -eq 200 ]; then
  echo "Application is running and returned HTTP 200 OK."
  # Optionally, include the logs if you want to see them for further debugging
  # cat app.log
  exit 0
else
  echo "Application returned HTTP $HTTP_STATUS. Failing the build."
  # Output logs for debugging
  cat app.log
  exit 1
fi

