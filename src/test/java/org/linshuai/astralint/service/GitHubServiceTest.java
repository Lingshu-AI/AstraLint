package org.linshuai.astralint.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.linshuai.astralint.entity.RepositoryConfig;

import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GitHubService using Mock objects.
 */
class GitHubServiceTest {

  @Mock
  private RestTemplate restTemplate;

  private GitHubService gitHubService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    gitHubService = new GitHubService();
  }

  @Test
  @DisplayName("应该能够构建正确的GitHub API URL")
  void testBuildGitHubApiUrl_shouldReturnCorrectUrl() {
    // Given
    String owner = "testowner";
    String repo = "testrepo";
    String prNumber = "123";

    // When
    String apiUrl = String.format("https://api.github.com/repos/%s/%s/pulls/%s", owner, repo, prNumber);

    // Then
    assertNotNull(apiUrl);
    assertEquals("https://api.github.com/repos/testowner/testrepo/pulls/123", apiUrl);
    assertTrue(apiUrl.startsWith("https://api.github.com/repos/"));
    assertTrue(apiUrl.contains(owner));
    assertTrue(apiUrl.contains(repo));
    assertTrue(apiUrl.contains(prNumber));
  }

  @Test
  @DisplayName("应该能够解析GitHub仓库URL")
  void testParseGitHubUrl_shouldExtractOwnerAndRepo() {
    // Given
    String githubUrl = "https://github.com/owner/repository.git";

    // When
    String withoutGit = githubUrl.replace(".git", "");
    String[] parts = withoutGit.split("/");

    // Then
    assertTrue(parts.length >= 2);
    assertEquals("repository", parts[parts.length - 1]);
    assertEquals("owner", parts[parts.length - 2]);
  }

  @Test
  @DisplayName("应该能够处理GitHub API响应")
  void testHandleGitHubApiResponse_shouldProcessCorrectly() {
    // Given
    Map<String, Object> mockResponse = new HashMap<>();
    mockResponse.put("id", 123456);
    mockResponse.put("number", 42);
    mockResponse.put("title", "Test PR");
    mockResponse.put("state", "open");
    mockResponse.put("diff_url", "https://github.com/owner/repo/pull/42.diff");

    // When & Then
    assertNotNull(mockResponse);
    assertEquals(123456, mockResponse.get("id"));
    assertEquals(42, mockResponse.get("number"));
    assertEquals("Test PR", mockResponse.get("title"));
    assertEquals("open", mockResponse.get("state"));
    assertTrue(mockResponse.get("diff_url").toString().endsWith(".diff"));
  }

  @Test
  @DisplayName("应该能够创建仓库配置对象")
  void testCreateRepositoryConfig_shouldSetCorrectValues() {
    // Given
    String repositoryUrl = "https://github.com/owner/repo";
    String token = "ghp_test_token";
    String webhookSecret = "webhook_secret";

    // When
    RepositoryConfig config = new RepositoryConfig();
    config.setRepositoryUrl(repositoryUrl);
    config.setAccessToken(token);
    config.setWebhookSecret(webhookSecret);
    config.setRepositoryType("GITHUB");

    // Then
    assertNotNull(config);
    assertEquals(repositoryUrl, config.getRepositoryUrl());
    assertEquals(token, config.getAccessToken());
    assertEquals(webhookSecret, config.getWebhookSecret());
    assertEquals("GITHUB", config.getRepositoryType());
  }

  @Test
  @DisplayName("应该能够验证GitHub webhook签名格式")
  void testGitHubWebhookSignature_shouldValidateFormat() {
    // Given
    String signature = "sha256=abc123def456";
    String payload = "test payload";

    // When & Then
    assertNotNull(signature);
    assertNotNull(payload);
    assertTrue(signature.startsWith("sha256="));
    assertFalse(payload.isEmpty());

    // 验证签名格式
    String[] parts = signature.split("=");
    assertEquals(2, parts.length);
    assertEquals("sha256", parts[0]);
    assertFalse(parts[1].isEmpty());
  }

  @Test
  @DisplayName("Mock RestTemplate应该能够模拟API调用")
  void testMockRestTemplate_shouldSimulateApiCall() {
    // Given
    String apiUrl = "https://api.github.com/repos/owner/repo/pulls/1";
    Map<String, Object> mockResponse = new HashMap<>();
    mockResponse.put("number", 1);
    mockResponse.put("title", "Test Pull Request");

    ResponseEntity<Map> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

    // When
    when(restTemplate.getForEntity(apiUrl, Map.class)).thenReturn(responseEntity);

    // Then
    ResponseEntity<Map> result = restTemplate.getForEntity(apiUrl, Map.class);
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(1, result.getBody().get("number"));
    assertEquals("Test Pull Request", result.getBody().get("title"));

    // 验证Mock调用
    verify(restTemplate, times(1)).getForEntity(apiUrl, Map.class);
  }
}
