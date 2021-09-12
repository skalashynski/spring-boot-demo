FROM openjdk:16-jdk-alpine
COPY ./build/libs/spring-boot2-demo-1.0.0.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]