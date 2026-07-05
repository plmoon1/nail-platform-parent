#!/bin/bash
# ==============================================
# 美甲预约平台 - Nacos 配置批量导入脚本
# ==============================================
# 使用方法：
#   chmod +x import-nacos-config.sh
#   ./import-nacos-config.sh [nacos_addr] [namespace]
#
# 示例：
#   ./import-nacos-config.sh localhost:8848 public
#   ./import-nacos-config.sh 192.168.1.100:8848 dev
# ==============================================

# 默认参数
NACOS_ADDR=${1:-localhost:8848}
NACOS_NAMESPACE=${2:-public}
NACOS_GROUP="NAIL_PLATFORM"
CONFIG_DIR="nacos-config"

# 颜色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "======================================"
echo "  美甲预约平台 - Nacos 配置导入工具"
echo "======================================"
echo ""
echo "📋 导入配置："
echo "   Nacos 地址: $NACOS_ADDR"
echo "   命名空间: $NACOS_NAMESPACE"
echo "   配置分组: $NACOS_GROUP"
echo "   配置目录: $CONFIG_DIR"
echo ""

# 检查 Nacos 是否可访问
echo "🔍 检查 Nacos 连接..."
if ! curl -s --connect-timeout 5 "http://$NACOS_ADDR/nacos/v1/console/health/liveness" > /dev/null; then
    echo -e "${RED}❌ 无法连接到 Nacos: $NACOS_ADDR${NC}"
    echo "请检查："
    echo "  1. Nacos 是否已启动"
    echo "  2. 地址是否正确"
    echo "  3. 网络是否通畅"
    exit 1
fi
echo -e "${GREEN}✅ Nacos 连接正常${NC}"
echo ""

# 检查配置目录
if [ ! -d "$CONFIG_DIR" ]; then
    echo -e "${RED}❌ 配置目录不存在: $CONFIG_DIR${NC}"
    echo "请确保在项目根目录执行此脚本"
    exit 1
fi

# 检查配置文件
config_files=($(find "$CONFIG_DIR" -name "*.yaml" -type f))
if [ ${#config_files[@]} -eq 0 ]; then
    echo -e "${RED}❌ 在 $CONFIG_DIR 中没有找到 YAML 配置文件${NC}"
    exit 1
fi

echo -e "${YELLOW}📦 找到 ${#config_files[@]} 个配置文件${NC}"
echo ""

# 导入配置
success_count=0
fail_count=0

for config_file in "${config_files[@]}"; do
    filename=$(basename "$config_file")
    data_id="${filename}"

    echo "📤 导入配置: $data_id"

    # 读取配置内容
    content=$(cat "$config_file")

    # URL 编码内容
    encoded_content=$(printf '%s' "$content" | jq -Rs '@uri')

    # 发送请求
    response=$(curl -s -X POST "http://${NACOS_ADDR}/nacos/v1/cs/configs" \
      -d "dataId=${data_id}" \
      -d "group=${NACOS_GROUP}" \
      -d "tenant=${NACOS_NAMESPACE}" \
      -d "type=yaml" \
      -d "content=${encoded_content}")

    if [ "$response" = "true" ]; then
        echo -e "${GREEN}  ✅ 导入成功${NC}"
        ((success_count++))
    else
        echo -e "${RED}  ❌ 导入失败: $response${NC}"
        ((fail_count++))
    fi
    echo ""
done

echo "======================================"
echo "  导入完成！"
echo "======================================"
echo -e "${GREEN}✅ 成功: $success_count 个${NC}"
echo -e "${RED}❌ 失败: $fail_count 个${NC}"
echo ""
echo "📖 下一步操作："
echo "  1. 访问 Nacos 控制台: http://$NACOS_ADDR/nacos"
echo "  2. 进入 配置管理 -> 配置列表"
echo "  3. 查看导入的配置（Group: NAIL_PLATFORM）"
echo "  4. 根据实际情况修改数据库密码、Redis 密码等"
echo ""
