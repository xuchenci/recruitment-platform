@echo off
chcp 65001 >nul
echo ============================================
echo   数智化校企招聘平台 - 构建与启动指南
echo ============================================
echo.
echo 由于沙盒限制，请按以下步骤操作：
echo.
echo === 步骤 1: 构建 ===
echo 打开 CMD 或 PowerShell，执行：
echo.
echo   cd D:\recruitment-platform
echo   set JAVA_HOME=D:\soft\jdk-17.0.11+9
echo   "D:\Java idea\apache-maven-3.9.14\bin\mvn.cmd" clean package -DskipTests
echo.
echo === 步骤 2: 启动基础设施 ===
echo   确保 MySQL(3306) 和 Redis(6379) 已运行
echo   然后启动 RocketMQ (用 JDK 8)：
echo.
echo   set JAVA_HOME=D:\soft\jdk_1.8.0_241
echo   set ROCKETMQ_HOME=D:\Java idea\rocketmq-all-4.7.1-bin-release
echo   start "NameServer" /MIN "%JAVA_HOME%\bin\java.exe" -server -Xms256m -Xmx256m -cp "%ROCKETMQ_HOME%\lib\*" -Duser.home=D:\tmp -Drocketmq.home.dir="%ROCKETMQ_HOME%" org.apache.rocketmq.namesrv.NamesrvStartup
echo   timeout /t 5
echo   start "Broker" /MIN "%JAVA_HOME%\bin\java.exe" -server -Xms256m -Xmx256m -cp "%ROCKETMQ_HOME%\lib\*" -Duser.home=D:\tmp -Drocketmq.home.dir="%ROCKETMQ_HOME%" org.apache.rocketmq.broker.BrokerStartup -n localhost:9876
echo   timeout /t 8
echo.
echo === 步骤 3: 启动微服务 ===
echo   或者直接运行 start-all.bat
echo.
echo ============================================
pause
