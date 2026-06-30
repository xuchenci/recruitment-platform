@echo off
echo ==========================================
echo    启动 数据分析服务 (Analytics Service)
echo ==========================================
echo.

REM 切换到项目根目录
cd /d %~dp0\..

REM 启动数据分析服务
echo 正在启动 Analytics Service...
start "Analytics-Service" cmd /k "cd analytics-service && mvn spring-boot:run"

echo.
echo Analytics Service 启动中...
echo 端口: 8090
echo 提供 ECharts 图表数据 API
echo.
pause
