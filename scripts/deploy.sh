#!/bin/bash

# /home/ec2-user/build/build/libs 디렉토리에 있는 zip 파일을 찾아서 BUILD_ZIP 변수에 할당합니다.
BUILD_ZIP=$(ls /home/ec2-user/build/build/libs/*.zip)

# BUILD_ZIP 경로에서 파일 이름만 추출하여 ZIP_NAME 변수에 할당합니다.
ZIP_NAME=$(basename $BUILD_ZIP)

# 추출한 zip 파일 이름을 로그로 저장합니다.
echo "> 빌드 파일 이름: $ZIP_NAME" >> /home/ec2-user/build/deploy.log

# 현재 실행 중인 .jar 프로세스의 PID를 확인하여 CURRENT_PID 변수에 할당합니다.
echo "> 현재 실행 중인 애플리케이션의 PID 확인" >> /home/ec2-user/build/deploy.log
CURRENT_PID=$(pgrep -f .jar)

# 만약 CURRENT_PID 변수가 비어 있지 않으면 (실행 중인 .jar 프로세스가 존재하면) 이 프로세스를 종료합니다.
if [ -z $CURRENT_PID ]
then
  # 실행 중인 애플리케이션이 없으므로 종료가 필요 없다는 메시지를 로그로 남깁니다.
  echo "> 현재 실행 중인 애플리케이션이 없기 때문에 종료하지 않아도 됩니다." >> /home/ec2-user/build/deploy.log
else
  # 실행 중인 .jar 프로세스를 종료하고 5초간 대기합니다.
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# zip 파일의 압축을 해제합니다.
echo "> $BUILD_ZIP 압축 해제" >> /home/ec2-user/build/deploy.log
unzip $BUILD_ZIP -d /home/ec2-user/build/libs/

# 압축 해제 후 생성된 .jar 파일을 찾아서 DEPLOY_JAR 변수에 할당합니다.
DEPLOY_JAR=$(ls /home/ec2-user/build/libs/*.jar)

# .jar 파일을 실행합니다.
echo "> $DEPLOY_JAR 배포" >> /home/ec2-user/build/deploy.log
nohup java -jar -Dspring.profiles.active=prod $DEPLOY_JAR >> /home/ec2-user/deploy.log 2>>/home/ec2-user/build/deploy_err.log &
