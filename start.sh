#!/bin/bash

echo "================================================"
echo "  数智化校企招聘与就业生态平台 - 启动脚本"
echo "================================================"
echo ""

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "[错误] 未找到Java环境，请安装JDK 17+"
    exit 1
fi

# 检查Maven环境
if ! command -v mvn &> /dev/null; then
    echo "[错误] 未找到Maven，请安装Maven 3.8+"
    exit 1
fi

echo "[1/4] 编译项目..."
mvn clean install -DskipTests
if [ $? -ne 0 ]; then
    echo "[错误] 项目编译失败"
    exit 1
fi
echo "编译完成！"
echo ""

echo "[2/4] 启动基础设施..."
echo "请确保以下服务已启动："
echo "  - MySQL (端口3306)"
echo "  - Redis (端口6379)"
echo "  - RocketMQ (端口9876, 10911)"
echo "  - Nacos (端口8848)"
echo ""
read -p "按回车键继续..."

echo "[3/4] 启动微服务..."
gnome-terminal --tab --title="Gateway" -- mvn -f gateway-service spring-boot:run
sleep 5

gnome-terminal --tab --title="Auth" -- mvn -f auth-service spring-boot:run
sleep 5

gnome-terminal --tab --title="Student" -- mvn -f student-service spring-boot:run
sleep 5

gnome-terminal --tab --title="Job" -- mvn -f job-service spring-boot:run
sleep 5

gnome-terminal --tab --title="Recommendation" -- mvn -f recommendation-service spring-boot:run
sleep 5

gnome-terminal --tab --title="Notification" -- mvn -f notification-service spring-boot:run
echo ""

echo "[4/4] 启动完成！"
echo ""
echo "服务访问地址："
echo "  - 网关服务: http://localhost:8080"
echo "  - Nacos控制台: http://localhost:8848/nacos"
echo ""
echo "默认账号："
echo "  - 用户名: admin"
echo "  - 密码: admin123"
echo ""
echo "================================================"
echo "所有服务已启动，请查看各服务窗口的日志"
echo "================================================"
