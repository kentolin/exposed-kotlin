# user/Dockerfile
FROM gradle:8.12-jdk-21-and-23-corretto AS build
WORKDIR /app
COPY ../.. .
RUN gradle :user:build --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/user/build/libs/user.jar .
CMD ["java", "-jar", "user.jar", "--port=8081"]