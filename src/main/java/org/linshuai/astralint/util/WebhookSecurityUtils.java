package org.linshuai.astralint.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Webhook安全验证工具类
 */
@Component
public class WebhookSecurityUtils {

  private static final Logger logger = LoggerFactory.getLogger(WebhookSecurityUtils.class);

  private static final String HMAC_SHA256 = "HmacSHA256";
  private static final String SHA256 = "SHA256";

  /**
   * 验证GitHub Webhook签名
   * GitHub使用HMAC-SHA256签名
   */
  public boolean verifyGitHubSignature(String payload, String signature, String secret) {
    if (payload == null || signature == null || secret == null) {
      logger.warn("GitHub Webhook验证失败: 参数为空");
      return false;
    }

    try {
      // GitHub签名格式: sha256=<signature>
      if (!signature.startsWith("sha256=")) {
        logger.warn("GitHub Webhook签名格式错误: {}", signature);
        return false;
      }

      String expectedSignature = "sha256=" + calculateHmacSha256(payload, secret);
      boolean isValid = secureEquals(signature, expectedSignature);

      if (!isValid) {
        logger.warn("GitHub Webhook签名验证失败");
      }

      return isValid;

    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      logger.error("GitHub Webhook签名验证异常", e);
      return false;
    }
  }

  /**
   * 验证GitLab Webhook Token
   * GitLab使用简单的token验证
   */
  public boolean verifyGitLabToken(String receivedToken, String expectedToken) {
    if (receivedToken == null || expectedToken == null) {
      logger.warn("GitLab Webhook验证失败: token为空");
      return false;
    }

    boolean isValid = secureEquals(receivedToken, expectedToken);

    if (!isValid) {
      logger.warn("GitLab Webhook token验证失败");
    }

    return isValid;
  }

  /**
   * 验证Gitee Webhook签名
   * Gitee支持多种验证方式，这里实现password验证
   */
  public boolean verifyGiteeSignature(String payload, String signature, String secret) {
    if (payload == null || signature == null || secret == null) {
      logger.warn("Gitee Webhook验证失败: 参数为空");
      return false;
    }

    try {
      // Gitee的签名计算方式
      String expectedSignature = calculateGiteeSignature(payload, secret);
      boolean isValid = secureEquals(signature, expectedSignature);

      if (!isValid) {
        logger.warn("Gitee Webhook签名验证失败");
      }

      return isValid;

    } catch (Exception e) {
      logger.error("Gitee Webhook签名验证异常", e);
      return false;
    }
  }

  /**
   * 计算HMAC-SHA256签名
   */
  private String calculateHmacSha256(String payload, String secret)
      throws NoSuchAlgorithmException, InvalidKeyException {

    Mac mac = Mac.getInstance(HMAC_SHA256);
    SecretKeySpec secretKeySpec = new SecretKeySpec(
        secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
    mac.init(secretKeySpec);

    byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
    return bytesToHex(hash);
  }

  /**
   * 计算Gitee签名 (使用SHA256)
   */
  private String calculateGiteeSignature(String payload, String secret)
      throws NoSuchAlgorithmException {

    String data = payload + secret;
    MessageDigest digest = MessageDigest.getInstance(SHA256);
    byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
    return bytesToHex(hash);
  }

  /**
   * 字节数组转十六进制字符串
   */
  private String bytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
      result.append(String.format("%02x", b));
    }
    return result.toString();
  }

  /**
   * 安全的字符串比较，防止时序攻击
   */
  private boolean secureEquals(String a, String b) {
    if (a == null || b == null) {
      return false;
    }

    if (a.length() != b.length()) {
      return false;
    }

    int result = 0;
    for (int i = 0; i < a.length(); i++) {
      result |= a.charAt(i) ^ b.charAt(i);
    }

    return result == 0;
  }

  /**
   * 验证请求来源IP（可选的额外安全措施）
   */
  public boolean verifySourceIP(String clientIP, String[] allowedIPs) {
    if (clientIP == null || allowedIPs == null || allowedIPs.length == 0) {
      return true; // 如果没有配置IP限制，则跳过验证
    }

    for (String allowedIP : allowedIPs) {
      if (Objects.equals(clientIP, allowedIP)) {
        return true;
      }
    }

    logger.warn("Webhook请求来自未授权IP: {}", clientIP);
    return false;
  }

  /**
   * 验证时间戳，防止重放攻击
   */
  public boolean verifyTimestamp(long timestamp, long toleranceSeconds) {
    long currentTime = System.currentTimeMillis() / 1000;
    long timeDiff = Math.abs(currentTime - timestamp);

    if (timeDiff > toleranceSeconds) {
      logger.warn("Webhook时间戳过期: 时间差{}秒", timeDiff);
      return false;
    }

    return true;
  }
}
