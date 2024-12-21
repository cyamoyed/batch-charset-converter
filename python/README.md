# 安装 PyInstaller
在 Python 环境中安装 PyInstaller：

```bash
pip install pyinstaller
```

# 打包 Python 脚本
假设你的 Python 脚本名为 detect_charset.py，可以使用以下命令将其打包为可执行文件：

*Windows*
```bash
pyinstaller --onefile detect_charset.py
```

*Linux/macOS*
```bash
pyinstaller --onefile --clean detect_charset.py
```

*打包后的文件位置*
打包完成后，生成的可执行文件会位于 dist 目录下。例如：

+ Windows：dist\detect_charset.exe
+ Linux/macOS：dist/detect_charset

## 测试可执行文件
确保打包后的可执行文件可以独立运行。例如：

```bash
./dist/detect_charset path/to/your/file.txt
```

输出示例：

UTF-8