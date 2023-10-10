#-----------------------------------------
#Dockerfile for product-service
#-----------------------------------------

#Build stage. Alpine is a CLEAN linux distro
#base image
FROM gradle:8-jdk17-alpine AS builder

#copy files from host machine into the image
COPY --chown=gradle:gradle . /home/gradle/src

#set the directory within image to execute future commands
WORKDIR /home/gradle/src

#run the gradle build from within container
RUN gradle build -x test

#BASE IMAGE
FROM openjdk:17-alpine

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/product-service.jar

ENV MONGO_DB_USERNAME=rootadmin \
    MONGO_DB_PASSWORD=password

EXPOSE 8084

ENTRYPOINT ["java","-jar","/app/product-service.jar"]

