#!/bin/bash
set -e

echo "🔍 检查 MySQL 容器状态..."
if [ "$(docker ps -q -f name=pms_mysql)" ]; then
    echo "✅ MySQL 容器已在运行。"
elif [ "$(docker ps -aq -f name=pms_mysql)" ]; then
    echo "🔄 重新启动 MySQL 容器..."
    docker start pms_mysql
else
    echo "🐳 启动新的 MySQL 容器..."
    docker-compose up -d
fi

echo "⏳ 等待 MySQL 启动 (预计 10 秒)..."
sleep 10

echo "🏃 启动 Spring Boot 应用..."
./gradlew bootRun
