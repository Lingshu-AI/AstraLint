# 🌟 AstraLint

<div align="center">

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Security](https://img.shields.io/badge/Security-A%20Grade-success.svg)](SECURITY_FIXES.md)
[![Build](https://github.com/aias00/AstraLint/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/aias00/AstraLint/actions/workflows/build-and-test.yml)
[![Quality](https://github.com/aias00/AstraLint/actions/workflows/code-quality.yml/badge.svg)](https://github.com/aias00/AstraLint/actions/workflows/code-quality.yml)
[![Security](https://github.com/aias00/AstraLint/actions/workflows/security.yml/badge.svg)](https://github.com/aias00/AstraLint/actions/workflows/security.yml)

**AI 驱动的智能代码审查系统**

集成多平台 Webhook，提供专业的代码质量分析、安全检查和性能优化建议

[快速开始](#-快速开始) • [功能特性](#-功能特性) • [部署指南](#-部署指南) • [API 文档](#-api-文档) • [CI/CD](CI_SETUP.md) • [安全报告](SECURITY_FIXES.md)

</div>

---

## 🚀 功能特性

### 🤖 智能代码审查

- **多维度分析**: 代码质量、安全漏洞、性能优化、最佳实践
- **AI 驱动**: 基于阿里云通义千问，提供专业的审查意见
- **灵活配置**: 支持基础、安全、性能、综合等多种审查类型
- **实时反馈**: 异步处理，快速响应

### 🔗 多平台集成

- **GitLab**: 完整的 MR 审查流程
- **GitHub**: PR 自动审查
- **Gitee**: 国内代码托管平台支持
- **Webhook**: 自动触发，无缝集成

### 🛡️ 企业级安全

- **JWT 认证**: 无状态认证，支持角色权限
- **签名验证**: HMAC-SHA256 Webhook 验证
- **安全头**: CSP、HSTS、XSS 防护
- **输入验证**: 全面的参数校验和清理

### 📊 管理控制台

- **配置管理**: AI 模型、仓库配置
- **实时监控**: 系统状态、审查统计
- **用户友好**: 现代化 Web 界面

---

## 🏃 快速开始

### 📋 环境要求

- **Java**: 17 或更高版本
- **Maven**: 3.6+
- **内存**: 最小 512MB，推荐 1GB+
- **API 密钥**: 阿里云 DashScope API Key

### ⚡ 一键启动

```bash
# 1. 克隆项目
git clone https://github.com/yourusername/AstraLint.git
cd AstraLint

# 2. 配置环境变量
cp env.example .env
# 编辑 .env 文件，设置必要参数

# 3. 启动应用
mvn spring-boot:run
```

### 🌐 访问地址

- **主页**: http://localhost:8080
- **代码审查**: http://localhost:8080/secure-review.html
- **管理控制台**: http://localhost:8080/admin/ (需要登录)
- **健康检查**: http://localhost:8080/actuator/health

### 🔐 默认账户

```
用户名: admin
密码: admin123
```

> ⚠️ **安全提醒**: 生产环境请务必修改默认密码

---

## 📝 配置说明

### 🔧 必需配置

创建 `.env` 文件并设置以下环境变量：

```bash
# AI 服务配置 (必需)
DASHSCOPE_API_KEY=your_dashscope_api_key_here

# JWT 安全配置 (必需)
JWT_SECRET=your_jwt_secret_at_least_256_bits_long

# 数据库配置 (生产环境)
DATABASE_PASSWORD=your_secure_password
```

### 🎛️ 可选配置

```bash
# GitLab 集成
GITLAB_URL=http://localhost:8929
GITLAB_TOKEN=your_gitlab_token
GITLAB_WEBHOOK_SECRET=your_webhook_secret

# GitHub 集成
GITHUB_TOKEN=your_github_token
GITHUB_WEBHOOK_SECRET=your_github_secret

# Gitee 集成
GITEE_TOKEN=your_gitee_token
GITEE_WEBHOOK_SECRET=your_gitee_secret

# 管理员账户
ADMIN_USERNAME=admin
ADMIN_PASSWORD=your_admin_password
```

---

## 🔌 API 文档

### 🔐 认证接口

#### 登录获取 Token

```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "admin",
    "password": "admin123"
}
```

**响应**:

```json
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "roles": ["ADMIN", "USER"]
}
```

### 🔍 代码审查接口

#### 快速审查 (无需认证)

```http
POST /api/code-review/quick
Content-Type: application/json

{
    "projectId": "my-project",
    "mergeRequestId": "123",
    "diffContent": "diff --git a/src/main.js...",
    "reviewType": "COMPREHENSIVE"
}
```

#### 提交审查请求 (需要认证)

```http
POST /api/code-review/submit
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
    "projectId": "my-project",
    "mergeRequestId": "123",
    "diffContent": "git diff content...",
    "reviewType": "SECURITY",
    "language": "java"
}
```

#### 审查类型说明

- `BASIC`: 基础代码审查
- `SECURITY`: 安全漏洞检查
- `PERFORMANCE`: 性能优化建议
- `COMPREHENSIVE`: 综合全面审查

### 🪝 Webhook 接口

#### GitLab Webhook

```http
POST /api/webhook/gitlab
X-Gitlab-Event: Merge Request Hook
X-Gitlab-Token: your_webhook_secret
Content-Type: application/json

{
    "object_kind": "merge_request",
    "object_attributes": {
        "action": "opened",
        "iid": 123
    },
    "project": {
        "id": 456
    }
}
```

#### GitHub Webhook

```http
POST /api/webhook/github
X-GitHub-Event: pull_request
X-Hub-Signature-256: sha256=<signature>
Content-Type: application/json

{
    "action": "opened",
    "pull_request": {
        "number": 123
    },
    "repository": {
        "full_name": "owner/repo"
    }
}
```

---

## 🚀 部署指南

### 🐳 Docker 部署

```bash
# 构建镜像
docker build -t astralint:latest .

# 运行容器
docker run -d \
  --name astralint \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DASHSCOPE_API_KEY=your_api_key \
  -e JWT_SECRET=your_jwt_secret \
  -e DATABASE_PASSWORD=your_db_password \
  astralint:latest
```

### 🎯 生产环境部署

#### 1. 环境准备

```bash
# 创建生产配置
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:mysql://localhost:3306/astralint
export DATABASE_USERNAME=astralint_user
export DATABASE_PASSWORD=secure_password
```

#### 2. 反向代理配置 (Nginx)

```nginx
server {
    listen 80;
    server_name yourdomain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com;

    ssl_certificate /path/to/certificate.crt;
    ssl_certificate_key /path/to/private.key;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

#### 3. 系统服务配置

```ini
# /etc/systemd/system/astralint.service
[Unit]
Description=AstraLint Code Review Service
After=network.target

[Service]
Type=simple
User=astralint
WorkingDirectory=/opt/astralint
ExecStart=/usr/bin/java -jar astralint.jar
EnvironmentFile=/opt/astralint/.env
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

---

## 🔄 CI/CD 集成

AstraLint 采用现代化的 CI/CD 流程，确保代码质量和部署可靠性：

### 🚀 自动化流程

- **构建测试**: 自动编译、单元测试、集成测试
- **代码质量**: Checkstyle 静态分析
- **安全扫描**: CodeQL 语义分析

### 📊 快速命令

```bash
# 查看所有可用命令
make help

# 完整构建流程
make build

# 代码质量检查
make quality

# 安全扫描
make security-check

# Docker 构建
make docker-build

# 完整 CI 流程
make ci-full
```

### 🔗 状态徽章

- [![Build](https://github.com/aias00/AstraLint/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/aias00/AstraLint/actions/workflows/build-and-test.yml) 构建状态
- [![Quality](https://github.com/aias00/AstraLint/actions/workflows/code-quality.yml/badge.svg)](https://github.com/aias00/AstraLint/actions/workflows/code-quality.yml) 代码质量
- [![Security](https://github.com/aias00/AstraLint/actions/workflows/security.yml/badge.svg)](https://github.com/aias00/AstraLint/actions/workflows/security.yml) 安全检查

> 详细的 CI/CD 配置和使用指南请参阅 [CI_SETUP.md](CI_SETUP.md)

---

## 🛠️ 开发指南

### 📁 项目结构

```
AstraLint/
├── src/main/java/org/linshuai/astralint/
│   ├── controller/          # REST 控制器
│   │   ├── AuthController.java
│   │   ├── CodeReviewController.java
│   │   ├── AdminController.java
│   │   └── WebhookController.java
│   ├── service/             # 业务服务层
│   │   ├── AiCodeReviewService.java
│   │   ├── AiModelService.java
│   │   └── *Service.java
│   ├── config/              # 配置类
│   │   ├── SecurityConfig.java
│   │   ├── AiConfig.java
│   │   └── WebConfig.java
│   ├── entity/              # 实体类
│   ├── repository/          # 数据访问层
│   ├── dto/                 # 数据传输对象
│   └── util/                # 工具类
├── src/main/resources/
│   ├── application.yml      # 主配置文件
│   ├── application-dev.yml  # 开发环境配置
│   ├── application-prod.yml # 生产环境配置
│   └── static/              # 静态资源
└── SECURITY_FIXES.md        # 安全修复报告
```

### 🔧 本地开发

```bash
# 启动开发环境
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run

# 运行测试
mvn test

# 代码质量检查
mvn checkstyle:check
mvn spotbugs:check
```

### 🎯 添加新功能

1. **新增审查规则**

   ```java
   // 在 AiCodeReviewService 中添加
   public String customCodeReview(String code) {
       // 自定义审查逻辑
   }
   ```

2. **新增 API 端点**

   ```java
   // 在对应 Controller 中添加
   @PostMapping("/custom")
   public ResponseEntity<String> customEndpoint(@Valid @RequestBody Request request) {
       // 端点逻辑
   }
   ```

3. **新增配置选项**
   ```yaml
   # 在 application.yml 中添加
   custom:
     feature:
       enabled: ${CUSTOM_FEATURE_ENABLED:false}
   ```

---

## 🔒 安全特性

### 🛡️ 安全评分: A 级

| 安全方面     | 等级 | 说明                       |
| ------------ | ---- | -------------------------- |
| 认证授权     | A    | JWT + 角色权限控制         |
| 数据保护     | A    | 环境变量 + 加密传输        |
| 输入验证     | A    | Bean Validation + XSS 防护 |
| 网络安全     | A    | CORS 限制 + 安全头         |
| Webhook 安全 | A    | HMAC-SHA256 签名验证       |

### 🔐 安全最佳实践

1. **生产部署检查清单**

   - [ ] 修改默认管理员密码
   - [ ] 配置强 JWT 密钥 (256 位+)
   - [ ] 启用 HTTPS
   - [ ] 配置 Webhook 签名验证
   - [ ] 限制网络访问

2. **定期安全维护**
   - [ ] 更新依赖版本
   - [ ] 轮换 API 密钥
   - [ ] 审查访问日志
   - [ ] 备份重要数据

详细安全信息请查看 [安全修复报告](SECURITY_FIXES.md)

---

## 📊 监控和运维

### 📈 健康检查

```bash
# 基础健康检查
curl http://localhost:8080/actuator/health

# 详细健康信息 (需要认证)
curl -H "Authorization: Bearer <token>" \
     http://localhost:8080/actuator/health/details
```

### 📊 指标监控

```bash
# 应用指标
curl http://localhost:8080/actuator/metrics

# JVM 信息
curl http://localhost:8080/actuator/info
```

### 📝 日志管理

```yaml
# 生产环境日志配置
logging:
  level:
    org.linshuai.astralint: INFO
    org.springframework.security: WARN
  file:
    name: /var/log/astralint/application.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

---

## 🎯 使用场景

### 🏢 企业代码审查

```bash
# 团队代码审查工作流
1. 开发者提交 MR/PR
2. 自动触发 AI 审查
3. 生成详细审查报告
4. 团队评审和改进
```

### 🔒 安全代码扫描

```bash
# 安全漏洞检测
curl -X POST http://localhost:8080/api/code-review/security \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": "secure-app",
    "diffContent": "your code changes...",
    "reviewType": "SECURITY"
  }'
```

### ⚡ 性能优化建议

```bash
# 性能分析和优化
curl -X POST http://localhost:8080/api/code-review/performance \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": "high-perf-app",
    "diffContent": "performance critical code...",
    "reviewType": "PERFORMANCE"
  }'
```

---

## 🤝 贡献指南

### 🎯 如何贡献

1. **Fork** 本仓库
2. **创建**功能分支 (`git checkout -b feature/amazing-feature`)
3. **提交**更改 (`git commit -m 'Add amazing feature'`)
4. **推送**分支 (`git push origin feature/amazing-feature`)
5. **创建** Pull Request

### 📝 代码规范

- 遵循 Java 编码标准
- 编写单元测试 (覆盖率 > 80%)
- 更新相关文档
- 通过所有 CI 检查

### 🐛 问题报告

使用 [GitHub Issues](../../issues) 报告 Bug，请包含：

- 详细的问题描述
- 复现步骤
- 环境信息 (Java 版本、操作系统)
- 相关日志和截图

---

## 📚 技术栈

### 🎯 后端技术

- **Spring Boot 3.2.0**: 主应用框架
- **Spring Security**: 认证和授权
- **Spring AI**: AI 集成框架
- **Spring Data JPA**: 数据持久化
- **H2/MySQL**: 数据库支持
- **JWT**: 无状态认证
- **Maven**: 依赖管理

### 🎨 前端技术

- **HTML5**: 现代标记语言
- **CSS3**: 响应式样式
- **JavaScript ES6+**: 现代前端脚本
- **Font Awesome**: 图标库
- **Responsive Design**: 移动端适配

### 🤖 AI 服务

- **阿里云 DashScope**: 通义千问 AI 模型
- **灵活配置**: 支持多 AI 提供商
- **模型管理**: 动态切换和配置

---

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE) - 详见 LICENSE 文件

---

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot) - 强大的 Java 应用框架
- [Spring AI](https://spring.io/projects/spring-ai) - AI 集成框架
- [阿里云通义千问](https://dashscope.aliyun.com/) - AI 模型服务
- 所有贡献者和使用者

---

## 📞 支持与联系

- 📖 **文档**: [项目文档](../../wiki)
- 🐛 **问题**: [GitHub Issues](../../issues)
- 💬 **讨论**: [GitHub Discussions](../../discussions)
- 📧 **邮件**: support@astralint.com

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给个 Star！**

Made with ❤️ by the AstraLint Team

</div>
