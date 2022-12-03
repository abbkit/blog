#!/usr/bin/env bash

#https://ghproxy.com/
#git clone https://ghproxy.com/https://github.com/abbkit/blog.git
#删除所有虚悬镜像
docker rmi $(docker images --filter dangling=true -q)
# 会删除24小时前的虚悬镜像
docker image prune -f --filter until=24h

cd blog/server
git pull
docker stop abbkit
docker rm abbkit
mvn clean install -Dmaven.test.skip=true
docker build --network=abbkit -t abbkit:latest .
docker-compose -f abbkit-compose.yml up -d

echo "copy html page..."
cd ..
pwd
rm -rf /work/sourcecode/blog/html/*
cp -rf html/* /work/sourcecode/blog/html
echo "started ok!"
