# GitHub Actions 构建说明

本项目使用 GitHub Actions 在 Linux 环境下自动构建 Android APK。

## 使用方法

### 自动构建

当代码推送到 `master` 或 `main` 分支时，GitHub Actions 会自动触发构建。

### 手动触发构建

1. 进入 GitHub 仓库页面
2. 点击 "Actions" 标签页
3. 选择 "Build APK" 工作流
4. 点击右侧的 "Run workflow" 按钮
5. 选择分支并点击 "Run workflow"

### 下载 APK

构建完成后：

1. 在 Actions 标签页找到对应的构建任务
2. 点击进入构建详情
3. 在页面底部的 "Artifacts" 部分下载 `minesweeper-apk`

## 构建配置

- **运行环境**: Ubuntu Latest
- **JDK 版本**: 17 (Temurin)
- **构建类型**: Debug APK
- **保留时间**: 30 天

## 注意事项

- 首次构建可能需要较长时间（下载依赖和 Android SDK）
- 构建产物会在 30 天后自动删除
- 如需构建 Release 版本，需要配置签名密钥

