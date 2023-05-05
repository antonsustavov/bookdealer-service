FROM openjdk:20-ea-17-jdk-oraclelinux8
MAINTAINER sustavov
COPY target/bookdealer-0.0.1-SNAPSHOT.jar bookdealer-service.jar
ENTRYPOINT ["java","-jar","/bookdealer-service.jar"]