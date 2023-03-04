FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.18_10
MAINTAINER malinkaphann@gmail.com
COPY build/libs/caller-service-0.0.1-SNAPSHOT.jar /app/caller-service.jar
EXPOSE 8080

ARG API_TOKEN
ENV API_TOKEN ${API_TOKEN}

ENTRYPOINT ["/bin/sh", "-c", "java -jar -DAPI_TOKEN=$API_TOKEN /app/caller-service.jar"]