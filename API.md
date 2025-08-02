# CodeVoyant API 文档

## 概述

CodeVoyant 提供RESTful API接口，支持代码审查、AI模型管理和仓库配置管理等功能。

## 基础信息

- **基础URL**: `http://localhost:8080`
- **内容类型**: `application/json`
- **字符编码**: `UTF-8`

## 认证

目前API使用简单的token认证，需要在请求头中包含有效的访问令牌。

## 代码审查API

### 1. 提交代码审查请求

**POST** `/api/code-review/submit`

提交异步代码审查请求。

**请求体**:
```json
{
  "project_id": "123",
  "merge_request_id": "456",
  "diff_content": "代码差异内容",
  "review_type": "COMPREHENSIVE",
  "priority": "MEDIUM"
}
```

**响应**:
```json
{
  "review_id": "uuid-string",
  "project_id": "123",
  "merge_request_id": "456",
  "status": "PROCESSING",
  "created_at": "2024-01-01T12:00:00"
}
```

### 2. 快速代码审查

**POST** `/api/code-review/quick`

同步快速代码审查。

**请求体**:
```json
{
  "project_id": "123",
  "diff_content": "代码差异内容"
}
```

**响应**: 直接返回审查结果文本

### 3. 安全检查

**POST** `/api/code-review/security`

专门进行代码安全检查。

**请求体**:
```json
{
  "project_id": "123",
  "diff_content": "代码差异内容"
}
```

**响应**: 返回安全检查报告

### 4. 性能优化建议

**POST** `/api/code-review/performance`

提供代码性能优化建议。

**请求体**:
```json
{
  "project_id": "123",
  "diff_content": "代码差异内容"
}
```

**响应**: 返回性能优化建议

### 5. 生成代码摘要

**POST** `/api/code-review/summary`

生成代码变更摘要。

**请求体**:
```json
{
  "project_id": "123",
  "diff_content": "代码差异内容"
}
```

**响应**: 返回代码变更摘要

### 6. 获取审查结果

**GET** `/api/code-review/{reviewId}`

获取异步审查的结果。

**响应**:
```json
{
  "review_id": "uuid-string",
  "project_id": "123",
  "merge_request_id": "456",
  "status": "COMPLETED",
  "summary": "代码审查摘要",
  "detailed_analysis": "详细分析结果",
  "processing_time_ms": 5000,
  "created_at": "2024-01-01T12:00:00"
}
```

### 7. 健康检查

**GET** `/api/code-review/health`

检查代码审查服务状态。

**响应**:
```json
{
  "status": "UP",
  "timestamp": 1704067200000
}
```

## 后台管理API

### AI模型管理

#### 1. 获取所有AI模型

**GET** `/api/admin/ai-models`

**响应**:
```json
[
  {
    "id": 1,
    "modelName": "qwen-plus",
    "modelProvider": "ALIBABA",
    "isActive": true,
    "isDefault": true,
    "createdAt": "2024-01-01T12:00:00"
  }
]
```

#### 2. 创建AI模型

**POST** `/api/admin/ai-models`

**请求体**:
```json
{
  "modelName": "gpt-4",
  "modelProvider": "OPENAI",
  "apiKey": "your-api-key",
  "temperature": 0.7,
  "maxTokens": 4000,
  "isActive": true,
  "isDefault": false
}
```

#### 3. 更新AI模型

**PUT** `/api/admin/ai-models/{id}`

#### 4. 删除AI模型

**DELETE** `/api/admin/ai-models/{id}`

#### 5. 设置默认模型

**POST** `/api/admin/ai-models/{id}/set-default`

#### 6. 切换模型状态

**POST** `/api/admin/ai-models/{id}/toggle-status`

### 仓库管理

#### 1. 获取所有仓库

**GET** `/api/admin/repositories`

**响应**:
```json
[
  {
    "id": 1,
    "repositoryName": "example-project",
    "repositoryType": "GITLAB",
    "isActive": true,
    "autoReviewEnabled": true,
    "createdAt": "2024-01-01T12:00:00"
  }
]
```

#### 2. 创建仓库

**POST** `/api/admin/repositories`

**请求体**:
```json
{
  "repositoryName": "example-project",
  "repositoryType": "GITLAB",
  "repositoryUrl": "https://gitlab.com/example/project",
  "projectId": "123",
  "accessToken": "your-access-token",
  "isActive": true,
  "autoReviewEnabled": true
}
```

#### 3. 更新仓库

**PUT** `/api/admin/repositories/{id}`

#### 4. 删除仓库

**DELETE** `/api/admin/repositories/{id}`

#### 5. 切换仓库状态

**POST** `/api/admin/repositories/{id}/toggle-status`

#### 6. 切换自动审查

**POST** `/api/admin/repositories/{id}/toggle-auto-review`

#### 7. 测试仓库连接

**POST** `/api/admin/repositories/{id}/test-connection`

**响应**:
```json
{
  "success": true,
  "message": "连接成功"
}
```

### 系统概览

#### 1. 获取系统概览

**GET** `/api/admin/dashboard`

**响应**:
```json
{
  "totalModels": 3,
  "activeModels": 2,
  "defaultModel": {
    "id": 1,
    "modelName": "qwen-plus",
    "modelProvider": "ALIBABA"
  },
  "totalRepositories": 2,
  "activeRepositories": 2,
  "autoReviewRepositories": 1
}
```

#### 2. 健康检查

**GET** `/api/admin/health`

**响应**:
```json
{
  "status": "UP",
  "timestamp": 1704067200000
}
```

## Webhook API

### GitLab Webhook

**POST** `/api/webhook/gitlab`

处理GitLab的Webhook事件。

**请求头**:
- `X-Gitlab-Event`: 事件类型
- `X-Gitlab-Token`: Webhook令牌

### GitHub Webhook

**POST** `/api/webhook/github`

处理GitHub的Webhook事件。

**请求头**:
- `X-GitHub-Event`: 事件类型
- `X-Hub-Signature-256`: Webhook签名

### Gitee Webhook

**POST** `/api/webhook/gitee`

处理Gitee的Webhook事件。

**请求头**:
- `X-Gitee-Event`: 事件类型
- `X-Gitee-Token`: Webhook令牌

## 错误处理

### 错误响应格式

```json
{
  "error": "错误描述",
  "message": "详细错误信息",
  "timestamp": "2024-01-01T12:00:00",
  "path": "/api/endpoint"
}
```

### 常见HTTP状态码

- `200 OK`: 请求成功
- `201 Created`: 资源创建成功
- `400 Bad Request`: 请求参数错误
- `404 Not Found`: 资源不存在
- `500 Internal Server Error`: 服务器内部错误

## 使用示例

### 使用curl进行快速代码审查

```bash
curl -X POST http://localhost:8080/api/code-review/quick \
  -H "Content-Type: application/json" \
  -d '{
    "project_id": "123",
    "diff_content": "diff --git a/Example.java b/Example.java\nindex 1234567..abcdefg 100644\n--- a/Example.java\n+++ b/Example.java\n@@ -1,3 +1,6 @@\n public class Example {\n+    public void newMethod() {\n+        System.out.println(\"Hello World\");\n+    }\n }"
  }'
```

### 使用curl创建AI模型

```bash
curl -X POST http://localhost:8080/api/admin/ai-models \
  -H "Content-Type: application/json" \
  -d '{
    "modelName": "gpt-4",
    "modelProvider": "OPENAI",
    "apiKey": "your-api-key",
    "temperature": 0.7,
    "maxTokens": 4000,
    "isActive": true,
    "isDefault": false
  }'
```

## 注意事项

1. 所有API请求都应该包含适当的Content-Type头
2. 敏感信息（如API密钥）在传输时应该加密
3. 生产环境中应该使用HTTPS
4. 建议实现适当的速率限制和访问控制
5. 定期备份配置数据 