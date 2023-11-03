FROM gradle:8.0-jdk17-alpine AS build
COPY . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build -x test

FROM amazoncorretto:17-alpine-jdk
COPY --from=build /home/gradle/project/build/libs/chat-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]