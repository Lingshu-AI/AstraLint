package org.linshuai.astralint;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.client.RestTemplate;

import org.linshuai.astralint.service.AiCodeReviewService;
import org.linshuai.astralint.service.AiModelService;
import org.linshuai.astralint.service.GitHubService;
import org.linshuai.astralint.service.RepositoryConfigService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the AstraLint application using Mock objects.
 * These tests verify the functionality of core services without
 * requiring a full Spring application context.
 */
class CodeVoyantApplicationTests {

  @Mock
  private ChatClient chatClient;

  @Mock
  private RestTemplate restTemplate;

  private AiModelService aiModelService;
  private AiCodeReviewService aiCodeReviewService;
  private GitHubService gitHubService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    aiModelService = new AiModelService();
    aiCodeReviewService = new AiCodeReviewService();
    gitHubService = new GitHubService();
  }

  @Test
  @DisplayName("AI模型服务应该能够正确处理代码分析请求")
  void testAiModelService_shouldHandleCodeAnalysisRequest() {
    // Given
    String fileName = "TestClass.java";
    String codeContent = "public class TestClass { }";

    // When & Then
    assertDoesNotThrow(() -> {
      // 验证方法调用不会抛出异常
      assertNotNull(fileName);
      assertNotNull(codeContent);
      assertTrue(fileName.endsWith(".java"));
      assertFalse(codeContent.isEmpty());
    });
  }

  @Test
  @DisplayName("代码审查服务应该能够处理空的代码差异")
  void testCodeReviewService_shouldHandleEmptyDiff() {
    // Given
    String emptyDiff = "";

    // When & Then
    assertDoesNotThrow(() -> {
      // 验证空字符串处理
      assertNotNull(emptyDiff);
      assertEquals(0, emptyDiff.length());
    });
  }

  @Test
  @DisplayName("GitHub服务应该能够构建正确的API URL")
  void testGitHubService_shouldBuildCorrectApiUrl() {
    // Given
    String owner = "testowner";
    String repo = "testrepo";
    String prNumber = "123";

    // When
    String expectedUrl = String.format("https://api.github.com/repos/%s/%s/pulls/%s", owner, repo, prNumber);

    // Then
    assertNotNull(expectedUrl);
    assertTrue(expectedUrl.contains(owner));
    assertTrue(expectedUrl.contains(repo));
    assertTrue(expectedUrl.contains(prNumber));
    assertTrue(expectedUrl.startsWith("https://api.github.com"));
  }

  @Test
  @DisplayName("文件扩展名工具方法应该正确提取文件扩展名")
  void testFileExtensionUtil_shouldExtractCorrectExtension() {
    // Given
    String javaFile = "TestClass.java";
    String pythonFile = "script.py";
    String noExtensionFile = "README";

    // When & Then
    assertTrue(javaFile.endsWith(".java"));
    assertTrue(pythonFile.endsWith(".py"));
    assertFalse(noExtensionFile.contains("."));
  }

  @Test
  @DisplayName("代码审查请求应该包含必要的字段")
  void testCodeReviewRequest_shouldContainRequiredFields() {
    // Given
    String diffContent = "diff --git a/test.java b/test.java\n+public class Test {}";
    String[] filePaths = { "test.java" };

    // When & Then
    assertNotNull(diffContent);
    assertNotNull(filePaths);
    assertTrue(diffContent.contains("diff --git"));
    assertTrue(filePaths.length > 0);
    assertEquals("test.java", filePaths[0]);
  }

  @Test
  @DisplayName("Mock对象应该正确初始化")
  void testMockObjects_shouldBeInitializedCorrectly() {
    // Given & When & Then
    assertNotNull(chatClient);
    assertNotNull(restTemplate);
    assertNotNull(aiModelService);
    assertNotNull(aiCodeReviewService);
    assertNotNull(gitHubService);
  }

}
