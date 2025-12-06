# 上传到 GitHub 仓库的步骤

## 方法一：使用 Git 命令行（推荐）

### 1. 确保已安装 Git

如果还没有安装 Git，请从 https://git-scm.com/download/win 下载并安装。

### 2. 打开 PowerShell 或命令提示符

在项目目录 `c:\Users\Admin\Downloads\saolei` 中打开终端。

### 3. 执行以下命令

```powershell
# 初始化 Git 仓库（如果还没有初始化）
git init

# 添加远程仓库
git remote add origin https://github.com/glqzk/zimu003.git

# 添加所有文件
git add .

# 提交更改
git commit -m "Initial commit: Android Minesweeper game"

# 推送到 GitHub（首次推送）
git push -u origin main
```

如果遇到分支名称问题，可以尝试：

```powershell
# 如果默认分支是 master
git branch -M main
git push -u origin main
```

或者：

```powershell
# 如果仓库使用 master 分支
git push -u origin master
```

## 方法二：使用 GitHub Desktop

1. 下载并安装 GitHub Desktop：https://desktop.github.com/
2. 打开 GitHub Desktop
3. 点击 "File" -> "Add Local Repository"
4. 选择项目目录 `c:\Users\Admin\Downloads\saolei`
5. 点击 "Publish repository" 按钮
6. 选择仓库 `glqzk/zimu003`

## 方法三：使用 Android Studio 内置 Git

1. 在 Android Studio 中打开项目
2. 点击 "VCS" -> "Enable Version Control Integration"
3. 选择 "Git"
4. 点击 "VCS" -> "Git" -> "Remotes"
5. 添加远程仓库：`https://github.com/glqzk/zimu003.git`
6. 右键点击项目根目录 -> "Git" -> "Add"
7. 右键点击项目根目录 -> "Git" -> "Commit Directory"
8. 输入提交信息并提交
9. 右键点击项目根目录 -> "Git" -> "Push"

## 注意事项

- 首次推送可能需要输入 GitHub 用户名和密码（或 Personal Access Token）
- 如果仓库已存在文件，可能需要先拉取：`git pull origin main --allow-unrelated-histories`
- 确保 `.gitignore` 文件已正确配置，避免上传不必要的文件

