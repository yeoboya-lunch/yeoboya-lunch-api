## BUILDER 단계
FROM --platform=linux/amd64 gradle:7.3.1-jdk11 AS build

# 작업 디렉토리 설정
WORKDIR /home/gradle/src

# Gradle 캐싱 최적화: dependencies 먼저 복사 후, 코드 복사
COPY --chown=gradle:gradle build.gradle settings.gradle ./
COPY --chown=gradle:gradle gradle ./gradle
COPY --chown=gradle:gradle gradlew ./

# gradlew 실행 권한 추가
RUN chmod +x gradlew

# Gradle 의존성 다운로드 (캐싱 최적화)
RUN ./gradlew build --dependencies --no-daemon || return 0

# 소스 코드 복사 (캐싱된 의존성을 활용하기 위해 나중에 복사)
COPY --chown=gradle:gradle . .

# Gradle 빌드 실행 (테스트 제외)
## todo 수정 필요
ARG TEST_ENABLED=false
ARG DOCS_ENABLED=false
RUN ./gradlew clean build -x test
#RUN ./gradlew clean build -Ptest=true
#RUN ./gradlew clean build -Pdocs=true

## RUNNING 단계 (jdk x)
FROM --platform=linux/amd64 eclipse-temurin:11-jre

# 유지보수자 정보
LABEL maintainer="Hyunjin Kim <khjzzm@gmail.com>"

# 컨테이너 내부에서 실행할 포트
EXPOSE 8080

# JAR 파일 복사
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

# 컨테이너 실행 시 실행될 명령어
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev}", "-jar", "/app/app.jar"]
