#!/bin/bash

# Repository 저장경로 설정
REPOSITORY=/home/ec2-user/app

echo -e "\n> 현재 구동중인 애플리케이션 PID 확인"
CURRENT_PID=$(pgrep -f yeoboya-lunch-api)
echo -e "> 현재 구동중인 애플리케이션 PID: $CURRENT_PID"

# 현재 구동중인 애플리케이션이 있을 경우 종료
if [ -z $CURRENT_PID ]; then
    echo -e "\n> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo -e "\n> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo -e "\n> 새 어플리케이션 배포"

# Build 파일 복사
echo -e "\n> Build 파일 복사"
cp $REPOSITORY/build/libs/*.jar $REPOSITORY/

# JAR 실행 파일 이름 확인
JAR_NAME=$(ls -tr $REPOSITORY/jar/ |grep 'yeoboya-lunch-api' | tail -n 1)
echo -e "\n> $JAR_NAME 에 실행권한 추가"

# 해당 JAR 파일에 실행권한 추가
chmod +x $REPOSITORY/jar/$JAR_NAME
echo -e "\n> JAR_NAME: $JAR_NAME"

# JAR 실행
nohup java -jar -Dspring.profiles.active=prod $REPOSITORY/jar/$JAR_NAME &

echo -e "\n> JAR 파일 실행 완료"
