#!/usr/bin/env bash

git clone https://github.com/abbkit/blog.git
mvn clean install -Dmaven.test.skip=true
docker build --network=abbkit -t abbkit:0.0.1 .
docker stop abbkit
docker rm abbkit
docker-compose -f abbkit-compose.yml up -d
