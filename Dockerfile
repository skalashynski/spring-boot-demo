FROM ubuntu

RUN apt-get update
RUN apt install default-jre -y

COPY target/demo-0.0.1-SNAPSHOT.jar /opt/app/demo-spring/demo.jar
CMD ["java", "-jar", "/opt/app/demo-spring/demo.jar"]