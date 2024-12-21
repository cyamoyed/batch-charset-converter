package xyz.igali;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class CharsetConverter {

    private static String pythonScriptPath = "";
    private final ResourceBundle messages;

    static {
        // 获取资源文件的URL
        URL resourceUrl = BatchCharsetConverter.class.getClassLoader().getResource("detect_charset");
        if (resourceUrl == null) {
            System.err.println("Resource not found!");
            throw new RuntimeException("Resource not found!");
        }

        // 将资源文件复制到临时目录
        File tempFile = null;
        try {
            tempFile = File.createTempFile("detect_charset", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tempFile.deleteOnExit();

        try {
            Files.copy(resourceUrl.openStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 设置文件为可执行
        if (!tempFile.setExecutable(true)) {
            System.err.println("Failed to set the file as executable!");
            throw new RuntimeException("Failed to set the file as executable!");
        }

        pythonScriptPath = tempFile.getAbsolutePath();

    }

    public CharsetConverter() {
        Locale locale = Locale.getDefault();
        messages = ResourceBundle.getBundle("MessagesBundle", locale, new UTF8Control());
    }

    /**
     * 转换文件夹中的所有文件
     *
     * @param folderPath        文件夹路径
     * @param originalCharset   原始字符集
     * @param targetCharset     目标字符集
     * @param autoDetect        是否自动检测字符集
     * @param useOriginalCharset 如果自动检测与原始字符集不同，是否使用原始字符集
     * @param fileExtensions    文件后缀名集合
     */
    public void convertFiles(String folderPath, Charset originalCharset, Charset targetCharset, boolean autoDetect, boolean useOriginalCharset, Set<String> fileExtensions, Callback callback) {
        try {
            Path folder = Paths.get(folderPath);
            Files.walk(folder)
                .filter(Files::isRegularFile) // 只处理文件，不处理文件夹
                .filter(file -> fileExtensions.isEmpty() || fileExtensions.contains(getFileExtension(file))) // 只处理指定后缀名的文件
                .forEach(file -> convertCharset(file, originalCharset, targetCharset, autoDetect, useOriginalCharset, callback));
        } catch (IOException e) {
            throw new RuntimeException("Error while converting files: " + e.getMessage(), e);
        }
    }

    /**
     * 获取文件的扩展名
     */
    private String getFileExtension(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? "" : fileName.substring(dotIndex);
    }

    /**
     * 转换单个文件的字符集
     */
    private void convertCharset(Path file, Charset originalCharset, Charset targetCharset, boolean autoDetect, boolean useOriginalCharset, Callback callback) {
        try {
            String detectedCharset = null;

            // 如果启用了自动检测字符集
            if (autoDetect) {
                detectedCharset = detectCharsetWithPython(file);
                if (detectedCharset == null) {
                    throw new RuntimeException("Failed to detect charset for file: " + file);
                }
            }

            // 如果检测到的字符集与用户设定的原始字符集不同，且选择了使用原始字符集
            Charset charsetToUse = autoDetect && useOriginalCharset && !detectedCharset.equalsIgnoreCase(originalCharset.name())
                    ? originalCharset
                    : Charset.forName(detectedCharset != null ? detectedCharset : originalCharset.name());

            // 读取文件内容，使用指定的字符集
            String content = new String(Files.readAllBytes(file), charsetToUse);

            // 将内容写回文件，使用目标字符集
            Files.write(file, content.getBytes(targetCharset));
            callback.onCallback(messages.getString("log.converted") + file + " (Original Charset: " + charsetToUse + ", Target Charset: " + targetCharset + ")");
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert file: " + file + " - " + e.getMessage(), e);
        }
    }

    /**
     * 使用 Python 脚本检测文件的字符集
     */
    private String detectCharsetWithPython(Path file) {
        try {
            // 构建 Python 命令
            String command = pythonScriptPath + " " + file.toAbsolutePath();

            // 执行命令
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // 读取输出
            String charset = reader.readLine();
            int exitCode = process.waitFor();

            if (exitCode == 0 && charset != null) {
                return charset;
            } else {
                throw new RuntimeException("Python script failed for file: " + file);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}