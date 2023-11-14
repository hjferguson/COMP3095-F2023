FROM gradle:8-jdk17-alpine AS builder

#copy files from host machine into the image
COPY --chown=gradle:gradle . /home/gradle/src

#set the directory within image to execute future commands
WORKDIR /home/gradle/src

#run the gradle build from within container
RUN gradle build -x test



FROM openjdk:17-alpine

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/discovery-service.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app/discovery-service.jar"]