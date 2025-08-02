#!/bin/bash

echo "========================================"
echo "CodeVoyant 智能代码审查系统"
echo "========================================"
echo

echo "正在检查Java环境..."
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境，请确保已安装Java 17或更高版本"
    exit 1
fi

echo "正在检查Maven环境..."
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven环境，请确保已安装Maven 3.6或更高版本"
    exit 1
fi

echo "正在清理并编译项目..."
mvn clean compile -DskipTests
if [ $? -ne 0 ]; then
    echo "错误: 项目编译失败"
    exit 1
fi

echo
echo "正在启动CodeVoyant服务..."
echo "服务启动后，请访问: http://localhost:8080"
echo "后台管理界面: http://localhost:8080/admin/"
echo "H2数据库控制台: http://localhost:8080/h2-console"
echo
echo "按 Ctrl+C 停止服务"
echo

mvn spring-boot:run 