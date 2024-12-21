package xyz.igali;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class BatchCharsetConverter extends JFrame {

    private final JTextField folderPathField;
    private final JComboBox<String> originalCharsetComboBox;
    private final JComboBox<String> targetCharsetComboBox;
    private final JTextArea logArea;
    private final JCheckBox autoDetectCheckBox;
    private final JCheckBox useOriginalCharsetCheckBox;
    private final JTextField fileExtensionsField;
    private final ResourceBundle messages;

    public BatchCharsetConverter() {
        // 设置国际化资源
        Locale locale = Locale.getDefault();
        messages = ResourceBundle.getBundle("MessagesBundle", locale, new UTF8Control());

        setTitle(messages.getString("window.title"));
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建 UI 组件
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 添加 padding

        folderPathField = new JTextField();
        JButton chooseFolderButton = new JButton(messages.getString("button.chooseFolder"));
        originalCharsetComboBox = new JComboBox<>(new String[]{"GBK", "UTF-8", "ISO-8859-1", "UTF-16"});
        targetCharsetComboBox = new JComboBox<>(new String[]{"UTF-8", "GBK", "ISO-8859-1", "UTF-16"});
        autoDetectCheckBox = new JCheckBox(messages.getString("checkbox.autoDetect"));
        autoDetectCheckBox.setSelected(true); // 默认选中
        useOriginalCharsetCheckBox = new JCheckBox(messages.getString("checkbox.useOriginalCharset"));
        fileExtensionsField = new JTextField(".txt,.log,.java,.js"); // 默认后缀名
        JButton startButton = new JButton(messages.getString("button.startConversion"));

        // 添加组件到输入面板
        inputPanel.add(new JLabel(messages.getString("label.folderPath")));
        inputPanel.add(folderPathField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(chooseFolderButton);
        inputPanel.add(new JLabel(messages.getString("label.originalCharset")));
        inputPanel.add(originalCharsetComboBox);
        inputPanel.add(new JLabel(messages.getString("label.targetCharset")));
        inputPanel.add(targetCharsetComboBox);
        inputPanel.add(new JLabel(messages.getString("label.autoDetect")));
        inputPanel.add(autoDetectCheckBox);
        inputPanel.add(new JLabel(""));
        inputPanel.add(useOriginalCharsetCheckBox);
        inputPanel.add(new JLabel(messages.getString("label.fileExtensions")));
        inputPanel.add(fileExtensionsField);

        // 日志区域
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 添加 padding

        // 添加组件到主窗口
        add(inputPanel, BorderLayout.NORTH);
        add(logScrollPane, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);

        // 选择文件夹按钮事件
        chooseFolderButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(BatchCharsetConverter.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                folderPathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        // 开始转换按钮事件
        startButton.addActionListener(e -> {
            String folderPath = folderPathField.getText();
            String originalCharset = (String) originalCharsetComboBox.getSelectedItem();
            String targetCharset = (String) targetCharsetComboBox.getSelectedItem();
            boolean autoDetect = autoDetectCheckBox.isSelected();
            boolean useOriginalCharset = useOriginalCharsetCheckBox.isSelected();
            String fileExtensionsInput = fileExtensionsField.getText();

            if (folderPath.isEmpty()) {
                JOptionPane.showMessageDialog(BatchCharsetConverter.this, messages.getString("message.selectFolder"));
                return;
            }
            if (originalCharset == null || originalCharset.isEmpty()) {
                JOptionPane.showMessageDialog(BatchCharsetConverter.this, messages.getString("message.selectOriginalCharset"));
                return;
            }
            if (targetCharset == null || targetCharset.isEmpty()) {
                JOptionPane.showMessageDialog(BatchCharsetConverter.this, messages.getString("message.selectTargetCharset"));
                return;
            }

            if (originalCharset.equals(targetCharset)) {
                JOptionPane.showMessageDialog(BatchCharsetConverter.this, messages.getString("message.charsetSame"));
                return;
            }

            logArea.setText(""); // 清空日志
            Set<String> fileExtensions = parseFileExtensions(fileExtensionsInput);

            // 调用核心转换类
            CharsetConverter converter = new CharsetConverter();
            try {
                converter.convertFiles(folderPath, Charset.forName(originalCharset), Charset.forName(targetCharset), autoDetect, useOriginalCharset, fileExtensions, this::log);
                log(messages.getString("log.conversionComplete"));
            } catch (RuntimeException ex) {
                log(messages.getString("log.error") + ex.getMessage());
            }
        });
    }

    private Set<String> parseFileExtensions(String fileExtensionsInput) {
        Set<String> extensions = new HashSet<>();
        if (fileExtensionsInput != null && !fileExtensionsInput.trim().isEmpty()) {
            String[] parts = fileExtensionsInput.split(",");
            for (String part : parts) {
                extensions.add(part.trim().toLowerCase());
            }
        }
        return extensions;
    }

    private void log(String message) {
        System.out.println(message);
        // 使用 SwingUtilities.invokeLater 确保日志更新在 UI 线程中执行
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength()); // 滚动到日志末尾
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BatchCharsetConverter().setVisible(true));
    }
}