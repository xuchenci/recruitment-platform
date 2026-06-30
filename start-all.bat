@echo off
chcp 65001 >nul
echo ============================================
echo   数智化校企招聘与就业生态平台 - 一键构建启动
echo ============================================
echo.

set JAVA17_HOME=D:\soft\jdk-17.0.11+9
set JAVA8_HOME=D:\soft\jdk_1.8.0_241
set MAVEN_HOME=D:\Java idea\apache-maven-3.9.14
set ROCKETMQ_HOME=D:\Java idea\rocketmq-all-4.7.1-bin-release
set PATH=%JAVA17_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo [1/6] 清理旧的 target 目录...
for /d %%d in (common-service gateway-service auth-service student-service enterprise-service job-service resume-service application-service recommendation-service notification-service sms-service analytics-service) do (
    if exist "%%d\target" (
        echo   删除 %%d\target
        rd /s /q "%%d\target" 2>nul
    )
)
echo.

echo [2/6] Maven 构建所有模块...
set JAVA_HOME=%JAVA17_HOME%
call mvn clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo 构建失败！请检查错误信息。
    pause
    exit /b 1
)
echo 构建成功！
echo.

echo [3/6] 检查基础设施...
echo 正在检查 MySQL (3306)...
powershell -Command "if (Test-NetConnection -ComputerName localhost -Port 3306 -InformationLevel Quiet) { Write-Host '  MySQL: OK' } else { Write-Host '  MySQL: NOT RUNNING!' }"

echo 正在检查 Redis (6379)...
powershell -Command "if (Test-NetConnection -ComputerName localhost -Port 6379 -InformationLevel Quiet) { Write-Host '  Redis: OK' } else { Write-Host '  Redis: NOT RUNNING!' }"
echo.

echo [4/6] 启动 RocketMQ (需要 JDK 8)...
set JAVA_HOME=%JAVA8_HOME%
start "RocketMQ-NameServer" /MIN "%JAVA8_HOME%\bin\java.exe" -server -Xms256m -Xmx256m -cp "%ROCKETMQ_HOME%\lib\*" -Duser.home=D:\tmp -Drocketmq.home.dir="%ROCKETMQ_HOME%" org.apache.rocketmq.namesrv.NamesrvStartup
echo   NameServer 启动中...等待5秒
timeout /t 5 /nobreak >nul

start "RocketMQ-Broker" /MIN "%JAVA8_HOME%\bin\java.exe" -server -Xms256m -Xmx256m -cp "%ROCKETMQ_HOME%\lib\*" -Duser.home=D:\tmp -Drocketmq.home.dir="%ROCKETMQ_HOME%" org.apache.rocketmq.broker.BrokerStartup -n localhost:9876
echo   Broker 启动中...等待8秒
timeout /t 8 /nobreak >nul
echo.

echo [5/6] 启动所有微服务 (JDK 17)...
set JAVA_HOME=%JAVA17_HOME%
echo 启动顺序: Gateway - Auth - Student - Enterprise - Job - Resume - Application - Recommendation - Notification - SMS - Analytics
echo.

start "Gateway-8080" /MIN java -jar gateway-service/target/gateway-service-1.0.0.jar
timeout /t 8 /nobreak >nul

start "Auth-8081" /MIN java -jar auth-service/target/auth-service-1.0.0.jar
timeout /t 5 /nobreak >nul

start "Student-8082" /MIN java -jar student-service/target/student-service-1.0.0.jar
timeout /t 5 /nobreak >nul

start "Enterprise-8083" /MIN java -jar enterprise-service/target/enterprise-service-1.0.0.jar
timeout /t 5 /nobreak >nul

start "Job-8084" /MIN java -jar job-service/target/job-service-1.0.0.jar
timeout /t 5 /nobreak >nul

start "Resume-8085" /MIN java -jar resume-service/target/resume-service-1.0.0.jar
timeout /t 5 /nobreak >nul

start "Application-8086" /MIN java -jar application-service/target/application-service-1.0.0.jar
timeout /t 5 /nobreak >nul

start "Recommendation-8087" /MIN java -jar recommendation-service/target/recommendation-service-1.0.0.jar
timeout /t 5 /nobreak >nul

start "Notification-8088" /MIN java -jar notification-service/target/notification-service-1.0.0.jar
timeout /t 5 /nobreak >nul

start "SMS-8089" /MIN java -jar sms-service/target/sms-service-1.0.0.jar
timeout /t 5 /nobreak >nul

start "Analytics-8090" /MIN java -jar analytics-service/target/analytics-service-1.0.0.jar

echo.
echo [6/6] 所有服务已启动！
echo.
echo ============================================
echo   服务列表:
echo   Gateway:       http://localhost:8080
echo   Auth:          http://localhost:8081
echo   Student:       http://localhost:8082
echo   Enterprise:    http://localhost:8083
echo   Job:           http://localhost:8084
echo   Resume:        http://localhost:8085
echo   Application:  http://localhost:8086
echo   Recommendation:http://localhost:8087
echo   Notification:  http://localhost:8088
echo   SMS:           http://localhost:8089
echo   Analytics:     http://localhost:8090
echo ============================================
echo.
echo 前端请访问: http://localhost:5173
echo   (前端需在 D:\recruitment-frontend 目录运行 npm run dev)
echo.
echo 关闭所有服务请运行: stop-all.bat
echo.
pause
