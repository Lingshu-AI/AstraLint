package org.linshuai.astralint.controller;

import org.linshuai.astralint.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private JwtUtils jwtUtils;

  // 临时的硬编码用户，生产环境应该使用数据库
  @Value("${security.default.admin.username:admin}")
  private String defaultAdminUsername;

  @Value("${security.default.admin.password:admin123}")
  private String defaultAdminPassword;

  /**
   * 登录请求DTO
   */
  public static class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过50个字符")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    private String password;

    // Getters and Setters
    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }

  /**
   * 用户登录
   */
  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
    try {
      logger.info("用户登录尝试: {}", request.getUsername());

      // 简单的用户验证（生产环境应该使用加密密码和数据库）
      if (isValidUser(request.getUsername(), request.getPassword())) {
        // 根据用户名确定角色
        String[] roles = getUserRoles(request.getUsername());

        // 生成JWT Token
        String token = jwtUtils.generateToken(request.getUsername(), roles);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "登录成功");
        response.put("token", token);
        response.put("username", request.getUsername());
        response.put("roles", roles);
        response.put("timestamp", LocalDateTime.now());

        logger.info("用户 {} 登录成功", request.getUsername());
        return ResponseEntity.ok(response);

      } else {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "用户名或密码错误");
        errorResponse.put("timestamp", LocalDateTime.now());

        logger.warn("用户 {} 登录失败: 凭据无效", request.getUsername());
        return ResponseEntity.badRequest().body(errorResponse);
      }

    } catch (Exception e) {
      logger.error("登录处理异常", e);

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("success", false);
      errorResponse.put("message", "登录处理失败，请稍后重试");
      errorResponse.put("timestamp", LocalDateTime.now());

      return ResponseEntity.internalServerError().body(errorResponse);
    }
  }

  /**
   * 刷新Token
   */
  @PostMapping("/refresh")
  public ResponseEntity<Map<String, Object>> refreshToken(
      @RequestHeader("Authorization") String authHeader) {
    try {
      String token = jwtUtils.resolveToken(authHeader);

      if (token != null && jwtUtils.isValidToken(token)) {
        String newToken = jwtUtils.refreshToken(token);

        if (newToken != null) {
          Map<String, Object> response = new HashMap<>();
          response.put("success", true);
          response.put("message", "Token刷新成功");
          response.put("token", newToken);
          response.put("timestamp", LocalDateTime.now());

          return ResponseEntity.ok(response);
        }
      }

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("success", false);
      errorResponse.put("message", "Token刷新失败");
      errorResponse.put("timestamp", LocalDateTime.now());

      return ResponseEntity.badRequest().body(errorResponse);

    } catch (Exception e) {
      logger.error("Token刷新异常", e);

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("success", false);
      errorResponse.put("message", "Token刷新失败");
      errorResponse.put("timestamp", LocalDateTime.now());

      return ResponseEntity.internalServerError().body(errorResponse);
    }
  }

  /**
   * 验证Token
   */
  @PostMapping("/validate")
  public ResponseEntity<Map<String, Object>> validateToken(
      @RequestHeader("Authorization") String authHeader) {
    try {
      String token = jwtUtils.resolveToken(authHeader);

      if (token != null && jwtUtils.isValidToken(token)) {
        String username = jwtUtils.getUsernameFromToken(token);
        String[] roles = jwtUtils.getRolesFromToken(token);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("username", username);
        response.put("roles", roles);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
      }

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("valid", false);
      errorResponse.put("message", "Token无效或已过期");
      errorResponse.put("timestamp", LocalDateTime.now());

      return ResponseEntity.badRequest().body(errorResponse);

    } catch (Exception e) {
      logger.error("Token验证异常", e);

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("valid", false);
      errorResponse.put("message", "Token验证失败");
      errorResponse.put("timestamp", LocalDateTime.now());

      return ResponseEntity.internalServerError().body(errorResponse);
    }
  }

  /**
   * 用户注销
   */
  @PostMapping("/logout")
  public ResponseEntity<Map<String, Object>> logout() {
    // JWT是无状态的，注销主要是客户端删除Token
    // 如果需要服务端注销，可以维护一个黑名单

    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("message", "注销成功");
    response.put("timestamp", LocalDateTime.now());

    return ResponseEntity.ok(response);
  }

  /**
   * 验证用户凭据（简单实现，生产环境应该查询数据库）
   */
  private boolean isValidUser(String username, String password) {
    // 默认管理员账户
    if (Objects.equals(username, defaultAdminUsername) &&
        Objects.equals(password, defaultAdminPassword)) {
      return true;
    }

    // 可以添加更多用户验证逻辑
    // 生产环境应该：
    // 1. 查询数据库
    // 2. 验证密码哈希
    // 3. 检查账户状态

    return false;
  }

  /**
   * 获取用户角色（简单实现）
   */
  private String[] getUserRoles(String username) {
    if (Objects.equals(username, defaultAdminUsername)) {
      return new String[] { "ADMIN", "USER" };
    }

    // 默认角色
    return new String[] { "USER" };
  }
}
