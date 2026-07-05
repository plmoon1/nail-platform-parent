#!/bin/bash
# ==============================================
# 美甲预约平台 - 本地配置文件生成脚本
# ==============================================
# 使用方法：
#   chmod +x setup-local-config.sh
#   ./setup-local-config.sh
# ==============================================

TEMPLATE_FILE="application-local.yml.template"
SERVICES=("auth-server" "user-server" "service-server" "reserve-server" "marketing-server" "notice-server" "gateway-gw")

echo "======================================"
echo "  美甲预约平台 - 本地配置生成工具"
echo "======================================"
echo ""

# 检查模板文件是否存在
if [ ! -f "$TEMPLATE_FILE" ]; then
    echo "❌ 错误：找不到模板文件 $TEMPLATE_FILE"
    exit 1
fi

echo "📋 开始为各服务生成配置文件..."
echo ""

# 复制配置文件到各服务
for service in "${SERVICES[@]}"; do
    target_dir="$service/src/main/resources"
    target_file="$target_dir/application-local.yml"

    # 检查目标目录是否存在
    if [ ! -d "$target_dir" ]; then
        echo "⚠️  警告：$target_dir 不存在，跳过 $service"
        continue
    fi

    # 复制文件
    cp "$TEMPLATE_FILE" "$target_file"

    if [ $? -eq 0 ]; then
        echo "✅ $service - 配置文件已生成"
    else
        echo "❌ $service - 配置文件生成失败"
    fi
done

echo ""
echo "======================================"
echo "  配置文件生成完成！"
echo "======================================"
echo ""
echo "📝 下一步操作："
echo "  1. 修改任意服务的 application-local.yml 文件"
echo "  2. 配置数据库密码、Redis 密码等信息"
echo "  3. 在 IDEA 中启动服务时，设置 Active profiles: local"
echo ""
echo "📖 详细说明请查看: LOCAL_SETUP_GUIDE.md"
echo ""