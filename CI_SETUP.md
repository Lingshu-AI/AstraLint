# 🚀 CI/CD Setup Guide

本文档描述了为 AstraLint 项目设置的 GitHub Actions CI/CD 流程。

## 📋 概述

基于参考项目 `lang-memory` 的配置，我们为 AstraLint 项目添加了完整的 CI/CD 管道，包括：

### 🛠️ CI 流程

1. **构建和测试** (`build-and-test.yml`)

   - Java 17 环境设置
   - Maven 依赖缓存
   - 单元测试执行
   - 应用程序构建
   - Docker 镜像构建

2. **代码质量检查** (`code-quality.yml`)

   - **Checkstyle**: 代码风格检查（使用 Google Java Style）

3. **安全检查** (`security.yml`)
   - **CodeQL**: GitHub 的语义代码分析
   - 定期安全扫描（每周一次）

## 🔧 配置文件

### GitHub Actions 工作流

```
.github/
├── workflows/
│   ├── build-and-test.yml     # 构建和测试
│   ├── code-quality.yml       # 代码质量检查
│   └── security.yml           # 安全检查
├── dependabot.yml             # 依赖更新自动化
├── PULL_REQUEST_TEMPLATE.md   # PR模板
└── ISSUE_TEMPLATE/            # Issue模板
    ├── bug_report.yml
    └── feature_request.yml
```

### 工具配置

```
tools/
└── github-actions/
    └── setup-deps/
        └── action.yml          # 环境依赖设置
```

### Maven 配置

`pom.xml` 已更新，包含以下插件：

- `maven-checkstyle-plugin`: 代码风格检查
- `jacoco-maven-plugin`: 代码覆盖率

## 📝 本地开发

### 快速开始

```bash
# 查看所有可用命令
make help

# 完整构建流程
make build

# 运行代码质量检查
make quality



# 构建Docker镜像
make docker-build
```

### 主要 Makefile 目标

| 命令                  | 描述             |
| --------------------- | ---------------- |
| `make clean`          | 清理构建产物     |
| `make compile`        | 编译项目         |
| `make test`           | 运行测试         |
| `make package`        | 打包应用程序     |
| `make build`          | 完整构建流程     |
| `make quality`        | 代码质量检查     |
| `make security-check` | 安全扫描         |
| `make docker-build`   | 构建 Docker 镜像 |
| `make ci-full`        | 完整 CI 流程     |

## 🔒 安全性

### 依赖管理

- **Dependabot**: 自动创建依赖更新 PR
- **OWASP Dependency Check**: 扫描已知漏洞
- **CodeQL**: 语义代码分析

### 配置文件

- `owasp-dependency-check-suppressions.xml`: OWASP 扫描抑制配置

## 🚀 部署

### 环境配置

项目支持两个部署环境：

1. **Staging**:

   - 触发：推送到 `main` 分支
   - 镜像标签：`staging`

2. **Production**:
   - 触发：推送版本标签 (如 `v1.0.0`)
   - 镜像标签：版本号 + `latest`

### 手动部署

可以通过 GitHub Actions 界面手动触发部署：

1. 访问项目的 Actions 页面
2. 选择"🚀 Deploy"工作流
3. 点击"Run workflow"
4. 选择目标环境

## 📊 报告和监控

CI/CD 流程会生成以下报告：

- 测试结果
- 代码覆盖率报告
- 代码质量分析报告
- 安全扫描报告

所有报告都会作为 GitHub Actions 的 artifacts 上传。

## 🤝 贡献

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

CI 流程会自动运行所有检查，确保代码质量和安全性。

## 📚 参考

本 CI/CD 配置基于以下最佳实践：

- Spring Boot 应用的 CI/CD 模式
- GitHub Actions 官方文档
- OWASP 安全指南
- Java 项目的代码质量标准

---

_此配置参考了 `lang-memory` 项目的 CI/CD 设置，并针对 AstraLint 项目进行了优化。_
