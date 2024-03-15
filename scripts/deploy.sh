#!/bin/bash
BUILD_ZIP=$(ls /home/ec2-user/action/*.zip)
ZIP_NAME=$(basename $BUILD_ZIP)
echo "> Build file name: $ZIP_NAME" >> /home/ec2-user/action/deploy.log

echo "> Check pid of the current application" >> /home/ec2-user/action/deploy.log
CURRENT_PID=$(pgrep -f .jar)

if [ -z $CURRENT_PID ]
then
  echo "> No application is currently running, so no shutdown required." >> /home/ec2-user/action/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Unzip $BUILD_ZIP" >> /home/ec2-user/action/deploy.log
unzip $BUILD_ZIP -d /home/ec2-user/action/

DEPLOY_JAR=$(ls /home/ec2-user/action/*.jar)
echo "> Deploy $DEPLOY_JAR" >> /home/ec2-user/action/deploy.log
nohup java -jar $DEPLOY_JAR >> /home/ec2-user/deploy.log 2>>/home/ec2-user/action/deploy_err.log &
