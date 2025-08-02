package org.linshuai.astralint.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * 代码审查工具类
 */
public class CodeReviewUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(CodeReviewUtils.class);
    
    // 文件扩展名模式
    private static final Pattern CODE_FILE_PATTERN = Pattern.compile(
        "\\.(java|js|ts|py|c|cpp|cs|php|rb|go|rs|swift|kt|scala|clj|hs|ml|fs|vb|sql|sh|bat|ps1|yaml|yml|json|xml|html|css|scss|less|vue|jsx|tsx)$",
        Pattern.CASE_INSENSITIVE
    );
    
    // 忽略的文件模式
    private static final Pattern IGNORE_FILE_PATTERN = Pattern.compile(
        "(\\.git|node_modules|target|build|dist|out|bin|obj|vendor|coverage|logs|temp|tmp|\\..*)$",
        Pattern.CASE_INSENSITIVE
    );
    
    /**
     * 检查是否为代码文件
     */
    public static boolean isCodeFile(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        return CODE_FILE_PATTERN.matcher(fileName).find();
    }
    
    /**
     * 检查是否应该忽略的文件
     */
    public static boolean shouldIgnoreFile(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return true;
        }
        return IGNORE_FILE_PATTERN.matcher(fileName).find();
    }
    
    /**
     * 计算代码行数
     */
    public static int countCodeLines(String content) {
        if (content == null || content.trim().isEmpty()) {
            return 0;
        }
        
        String[] lines = content.split("\n");
        int count = 0;
        
        for (String line : lines) {
            String trimmedLine = line.trim();
            // 跳过空行和注释行
            if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("//") && !trimmedLine.startsWith("#")) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 提取文件扩展名
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        
        return "";
    }
    
    /**
     * 格式化文件大小
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
    
    /**
     * 检查文件大小是否超过限制
     */
    public static boolean isFileSizeExceeded(String content, int maxSize) {
        if (content == null) {
            return false;
        }
        return content.length() > maxSize;
    }
    
    /**
     * 清理代码内容（移除敏感信息）
     */
    public static String sanitizeCodeContent(String content) {
        if (content == null) {
            return "";
        }
        
        // 移除可能的API密钥、密码等敏感信息
        String sanitized = content
            .replaceAll("(?i)(api[_-]?key|password|secret|token)\\s*[:=]\\s*['\"][^'\"]*['\"]", "$1: ***")
            .replaceAll("(?i)(https?://[^\\s]+)", "***URL***");
        
        return sanitized;
    }
    
    /**
     * 生成文件摘要
     */
    public static String generateFileSummary(String fileName, String content) {
        if (content == null || content.trim().isEmpty()) {
            return "空文件";
        }
        
        int totalLines = content.split("\n").length;
        int codeLines = countCodeLines(content);
        String extension = getFileExtension(fileName);
        
        return String.format("文件: %s, 扩展名: %s, 总行数: %d, 代码行数: %d", 
            fileName, extension, totalLines, codeLines);
    }
} 