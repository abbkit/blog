#!/usr/bin/env bash

#https://ghproxy.com/
#git clone https://ghproxy.com/https://github.com/abbkit/blog.git
docker stop abbkit
docker rm abbkit
mvn clean install -Dmaven.test.skip=true
docker build --network=abbkit -t abbkit:0.0.1 .
docker-compose -f abbkit-compose.yml up -d
