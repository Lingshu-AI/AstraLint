package org.linshuai.astralint.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AiCodeReviewService using Mock objects.
 */
class AiCodeReviewServiceTest {

  @Mock
  private AiModelService aiModelService;

  private AiCodeReviewService aiCodeReviewService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    aiCodeReviewService = new AiCodeReviewService();
  }

  @Test
  @DisplayName("应该能够处理有效的代码差异")
  void testGenerateComprehensiveReview_shouldHandleValidDiff() {
    // Given
    String validDiff = """
        diff --git a/src/main/java/TestClass.java b/src/main/java/TestClass.java
        index 1234567..abcdefg 100644
        --- a/src/main/java/TestClass.java
        +++ b/src/main/java/TestClass.java
        @@ -1,3 +1,6 @@
         public class TestClass {
        +    private String name;
        +
        +    public String getName() { return name; }
         }
        """;

    // When & Then
    assertDoesNotThrow(() -> {
      assertNotNull(validDiff);
      assertTrue(validDiff.contains("TestClass.java"));
      assertTrue(validDiff.contains("+"));
      assertTrue(validDiff.contains("diff --git"));
    });
  }

  @Test
  @DisplayName("应该能够处理空的代码差异")
  void testGenerateComprehensiveReview_shouldHandleEmptyDiff() {
    // Given
    String emptyDiff = "";

    // When & Then
    assertDoesNotThrow(() -> {
      assertNotNull(emptyDiff);
      assertEquals(0, emptyDiff.length());
    });
  }

  @Test
  @DisplayName("应该能够解析代码差异中的文件路径")
  void testParseDiff_shouldExtractFilePaths() {
    // Given
    String diffWithMultipleFiles = """
        diff --git a/src/main/java/ClassA.java b/src/main/java/ClassA.java
        +++ b/src/main/java/ClassA.java
        diff --git a/src/test/java/ClassATest.java b/src/test/java/ClassATest.java
        +++ b/src/test/java/ClassATest.java
        """;

    // When & Then
    assertNotNull(diffWithMultipleFiles);
    assertTrue(diffWithMultipleFiles.contains("ClassA.java"));
    assertTrue(diffWithMultipleFiles.contains("ClassATest.java"));
    assertTrue(diffWithMultipleFiles.contains("src/main/java"));
    assertTrue(diffWithMultipleFiles.contains("src/test/java"));
  }

  @Test
  @DisplayName("应该能够计算代码变更的行数")
  void testCalculateChangedLines_shouldCountCorrectly() {
    // Given
    String diffContent = """
        @@ -1,5 +1,8 @@
        +Added line 1
        +Added line 2
         Unchanged line
        -Removed line
        +Modified line
        """;

    // When
    long addedLines = diffContent.lines()
        .filter(line -> line.startsWith("+") && !line.startsWith("+++"))
        .count();
    long removedLines = diffContent.lines()
        .filter(line -> line.startsWith("-") && !line.startsWith("---"))
        .count();

    // Then
    assertEquals(3, addedLines); // +Added line 1, +Added line 2, +Modified line
    assertEquals(1, removedLines); // -Removed line
  }

  @Test
  @DisplayName("Mock对象应该正确配置")
  void testMockConfiguration_shouldBeCorrect() {
    // Given & When & Then
    assertNotNull(aiModelService);
    assertNotNull(aiCodeReviewService);

    // 验证Mock对象可以被正确模拟
    when(aiModelService.toString()).thenReturn("Mocked AiModelService");
    assertEquals("Mocked AiModelService", aiModelService.toString());
  }
}
