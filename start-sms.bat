@echo off
echo ==========================================
echo    启动 短信服务 (SMS Service)
echo ==========================================
echo.

REM 设置环境变量（请修改为实际值）
set ALIYUN_ACCESS_KEY_ID=your-access-key-id
set ALIYUN_ACCESS_KEY_SECRET=your-access-key-secret

REM 切换到项目根目录
cd /d %~dp0\..

REM 启动短信服务
echo 正在启动 SMS Service...
start "SMS-Service" cmd /k "cd sms-service && mvn spring-boot:run"

echo.
echo SMS Service 启动中...
echo 端口: 8090
echo.
pause
