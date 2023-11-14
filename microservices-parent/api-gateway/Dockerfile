FROM openjdk:17-alpine

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/api-gateway.jar

EXPOSE 8181

ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]