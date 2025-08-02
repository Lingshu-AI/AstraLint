@echo off
echo ========================================
echo CodeVoyant 智能代码审查系统
echo ========================================
echo.

echo 正在检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请确保已安装Java 17或更高版本
    pause
    exit /b 1
)

echo 正在检查Maven环境...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven环境，请确保已安装Maven 3.6或更高版本
    pause
    exit /b 1
)

echo 正在清理并编译项目...
call mvn clean compile -DskipTests
if %errorlevel% neq 0 (
    echo 错误: 项目编译失败
    pause
    exit /b 1
)

echo.
echo 正在启动CodeVoyant服务...
echo 服务启动后，请访问: http://localhost:8080
echo 后台管理界面: http://localhost:8080/admin/
echo H2数据库控制台: http://localhost:8080/h2-console
echo.
echo 按 Ctrl+C 停止服务
echo.

call mvn spring-boot:run

pause 