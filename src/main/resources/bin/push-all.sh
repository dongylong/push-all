#!/usr/bin/env bash
CURRENT_DIR=`pwd`
PROJECT_NAME="push-all"
TARGET_NAME=${CURRENT_DIR}/target
JAR_FILE=CURRENT_DIR/${PROJECT_NAME}-1.0-SNAPSHOT.jar
nohup java -jar ${JAR_FILE} \
--spring.profiles.active=dev \
--push.title=1月26日\(周五\)重大更新 \
--push.alert=点击查看更新详情 \
--push.extra.title=1月26日更新详情 \
--push.extra.style=dark \
--push.extra.bgcolor=#000000 \
> ${CURRENT_DIR}/push_$(date +%y%m%d%H).log &