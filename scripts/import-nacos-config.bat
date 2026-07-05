@echo off
REM ==============================================
REM 美甲预约平台 - Nacos 配置批量导入脚本 (Windows)
REM ==============================================
REM 使用方法：
REM   import-nacos-config.bat [nacos_addr] [namespace]
REM
REM 示例：
REM   import-nacos-config.bat localhost:8848 public
REM   import-nacos-config.bat 192.168.1.100:8848 dev
REM ==============================================

setlocal enabledelayedexpansion

REM 默认参数
if "%1"=="" (
    set NACOS_ADDR=localhost:8848
) else (
    set NACOS_ADDR=%1
)

if "%2"=="" (
    set NACOS_NAMESPACE=public
) else (
    set NACOS_NAMESPACE=%2
)

set NACOS_GROUP=NAIL_PLATFORM
set CONFIG_DIR=nacos-config

echo ======================================
echo   美甲预约平台 - Nacos 配置导入工具
echo ======================================
echo.
echo 📋 导入配置：
echo    Nacos 地址: %NACOS_ADDR%
echo    命名空间: %NACOS_NAMESPACE%
echo    配置分组: %NACOS_GROUP%
echo    配置目录: %CONFIG_DIR%
echo.

REM 检查 Nacos 连接
echo 🔍 检查 Nacos 连接...
curl -s --connect-timeout 5 "http://%NACOS_ADDR%/nacos/v1/console/health/liveness" >nul
if errorlevel 1 (
    echo ❌ 无法连接到 Nacos: %NACOS_ADDR%
    echo 请检查：
    echo   1. Nacos 是否已启动
    echo   2. 地址是否正确
    echo   3. 网络是否通畅
    pause
    exit /b 1
)
echo ✅ Nacos 连接正常
echo.

REM 检查配置目录
if not exist "%CONFIG_DIR%" (
    echo ❌ 配置目录不存在: %CONFIG_DIR%
    echo 请确保在项目根目录执行此脚本
    pause
    exit /b 1
)

REM 检查 curl 命令
where curl >nul 2>&1
if errorlevel 1 (
    echo ❌ 未找到 curl 命令
    echo 请安装 curl 或使用 Windows 10+ 自带版本
    pause
    exit /b 1
)

echo.
echo 📦 开始导入配置...
echo.

set success_count=0
set fail_count=0

REM 遍历配置文件
for %%f in (%CONFIG_DIR%\*.yaml) do (
    set filename=%%~nxf
    set data_id=%%~nxf

    echo 📤 导入配置: !data_id!

    REM 读取文件内容并通过 curl 发送
    REM 注意：Windows cmd 对特殊字符处理有限，建议使用 PowerShell 或 Git Bash
    curl -s -X POST "http://%NACOS_ADDR%/nacos/v1/cs/configs" ^
      -d "dataId=!data_id!" ^
      -d "group=%NACOS_GROUP%" ^
      -d "tenant=%NACOS_NAMESPACE%" ^
      -d "type=yaml" ^
      --data-binary @%%f

    if errorlevel 1 (
        echo   ❌ 导入失败
        set /a fail_count+=1
    ) else (
        echo   ✅ 导入成功
        set /a success_count+=1
    )
    echo.
)

echo ======================================
echo   导入完成！
echo ======================================
echo ✅ 成功: %success_count% 个
echo ❌ 失败: %fail_count% 个
echo.
echo 📖 下一步操作：
echo   1. 访问 Nacos 控制台: http://%NACOS_ADDR%/nacos
echo   2. 进入 配置管理 -^> 配置列表
echo   3. 查看导入的配置（Group: NAIL_PLATFORM）
echo   4. 根据实际情况修改数据库密码、Redis 密码等
echo.
pause
