#!/bin/bash
BUILD_ZIP=$(ls /home/ec2-user/action/build/libs/*.zip)
ZIP_NAME=$(basename $BUILD_ZIP)
echo "> Build file name: $ZIP_NAME" >> /home/ec2-user/action/deploy.log

echo "> Copy build file" >> /home/ec2-user/action/deploy.log
DEPLOY_PATH=/home/ec2-user/action/
cp $BUILD_ZIP $DEPLOY_PATH

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

DEPLOY_ZIP=$DEPLOY_PATH$ZIP_NAME
echo "> Unzip $DEPLOY_ZIP" >> /home/ec2-user/action/deploy.log
unzip $DEPLOY_ZIP -d $DEPLOY_PATH

DEPLOY_JAR=$(ls $DEPLOY_PATH*.jar)
echo "> Deploy $DEPLOY_JAR" >> /home/ec2-user/action/deploy.log
nohup java -jar $DEPLOY_JAR >> /home/ec2-user/deploy.log 2>>/home/ec2-user/action/deploy_err.log &
