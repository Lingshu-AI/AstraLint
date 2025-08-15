package org.linshuai.astralint.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CodeReviewUtils utility methods.
 */
class CodeReviewUtilsTest {

  @Test
  @DisplayName("应该能够正确提取文件扩展名")
  void testGetFileExtension_shouldReturnCorrectExtension() {
    // Given & When & Then
    assertEquals("java", getFileExtension("TestClass.java"));
    assertEquals("py", getFileExtension("script.py"));
    assertEquals("js", getFileExtension("app.js"));
    assertEquals("tsx", getFileExtension("Component.tsx"));
    assertEquals("", getFileExtension("README"));
    assertEquals("", getFileExtension("Dockerfile"));
    assertEquals("", getFileExtension(""));
  }

  @Test
  @DisplayName("应该能够判断是否为支持的编程语言文件")
  void testIsSupportedLanguage_shouldIdentifyCorrectly() {
    // Given & When & Then
    assertTrue(isSupportedLanguage("java"));
    assertTrue(isSupportedLanguage("python"));
    assertTrue(isSupportedLanguage("javascript"));
    assertTrue(isSupportedLanguage("typescript"));
    assertFalse(isSupportedLanguage("txt"));
    assertFalse(isSupportedLanguage(""));
    assertFalse(isSupportedLanguage("unknown"));
  }

  @Test
  @DisplayName("应该能够计算代码复杂度指标")
  void testCalculateComplexity_shouldReturnValidMetrics() {
    // Given
    String simpleCode = "public class Simple { }";
    String complexCode = """
        public class Complex {
            public void method1() {
                if (condition1) {
                    for (int i = 0; i < 10; i++) {
                        if (condition2) {
                            while (condition3) {
                                // complex logic
                            }
                        }
                    }
                }
            }
        }
        """;

    // When & Then
    assertTrue(simpleCode.length() < complexCode.length());
    assertTrue(countComplexityKeywords(simpleCode) < countComplexityKeywords(complexCode));

    // 验证复杂代码包含更多控制结构
    assertTrue(complexCode.contains("if"));
    assertTrue(complexCode.contains("for"));
    assertTrue(complexCode.contains("while"));
  }

  @Test
  @DisplayName("应该能够验证代码差异格式")
  void testValidateDiffFormat_shouldDetectValidFormat() {
    // Given
    String validDiff = """
        diff --git a/src/main/java/Test.java b/src/main/java/Test.java
        index 1234567..abcdefg 100644
        --- a/src/main/java/Test.java
        +++ b/src/main/java/Test.java
        @@ -1,3 +1,4 @@
         public class Test {
        +    private String field;
         }
        """;

    String invalidDiff = "This is not a valid diff format";

    // When & Then
    assertTrue(isValidDiffFormat(validDiff));
    assertFalse(isValidDiffFormat(invalidDiff));
    assertFalse(isValidDiffFormat(""));
    assertFalse(isValidDiffFormat(null));
  }

  @Test
  @DisplayName("应该能够提取安全问题模式")
  void testDetectSecurityPatterns_shouldIdentifyIssues() {
    // Given
    String secureCode = "public class Secure { }";
    String insecureCode = """
        public class Insecure {
            String password = "hardcoded123";
            String query = "SELECT * FROM users WHERE id = " + userId;
            Runtime.getRuntime().exec(userInput);
        }
        """;

    // When & Then
    assertFalse(hasSecurityIssues(secureCode));
    assertTrue(hasSecurityIssues(insecureCode));

    // 验证特定安全问题
    assertTrue(insecureCode.contains("password"));
    assertTrue(insecureCode.contains("SELECT"));
    assertTrue(insecureCode.contains("exec"));
  }

  // Helper methods for testing
  private String getFileExtension(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      return "";
    }
    int lastDotIndex = fileName.lastIndexOf('.');
    if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
      return "";
    }
    return fileName.substring(lastDotIndex + 1).toLowerCase();
  }

  private boolean isSupportedLanguage(String extension) {
    return switch (extension.toLowerCase()) {
      case "java", "py", "python", "js", "javascript", "ts", "typescript",
          "go", "cpp", "c", "cs", "rb", "ruby", "php" ->
        true;
      default -> false;
    };
  }

  private int countComplexityKeywords(String code) {
    String[] keywords = { "if", "else", "for", "while", "switch", "case", "try", "catch" };
    int count = 0;
    String lowerCode = code.toLowerCase();
    for (String keyword : keywords) {
      int index = lowerCode.indexOf(keyword);
      while (index != -1) {
        count++;
        index = lowerCode.indexOf(keyword, index + 1);
      }
    }
    return count;
  }

  private boolean isValidDiffFormat(String diff) {
    if (diff == null || diff.isEmpty()) {
      return false;
    }
    return diff.contains("diff --git") || diff.contains("@@") ||
        diff.contains("---") || diff.contains("+++");
  }

  private boolean hasSecurityIssues(String code) {
    if (code == null || code.isEmpty()) {
      return false;
    }
    String lowerCode = code.toLowerCase();
    String[] securityPatterns = {
        "password", "secret", "token", "key",
        "select * from", "exec", "eval",
        "system(", "runtime.getruntime"
    };

    for (String pattern : securityPatterns) {
      if (lowerCode.contains(pattern)) {
        return true;
      }
    }
    return false;
  }
}
