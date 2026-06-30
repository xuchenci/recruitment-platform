@echo off
chcp 65001 >nul
echo 正在停止所有 Java 进程...
taskkill /F /IM java.exe 2>nul
echo 所有 Java 进程已停止（包括 RocketMQ 和微服务）。
echo.
pause
