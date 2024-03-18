#!/bin/bash

REPOSITORY=/home/ec2-user/app

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f yeoboya-lunch-api)

echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi
echo "> 새 어플리케이션 배포"

echo "> 필요한 디렉터리가 있는지 확인 후 생성"
mkdir -p $REPOSITORY/jar/

echo "> Build 파일 복사"
cp $REPOSITORY/build/libs/*.jar $REPOSITORY/jar/

JAR_NAME=$(ls -tr $REPOSITORY/jar/ |grep 'yeoboya-lunch-api' | tail -n 1)
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME
echo "> JAR Name: $JAR_NAME"
nohup java -jar -Dspring.profiles.active=prod $REPOSITORY/jar/$JAR_NAME &
