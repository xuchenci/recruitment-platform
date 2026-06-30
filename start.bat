@echo off
echo ================================================
echo   数智化校企招聘与就业生态平台 - 启动脚本
echo ================================================
echo.

REM 检查Java环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到Java环境，请安装JDK 17+
    pause
    exit /b 1
)

REM 检查Maven环境
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到Maven，请安装Maven 3.8+
    pause
    exit /b 1
)

echo [1/4] 编译项目...
cd /d %~dp0
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo [错误] 项目编译失败
    pause
    exit /b 1
)
echo 编译完成！
echo.

echo [2/4] 启动基础设施...
echo 请确保以下服务已启动：
echo   - MySQL (端口3306)
echo   - Redis (端口6379)
echo   - RocketMQ (端口9876, 10911)
echo   - Nacos (端口8848)
echo.
pause

echo [3/4] 启动微服务...
start "Gateway Service" cmd /k "cd gateway-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

start "Auth Service" cmd /k "cd auth-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

start "Student Service" cmd /k "cd student-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

start "Job Service" cmd /k "cd job-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

start "Recommendation Service" cmd /k "cd recommendation-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

start "Notification Service" cmd /k "cd notification-service && mvn spring-boot:run"
echo.

echo [4/4] 启动完成！
echo.
echo 服务访问地址：
echo   - 网关服务: http://localhost:8080
echo   - Nacos控制台: http://localhost:8848/nacos
echo.
echo 默认账号：
echo   - 用户名: admin
echo   - 密码: admin123
echo.
echo ================================================
echo 所有服务已启动，请查看各服务窗口的日志
echo ================================================
pause
