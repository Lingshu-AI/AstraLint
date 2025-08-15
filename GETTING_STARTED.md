# 🚀 快速上手指南

欢迎使用 AstraLint！这份指南将帮助你在几分钟内启动并运行这个 AI 驱动的代码审查系统。

## 📋 开始之前

确保你的环境满足以下要求：

- ☑️ Java 17 或更高版本
- ☑️ Maven 3.6 或更高版本
- ☑️ 阿里云 DashScope API 密钥 ([获取地址](https://dashscope.aliyun.com/))

## ⚡ 3 分钟快速启动

### 第 1 步：下载并配置

```bash
# 克隆项目
git clone https://github.com/yourusername/AstraLint.git
cd AstraLint

# 配置环境变量
cp env.example .env
```

编辑 `.env` 文件，设置你的 API 密钥：

```bash
DASHSCOPE_API_KEY=your_api_key_here
JWT_SECRET=your_jwt_secret_at_least_256_bits_long
```

### 第 2 步：启动应用

```bash
mvn spring-boot:run
```

等待启动完成，你将看到类似的输出：

```
Started CodeVoyantApplication in 15.234 seconds
```

### 第 3 步：验证安装

打开浏览器访问：

- 🏠 **首页**: http://localhost:8080
- 🔍 **代码审查**: http://localhost:8080/secure-review.html
- ⚙️ **管理后台**: http://localhost:8080/admin/

默认登录信息：

```
用户名: admin
密码: admin123
```

## 🎯 第一次代码审查

### 使用 Web 界面

1. 访问 http://localhost:8080/secure-review.html
2. 填写项目信息
3. 粘贴你的代码变更（git diff 输出）
4. 选择审查类型
5. 点击"开始审查"

### 使用 API

```bash
curl -X POST http://localhost:8080/api/code-review/quick \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": "my-project",
    "mergeRequestId": "123",
    "diffContent": "diff --git a/src/main.js b/src/main.js\n+console.log(\"Hello World\");",
    "reviewType": "COMPREHENSIVE"
  }'
```

## 🔗 配置 Git 仓库集成

### GitLab 集成

1. 在 GitLab 项目中，前往 **Settings → Webhooks**
2. 添加 Webhook URL: `http://your-server:8080/api/webhook/gitlab`
3. 选择触发事件: **Merge request events**
4. 设置 Secret Token（在 `.env` 中配置 `GITLAB_WEBHOOK_SECRET`）

### GitHub 集成

1. 在 GitHub 仓库中，前往 **Settings → Webhooks**
2. 添加 Webhook URL: `http://your-server:8080/api/webhook/github`
3. 选择事件: **Pull requests**
4. 设置 Secret（在 `.env` 中配置 `GITHUB_WEBHOOK_SECRET`）

## ⚙️ 基础配置

### 管理后台设置

1. 登录管理后台 http://localhost:8080/admin/
2. **AI 模型管理**: 配置 AI 模型参数
3. **仓库管理**: 添加要监控的代码仓库
4. **系统监控**: 查看运行状态和统计

### 环境变量说明

| 变量名              | 说明                      | 是否必需 |
| ------------------- | ------------------------- | -------- |
| `DASHSCOPE_API_KEY` | 阿里云 DashScope API 密钥 | ✅ 必需  |
| `JWT_SECRET`        | JWT 签名密钥              | ✅ 必需  |
| `GITLAB_TOKEN`      | GitLab 访问令牌           | 可选     |
| `GITHUB_TOKEN`      | GitHub 访问令牌           | 可选     |
| `ADMIN_USERNAME`    | 管理员用户名              | 可选     |
| `ADMIN_PASSWORD`    | 管理员密码                | 可选     |

## 🔍 审查类型说明

- **🎯 基础审查 (BASIC)**: 代码质量和规范检查
- **🛡️ 安全检查 (SECURITY)**: 安全漏洞和风险识别
- **⚡ 性能优化 (PERFORMANCE)**: 性能瓶颈和优化建议
- **📋 综合审查 (COMPREHENSIVE)**: 全方位代码分析

## 🔒 安全配置

### 生产环境部署

1. **修改默认密码**:

   ```bash
   ADMIN_PASSWORD=your_secure_password
   ```

2. **配置强 JWT 密钥**:

   ```bash
   JWT_SECRET=$(openssl rand -base64 32)
   ```

3. **启用 HTTPS**:

   - 配置 SSL 证书
   - 使用反向代理（Nginx/Apache）

4. **数据库配置**:
   ```bash
   DATABASE_URL=jdbc:mysql://localhost:3306/astralint
   DATABASE_USERNAME=astralint_user
   DATABASE_PASSWORD=secure_database_password
   ```

## 🎮 常用命令

```bash
# 启动开发环境
mvn spring-boot:run

# 运行测试
mvn test

# 打包应用
mvn clean package

# 使用生产配置启动
java -jar target/astralint-1.0.jar --spring.profiles.active=prod
```

## 🆘 常见问题

### Q: 启动时报错 "API 密钥无效"

**A**: 检查 `.env` 文件中的 `DASHSCOPE_API_KEY` 是否正确配置。

### Q: Webhook 没有触发

**A**:

1. 检查 Webhook URL 是否可访问
2. 验证 Secret 配置是否正确
3. 查看应用日志获取详细错误信息

### Q: 无法访问管理后台

**A**:

1. 确认用户名密码正确
2. 检查 JWT 配置
3. 清除浏览器缓存

### Q: 代码审查响应慢

**A**:

1. 检查网络连接
2. 减少代码变更大小
3. 调整 AI 模型超时配置

## 📚 更多资源

- 📖 [完整文档](README.md)
- 🔒 [安全报告](SECURITY_FIXES.md)
- 🐛 [问题报告](../../issues)
- 💬 [社区讨论](../../discussions)

## 🎉 恭喜！

你已经成功设置了 AstraLint！现在可以开始享受 AI 驱动的代码审查体验了。

如果遇到任何问题，请查看 [FAQ](../../wiki/FAQ) 或提交 [Issue](../../issues)。

---

**提示**: 第一次使用建议从简单的代码变更开始，熟悉系统后再处理复杂的审查任务。
