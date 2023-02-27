FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.18_10
MAINTAINER malinkaphann@gmail.com
COPY build/libs/book-service-0.0.1-SNAPSHOT.jar /app/book-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-DDB_HOST=postgres", "-DDB_PORT=5432", "-DDB_NAME=book_db", "-jar","/app/book-service.jar"]