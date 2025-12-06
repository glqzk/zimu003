# Android 扫雷游戏

一个功能完整的Android扫雷游戏应用，支持通过 GitHub Actions 自动构建。

## 功能特性

- ✅ 三种难度级别：简单（9x9，10雷）、中等（16x16，40雷）、困难（16x30，99雷）
- ✅ 点击格子揭示内容
- ✅ 长按格子标记/取消标记地雷
- ✅ 自动展开空白区域
- ✅ 计时器功能
- ✅ 剩余地雷数显示
- ✅ 游戏胜利/失败检测
- ✅ 重新开始游戏
- ✅ 难度切换

## 如何构建

### 使用 GitHub Actions（推荐）

1. 将代码推送到 GitHub 仓库
2. GitHub Actions 会自动构建 APK
3. 在 Actions 标签页下载构建好的 APK 文件

或者手动触发构建：
- 进入 GitHub 仓库的 Actions 标签页
- 选择 "Build APK" 工作流
- 点击 "Run workflow"

构建完成后，APK 文件可以在 Artifacts 中下载。

### 使用 Android Studio

1. 打开 Android Studio
2. 选择 "Open an Existing Project"
3. 选择本项目目录
4. 等待 Gradle 同步完成
5. 点击 "Run" 按钮或按 `Shift+F10` 构建并运行

### 使用命令行（Linux/Mac）

```bash
# 确保 gradlew 有执行权限
chmod +x gradlew

# 构建 Debug APK
./gradlew assembleDebug
```

### 使用命令行（Windows）

```powershell
# 构建 Debug APK
.\gradlew.bat assembleDebug
```

生成的 APK 文件位于：`app/build/outputs/apk/debug/app-debug.apk`

## 游戏说明

- **点击格子**：揭示该格子
- **长按格子**：标记/取消标记为地雷（显示🚩）
- **数字**：表示周围8个格子中地雷的数量
- **空白格子**：周围没有地雷，会自动展开
- **目标**：揭示所有非地雷格子即可获胜

## 技术栈

- **Kotlin** - 现代 Android 开发语言
- Android SDK
- GridLayout
- Material Design Components
- GitHub Actions (CI/CD)

## 最低要求

- Android 5.0 (API 21) 或更高版本
- Android Studio Arctic Fox 或更高版本（本地开发）

## 项目结构

```
.
├── .github/
│   └── workflows/
│       └── build.yml          # GitHub Actions 构建配置
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/minesweeper/
│   │       │   ├── MainActivity.java    # 主活动
│   │       │   ├── GameEngine.java      # 游戏引擎
│   │       │   └── Cell.java            # 格子类
│   │       ├── res/                      # 资源文件
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── gradle/
│   └── wrapper/                          # Gradle Wrapper
├── gradlew                               # Linux/Mac 构建脚本
├── gradlew.bat                           # Windows 构建脚本
└── build.gradle                          # 项目级构建配置
```

## 许可证

本项目采用 Apache 2.0 许可证。
