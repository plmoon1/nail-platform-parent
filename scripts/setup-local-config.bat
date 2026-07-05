@echo off
REM ==============================================
REM 美甲预约平台 - 本地配置文件生成脚本 (Windows)
REM ==============================================

set TEMPLATE_FILE=application-local.yml.template
set SERVICES=auth-server user-server service-server reserve-server marketing-server notice-server gateway-gw

echo ======================================
echo   美甲预约平台 - 本地配置生成工具
echo ======================================
echo.

REM 检查模板文件是否存在
if not exist "%TEMPLATE_FILE%" (
    echo ❌ 错误：找不到模板文件 %TEMPLATE_FILE%
    pause
    exit /b 1
)

echo 📋 开始为各服务生成配置文件...
echo.

REM 复制配置文件到各服务
for %%s in (%SERVICES%) do (
    set TARGET_DIR=%%s\src\main\resources
    set TARGET_FILE=!TARGET_DIR!\application-local.yml

    REM 检查目标目录是否存在
    if not exist "!TARGET_DIR!" (
        echo ⚠️  警告：!TARGET_DIR! 不存在，跳过 %%s
    ) else (
        REM 复制文件
        copy "%TEMPLATE_FILE%" "!TARGET_FILE!" >nul

        if !errorlevel! equ 0 (
            echo ✅ %%s - 配置文件已生成
        ) else (
            echo ❌ %%s - 配置文件生成失败
        )
    )
)

echo.
echo ======================================
echo   配置文件生成完成！
echo ======================================
echo.
echo 📝 下一步操作：
echo   1. 修改任意服务的 application-local.yml 文件
echo   2. 配置数据库密码、Redis 密码等信息
echo   3. 在 IDEA 中启动服务时，设置 Active profiles: local
echo.
echo 📖 详细说明请查看: LOCAL_SETUP_GUIDE.md
echo.
pause