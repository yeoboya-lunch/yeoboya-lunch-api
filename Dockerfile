## BUILFER
FROM adoptopenjdk:11-jdk-hotspot as builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

## RUNNING
FROM adoptopenjdk:11-jdk-hotspot
LABEL maintainer="khjzzm@gmail.com"
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 463
ENTRYPOINT [                                                 \
    "java",                                                  \
    "-Dspring.profiles.active=${SERVER_MODE}",               \
    "-Djasypt.encryptor.password=${JASYPT}",                 \
    "-jar",                                                  \
    "app.jar"                                                \
]
