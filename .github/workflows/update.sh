#!/usr/bin/env sh

docker login -u $DOCKER_USER -p $DOCKER_PASSWORD $DOCKER_REGISTRY
docker pull docker.pkg.github.com/damiien/clean-me-up-rest-api/clean-me-up-rest-api
docker stop rest-api || true && docker rm rest-api || true
docker run --network host -e SPRING_PROFILES_ACTIVE=dev -d --name rest-api docker.pkg.github.com/damiien/clean-me-up-rest-api/clean-me-up-rest-api