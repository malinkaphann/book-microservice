FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.18_10
MAINTAINER malinkaphann@gmail.com
COPY build/libs/eureka-service-0.0.1-SNAPSHOT.jar /app/eureka-service.jar
ENTRYPOINT ["java", "-jar", "/app/eureka-service.jar"]