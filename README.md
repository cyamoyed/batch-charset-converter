# Batch Charset Converter

## 简介

`Batch Charset Converter` 是一个用于批量转换文件夹下所有文件字符集的 Java 程序。它支持以下功能：

- 批量转换文件夹下所有文件的字符集。
- 自动检测文件的字符集（可选）。
- 支持自定义文件后缀名过滤。
- 提供图形用户界面（GUI），方便用户操作。

---

## 功能特性

1. **批量转换**：
    - 支持批量转换文件夹下所有文件的字符集。

2. **自动检测字符集**：
    - 支持自动检测文件的字符集（使用 Python 脚本）。

3. **自定义后缀名**：
    - 支持用户输入以逗号分隔的文件后缀名（如 `.txt,.log`），只处理指定后缀名的文件。

4. **国际化支持**：
    - 支持多语言界面（英文和中文）。

5. **图形用户界面**：
    - 提供友好的图形界面，方便用户操作。

---

## 系统要求

- **操作系统**：Windows、macOS、Linux

---

## 安装与运行

### 1. 下载程序

- 下载程序的 JAR 文件或打包后的可执行文件（如 `.exe` 或 `.dmg`）。

### 2. 运行程序

#### 方法一：运行 JAR 文件

1. 确保已安装 JDK 11 或更高版本。
2. 打开终端，运行以下命令：

   ```bash
   java -jar BatchCharsetConverter.jar
   ```

#### 方法二：运行打包后的可执行文件

- 双击生成的 `.exe` 或 `.dmg` 文件，程序将自动运行。

---

## 使用说明

### 1. 选择文件夹

- 点击 `选择文件夹` 按钮，选择需要转换字符集的文件夹。

### 2. 设置字符集

- 在 `原始字符集` 下拉菜单中选择原始字符集（如不确定要转换文件的字符集，可以选择 `自动检测`）。
- 在 `目标字符集` 下拉菜单中选择目标字符集。

### 3. 自定义后缀名

- 在 `文件扩展名` 文本框中输入以逗号分隔的文件后缀名（如 `.txt,.log`）。

### 4. 启用自动检测字符集

- 勾选 `自动检测` 复选框，启用自动检测字符集功能。
- 如果检测到的字符集与设定的原始字符集不同，可以选择是否使用原始字符集（勾选 `如果自动检测与原始字符集不同，使用原始字符集`）。

### 5. 开始转换

- 点击 `开始转换` 按钮，程序将开始转换文件夹下的所有文件。

### 6. 查看日志

- 转换过程中的日志会实时显示在窗口下方的日志区域。

---

## 打包为可执行文件

### 1. 编译为 JAR 文件

1. 使用 Maven 编译：
    - 打开终端，运行以下命令：

      ```bash
      mvn clean package
        ```
    - 在 `target` 目录下生成 JAR 文件。

2. 测试生成的 JAR 文件。

   ```bash
    java -jar target/batchCharsetConverter-1.0-SNAPSHOT.jar
   ```

### 2. 打包为 `.exe`（适用于 Windows）

1. 使用 `Launch4j` 工具：
    - 下载并安装 [Launch4j](http://launch4j.sourceforge.net/)。
    - 配置 `Launch4j`，指定 JAR 文件和输出路径。
    - 生成 `.exe` 文件。

2. 测试生成的 `.exe` 文件。

### 3. 打包为 `.dmg`（适用于 macOS）

1. 使用 `jpackage` 工具：
    - 确保已安装 JDK 14 或更高版本。
    - 运行以下命令：

      ```bash
      jpackage --name 字符集转换 --input target/ \
               --main-jar batchCharsetConverter-1.0-SNAPSHOT.jar \
               --main-class xyz.igali.BatchCharsetConverter \
               --type dmg \
               --runtime-image custom-jre \
               --icon icon.icns \
               --app-version 1.0
      ```

2. 测试生成的 `.dmg` 文件。

---

## 常见问题解答

### 1. 程序启动时文字乱码

- 确保资源文件（`MessagesBundle.properties` 和 `MessagesBundle_zh_CN.properties`）的编码为 UTF-8。
- 如果仍然乱码，请检查 Java 运行环境的编码设置。

### 2. 自动检测字符集失败

- 确保 Python 脚本路径正确。
- 确保 Python 环境已正确安装，并且可以通过命令行调用 `python`。

### 3. 转换过程中出现错误

- 检查日志区域中的错误信息。
- 确保文件夹路径和文件后缀名设置正确。

---

## 许可证

本程序采用 MIT 许可证，详情请参阅 [LICENSE](LICENSE) 文件。

---

## 联系我们

如有任何问题或建议，请联系：

- 邮箱：your-email@example.com
- GitHub：[你的 GitHub 仓库地址](https://github.com/your-username/your-repo)

---

### 示例截图

#### 初始界面（英文）

![初始界面](https://i.imgur.com/example1.png)

#### 初始界面（中文）

![初始界面](https://i.imgur.com/example2.png)



