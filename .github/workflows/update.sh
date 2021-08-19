#!/usr/bin/env sh

docker login -u $DOCKER_USER -p $DOCKER_PASSWORD $DOCKER_REGISTRY
docker pull docker.pkg.github.com/newsroom-technologies/arktype/clean-me-up-rest-api
docker stop rest-api || true && docker rm rest-api || true
docker run --network host -e SPRING_PROFILES_ACTIVE=default -d --name rest-api docker.pkg.github.com/arktype/clean-me-up-rest-api