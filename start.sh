#!/bin/sh
cd eureka
gradle clean
gradle build

cd ../book-service
gradle clean
gradle build

cd ../caller-service
gradle clean
gradle build

cd ..
docker-compose up -d
