#!/bin/bash

# Repository 저장경로 설정
REPOSITORY=/home/ec2-user/app
LOG=/home/ec2-user/logs

echo "> pgrep -f yeoboya-lunch-api"
CURRENT_PID=$(pgrep -f yeoboya-lunch-api)

# 현재 구동중인 애플리케이션이 있을 경우 종료
if [ -z $CURRENT_PID ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID sleep 5"
    kill -15 $CURRENT_PID
    sleep 5
fi

# Build 파일 복사
echo "> cp $REPOSITORY/build/libs/*.jar $REPOSITORY/"
cp $REPOSITORY/build/libs/*.jar $REPOSITORY/

# JAR 실행 파일 이름 확인
JAR_NAME=$(ls -tr $REPOSITORY/ |grep 'yeoboya-lunch-api' | tail -n 1)
echo "> JAR_NAME: $JAR_NAME"

# JAR 실행
DATE=$(date '+%Y-%m-%d')
nohup java -jar -Dspring.profiles.active=prod,prod-oauth $REPOSITORY/$JAR_NAME > $LOG/latest-deploy-$DATE.log 2>&1 &

echo "> JAR 파일 실행 완료"
