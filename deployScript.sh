#! /usr/bin/bash

echo "Logging in"
docker login registry.devops-labs.it -u jenkins-bot31 -p Jenkins-bot-secret-password31

echo "stopping and removing old container"
docker rm -f todo-users

docker ps

echo "starting container"
docker run -d -p 80:8082 --name todo-users --restart always --env spring_profiles_active={PROFILE} registry.devops-labs.it/students/devops31/todo-users:version-{VERSION}

docker ps

exit

