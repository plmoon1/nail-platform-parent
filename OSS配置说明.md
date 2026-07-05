# 阿里云 OSS 配置说明

## 配置项说明

在 `application.yml` 中添加以下配置：

```yaml
# 阿里云 OSS 配置
aliyun:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com        # OSS地域节点
    accessKeyId: YOUR_ACCESS_KEY_ID              # AccessKey ID
    accessKeySecret: YOUR_ACCESS_KEY_SECRET       # AccessKey Secret
    bucketName: your-bucket-name                  # 存储桶名称
    domain: https://cdn.yourdomain.com           # 自定义域名（可选）
```

## 配置项详解

### 1. endpoint（地域节点）
阿里云 OSS 的地域节点，根据你的 Bucket 所在地域选择：

- 华东1（杭州）：`oss-cn-hangzhou.aliyuncs.com`
- 华东2（上海）：`oss-cn-shanghai.aliyuncs.com`
- 华北1（青岛）：`oss-cn-qingdao.aliyuncs.com`
- 华北2（北京）：`oss-cn-beijing.aliyuncs.com`
- 华南1（深圳）：`oss-cn-shenzhen.aliyuncs.com`

[查看完整地域节点列表](https://help.aliyun.com/document_detail/31837.html)

### 2. accessKeyId & accessKeySecret
阿里云 AccessKey，用于 API 调用鉴权。

**获取方式：**
1. 登录阿里云控制台：https://oss.console.aliyun.com/
2. 点击右上角头像 → `AccessKeys` → `创建 AccessKey`
3. 建议使用 RAM 子账号的 AccessKey，并授予 OSS 相关权限

### 3. bucketName
存储桶名称，在阿里云 OSS 控制台创建 Bucket 时指定。

**注意事项：**
- Bucket 名称全局唯一
- 只能包含小写字母、数字和短横线
- 长度限制：3-63 个字符

### 4. domain（可选）
自定义域名，用于 CDN 加速或品牌定制。

**配置后效果：**
- 不配置：访问地址为 `https://bucket.oss-cn-hangzhou.aliyuncs.com/file.jpg`
- 配置后：访问地址为 `https://cdn.yourdomain.com/file.jpg`

## 环境变量配置（推荐）

为了避免将敏感信息提交到代码仓库，建议使用环境变量：

### Windows PowerShell
```powershell
$env:OSS_ENDPOINT="oss-cn-hangzhou.aliyuncs.com"
$env:OSS_ACCESS_KEY_ID="your-access-key-id"
$env:OSS_ACCESS_KEY_SECRET="your-access-key-secret"
$env:OSS_BUCKET_NAME="your-bucket-name"
```

### Linux/Mac
```bash
export OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
export OSS_ACCESS_KEY_ID=your-access-key-id
export OSS_ACCESS_KEY_SECRET=your-access-key-secret
export OSS_BUCKET_NAME=your-bucket-name
```

### IDEA 运行配置
在 `Run/Debug Configurations` 中添加环境变量：
```
OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
OSS_ACCESS_KEY_ID=your-access-key-id
OSS_ACCESS_KEY_SECRET=your-access-key-secret
OSS_BUCKET_NAME=your-bucket-name
```

## 创建 Bucket 步骤

1. 登录阿里云 OSS 控制台：https://oss.console.aliyun.com/
2. 点击 `创建 Bucket`
3. 填写 Bucket 信息：
   - **Bucket 名称**：例如 `nail-platform`
   - **地域**：选择就近地域（如华东1-杭州）
   - **存储类型**：标准存储
4. 权限控制：
   - **读写权限**：私有（推荐）
   - **防盗链**：根据需要开启

## 使用示例

```java
@Autowired
private OSSUtil ossUtil;

// 上传用户头像
String avatarUrl = ossUtil.upload(file, "avatar");

// 上传美甲作品图
String artworkUrl = ossUtil.upload(file, "artwork");

// 删除文件
boolean deleted = ossUtil.delete(fileUrl);

// 获取临时访问URL（15分钟有效期）
String tempUrl = ossUtil.getTemporaryUrl(fileUrl, 15);
```

## 成本估算

阿里云 OSS 按量计费：

- **存储费用**：约 ¥0.12/GB/月
- **外网流出流量**：约 ¥0.50/GB（使用 CDN 可降低）
- **请求次数**：前 100 万次免费，之后 ¥0.01/万次

详细价格：https://www.aliyun.com/price/product#/oss/detail

## 安全建议

1. **不要在代码中硬编码 AccessKey**
2. **使用 RAM 子账号**，授予最小权限
3. **开启 Bucket 读写日志监控**
4. **设置防盗链规则**，防止资源被盗用
5. **定期轮换 AccessKey**