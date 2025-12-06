# 构建说明

## 前置要求

1. **Java JDK 8 或更高版本**
   - 下载地址：https://www.oracle.com/java/technologies/downloads/
   - 或使用 OpenJDK：https://adoptium.net/

2. **Android SDK**
   - 推荐使用 Android Studio，它会自动安装 Android SDK
   - 下载地址：https://developer.android.com/studio

## 使用 Android Studio 构建（推荐）

1. 打开 Android Studio
2. 选择 "File" -> "Open"
3. 选择本项目目录
4. 等待 Gradle 同步完成（首次可能需要下载依赖）
5. 连接 Android 设备或启动模拟器
6. 点击 "Run" 按钮（绿色三角形）或按 `Shift+F10`

## 使用命令行构建

### Windows

```powershell
# 确保已设置 ANDROID_HOME 环境变量
# 设置 ANDROID_HOME=C:\Users\YourName\AppData\Local\Android\Sdk

# 构建 Debug APK
.\gradlew.bat assembleDebug

# 构建 Release APK（需要签名配置）
.\gradlew.bat assembleRelease
```

### Linux/Mac

```bash
# 确保已设置 ANDROID_HOME 环境变量
# export ANDROID_HOME=$HOME/Android/Sdk

# 构建 Debug APK
./gradlew assembleDebug

# 构建 Release APK（需要签名配置）
./gradlew assembleRelease
```

## 生成的 APK 位置

构建完成后，APK 文件位于：
```
app/build/outputs/apk/debug/app-debug.apk
```

## 安装到设备

### 使用 ADB

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 手动安装

1. 将 APK 文件传输到 Android 设备
2. 在设备上启用"未知来源"安装权限
3. 点击 APK 文件进行安装

## 常见问题

### Gradle 同步失败

- 检查网络连接（需要下载依赖）
- 检查 Android SDK 是否正确安装
- 尝试清理项目：`./gradlew clean`

### 构建错误：找不到 SDK

- 确保已安装 Android SDK
- 设置 ANDROID_HOME 环境变量
- 在 `local.properties` 文件中添加：`sdk.dir=C:\\Users\\YourName\\AppData\\Local\\Android\\Sdk`

### Java 版本不兼容

- 确保使用 Java 8 或更高版本
- 检查 JAVA_HOME 环境变量

