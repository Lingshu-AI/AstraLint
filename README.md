# CodeVoyant - 智能代码审查系统

基于Spring AI Alibaba框架的智能代码审查系统，提供自动化的代码质量分析、安全检查、性能优化建议等功能。

## 🚀 功能特性

### 核心功能
- **🤖 智能代码审查**: 使用AI模型自动分析代码变更
- **🔒 安全检查**: 识别潜在的安全漏洞和风险
- **⚡ 性能优化**: 提供代码性能改进建议
- **📋 代码摘要**: 自动生成代码变更摘要
- **🔍 综合报告**: 生成详细的代码审查报告
- **⚙️ 后台管理**: 完整的配置管理界面

### 审查类型
- **基础审查 (BASIC)**: 代码质量和最佳实践检查
- **安全检查 (SECURITY)**: 安全漏洞和风险识别
- **性能优化 (PERFORMANCE)**: 性能瓶颈和改进建议
- **综合审查 (COMPREHENSIVE)**: 全面的代码审查分析

### 后台管理功能
- **AI模型管理**: 支持多AI模型配置和管理
- **仓库管理**: 支持GitLab、GitHub、Gitee等仓库配置
- **系统监控**: 实时系统状态和统计信息
- **配置管理**: 灵活的配置参数调整

## 🛠️ 技术栈

- **Spring Boot 3.3.1**: 主框架
- **Spring AI**: AI集成框架
- **Spring AI Alibaba DashScope**: 阿里云通义千问AI模型
- **Spring Data JPA**: 数据持久化
- **H2 Database**: 内存数据库（开发环境）
- **GitLab4J**: GitLab API集成
- **Jackson**: JSON处理
- **Maven**: 依赖管理
- **Bootstrap 5**: 前端UI框架

## 📋 系统要求

- Java 17+
- Maven 3.6+
- 阿里云DashScope API密钥

## 🚀 快速开始

### 1. 克隆项目
```bash
git clone <repository-url>
cd CodeVoyant
```

### 2. 配置环境
编辑 `src/main/resources/application.properties`:

```properties
# 阿里云DashScope配置
spring.ai.alibaba.dashscope.api-key=your_dashscope_api_key_here
spring.ai.alibaba.dashscope.chat.options.model=qwen-plus
spring.ai.alibaba.dashscope.chat.options.temperature=0.7
spring.ai.alibaba.dashscope.chat.options.max-tokens=4000

# GitLab配置
gitlab.url=http://localhost:8929
gitlab.token=your_access_token_here
gitlab.webhook.secret=your_webhook_secret_here

# AI代码审查配置
ai.code-review.enabled=true
ai.code-review.model=qwen-plus
ai.code-review.max-file-size=10000
ai.code-review.timeout=30000
```

### 3. 运行应用
```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动。

### 4. 访问后台管理
打开浏览器访问：`http://localhost:8080/admin/`

## 📡 API接口

### 代码审查API
- `POST /api/code-review/submit` - 提交代码审查请求
- `POST /api/code-review/quick` - 快速代码审查
- `POST /api/code-review/security` - 安全检查
- `POST /api/code-review/performance` - 性能优化建议
- `POST /api/code-review/summary` - 生成代码摘要
- `GET /api/code-review/{reviewId}` - 获取审查结果
- `GET /api/code-review/health` - 健康检查

### 后台管理API
- `GET /api/admin/dashboard` - 获取系统概览
- `GET /api/admin/ai-models` - 获取所有AI模型
- `POST /api/admin/ai-models` - 创建AI模型
- `PUT /api/admin/ai-models/{id}` - 更新AI模型
- `DELETE /api/admin/ai-models/{id}` - 删除AI模型
- `POST /api/admin/ai-models/{id}/set-default` - 设置默认模型
- `POST /api/admin/ai-models/{id}/toggle-status` - 切换模型状态
- `GET /api/admin/repositories` - 获取所有仓库
- `POST /api/admin/repositories` - 创建仓库
- `PUT /api/admin/repositories/{id}` - 更新仓库
- `DELETE /api/admin/repositories/{id}` - 删除仓库
- `POST /api/admin/repositories/{id}/toggle-status` - 切换仓库状态
- `POST /api/admin/repositories/{id}/toggle-auto-review` - 切换自动审查
- `POST /api/admin/repositories/{id}/test-connection` - 测试仓库连接

## 🔧 配置说明

### AI模型配置
- `spring.ai.alibaba.dashscope.api-key`: 阿里云DashScope API密钥
- `spring.ai.alibaba.dashscope.chat.options.model`: AI模型名称 (默认: qwen-plus)
- `spring.ai.alibaba.dashscope.chat.options.temperature`: 生成温度 (0.0-1.0)
- `spring.ai.alibaba.dashscope.chat.options.max-tokens`: 最大生成token数

### 代码审查配置
- `ai.code-review.enabled`: 是否启用AI代码审查
- `ai.code-review.model`: 使用的AI模型
- `ai.code-review.max-file-size`: 最大文件大小限制
- `ai.code-review.timeout`: AI请求超时时间

### 数据库配置
- `spring.datasource.url`: 数据库连接URL
- `spring.jpa.hibernate.ddl-auto`: 数据库表结构策略
- `spring.h2.console.enabled`: 启用H2控制台

## 🖥️ 后台管理界面

### 系统概览
- 显示AI模型和仓库统计信息
- 系统状态监控
- 快速操作入口

### AI模型管理
- 支持多AI提供商（阿里云、OpenAI、Anthropic等）
- 模型参数配置（温度、最大token数、超时时间等）
- 模型状态管理（激活/停用）
- 默认模型设置

### 仓库管理
- 支持多种仓库类型（GitLab、GitHub、Gitee）
- 仓库连接配置
- Webhook设置
- 自动审查开关
- 连接测试功能

## 📊 审查报告示例

### 综合审查报告
```
# 🔍 综合代码审查报告

## 📋 代码变更摘要

### src/main/java/Example.java
主要功能变更概述：添加了新的用户认证功能
影响范围评估：影响用户登录模块
测试建议：需要添加单元测试和集成测试

## 🔒 代码安全检查报告

### src/main/java/Example.java
🚨 发现SQL注入风险：直接拼接SQL语句
建议：使用参数化查询或ORM框架

## ⚡ 代码优化建议

### src/main/java/Example.java
- 建议使用StringBuilder替代String拼接
- 考虑添加缓存机制提高性能

## 🤖 智能代码审查结果

### 📁 文件: src/main/java/Example.java
- ✅ 代码结构清晰，命名规范
- ⚠️ 缺少异常处理
- 建议添加日志记录
```

## 🔄 集成GitLab

### Webhook配置
1. 在GitLab项目中配置Webhook
2. URL: `http://your-server:8080/api/webhook/gitlab`
3. 选择触发事件: Merge Request events
4. 设置Secret Token

### 自动审查流程
1. 开发者创建Merge Request
2. GitLab发送Webhook事件
3. CodeVoyant自动触发代码审查
4. AI分析代码并生成报告
5. 在Merge Request中添加审查评论

## 🛡️ 安全考虑

- API密钥存储在数据库中（生产环境建议加密）
- 支持CORS配置
- 请求超时和重试机制
- 文件大小限制防止资源耗尽
- 数据库访问控制

## 🚀 部署

### 开发环境
```bash
# 使用H2内存数据库
mvn spring-boot:run
```

### 生产环境
```bash
# 使用外部数据库（MySQL/PostgreSQL）
# 修改application.properties中的数据库配置
mvn clean package
java -jar target/CodeVoyant-1.0-SNAPSHOT.jar
```

### Docker部署
```bash
# 构建镜像
docker build -t codevoyant .

# 运行容器
docker run -p 8080:8080 \
  -e SPRING_AI_ALIBABA_DASHSCOPE_API_KEY=your_key \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/codevoyant \
  codevoyant
```

### 生产环境配置
- 使用HTTPS
- 配置负载均衡
- 设置监控和日志
- 数据库持久化审查结果
- 定期备份配置数据

## 📝 使用指南

### 1. 首次使用
1. 启动应用后访问 `http://localhost:8080/admin/`
2. 在AI模型管理中配置你的AI模型API密钥
3. 在仓库管理中添加你的代码仓库
4. 配置Webhook以启用自动审查

### 2. 手动代码审查
```bash
curl -X POST http://localhost:8080/api/code-review/quick \
  -H "Content-Type: application/json" \
  -d '{
    "project_id": "123",
    "diff_content": "代码差异内容"
  }'
```

### 3. 查看审查结果
访问后台管理界面查看历史审查记录和统计信息。

## 🤝 贡献

欢迎提交Issue和Pull Request！

## 📄 许可证

MIT License

## 📞 支持

如有问题，请提交Issue或联系开发团队。 