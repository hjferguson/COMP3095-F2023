FROM openjdk:17-alpine

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/discovery-service.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app/discovery-service.jar"]