#Dockerfile for WCBE
# Initializes a new build stage and sets the base image for the succeeding instructions.
FROM maven:3.6.0-jdk-11-slim AS build

# The WORKDIR instruction sets the working directory for any RUN, CMD, ENTRYPOINT, COPY and ADD instructions
WORKDIR /usr/src/app/WorkConnectBackend/tracker

# COPY instruction copies new files or directories from <src> and adds them to the filesystem of the container at the path <dest>.
COPY . /usr/src/app/

# RUN instruction will execute any commands in a new layer on top of the current image and commit the results.
RUN mvn clean install -Dmaven.test.skip=true

FROM openjdk:11-jre-slim

ARG JAVA_HOME=/usr/lib/jvm/java-1.11.0-openjdk-amd64

#RUN apk add tzdata curl

RUN cp /usr/share/zoneinfo/Asia/Manila /etc/localtime

RUN echo "Asia/Manila" > /etc/timezone

#WCBE TEST

#RUN addgroup -S spring && adduser -S spring -G spring

#USER spring:spring

COPY --from=build  /usr/src/app/WorkConnectBackend/tracker/target/tracker-0.0.1-SNAPSHOT.war app.war

COPY --from=build /usr/src/app/WorkConnectBackend/tracker/src/main/resources resources

EXPOSE 7600

EXPOSE 7500

ENTRYPOINT ["java","-Duser.timezone=Asia/Manila","-Dlog4j2.formatMsgNoLookups=true","-jar","/app.war"]
