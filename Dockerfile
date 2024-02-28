## BUILDER
# 베이스 이미지로 gradle 7.3.1과 JDK 11 버전을 가지고 있는 이미지를 사용합니다
FROM gradle:7.3.1-jdk11 AS build

# 현재 위치의 모든 파일을 도커 컨테이너 내 /home/gradle/src 디렉토리에 복사합니다
COPY --chown=gradle:gradle . /home/gradle/src

# 도커에서 사용할 작업 디렉토리를 지정합니다
WORKDIR /home/gradle/src

# Gradle을 이용해 빌드를 수행하며, 데몬을 사용하지 않거나(test 제외) 빌드를 수행합니다
RUN gradle build --no-daemon -x test

## RUNNING
# 베이스 이미지로 adoptopenjdk의 11버전 jdk-hotspot 런타임을 사용합니다
FROM adoptopenjdk:11-jdk-hotspot

# 이미지를 유지보수하는 사람의 정보를 레이블 형식으로 명시합니다
LABEL maintainer="khjzzm@gmail.com"

# 컨테이너의 4463 포트를 이용하도록 설정합니다
EXPOSE 4463

# 이전 BUILDER 단계에서 빌드된 jar 파일을 app 디렉토리에 복사합니다
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

# Docker 컨테이너가 생성되었을 때 실행될 명령어를 정의합니다
# 여기서는 자바 애플리케이션을 실행하는 java -jar command 를 정의하고 있습니다
# Spring 프로파일 환경변수로 "SERVER_MODE"값을 이용하며 /app/app.jar를 실행합니다
ENTRYPOINT [                                                 \
    "java",                                                  \
    "-Dspring.profiles.active=${SERVER_MODE}",               \
    "-jar",                                                  \
    "/app/app.jar"                                                \
]

