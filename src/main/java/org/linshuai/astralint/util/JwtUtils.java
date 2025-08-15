package org.linshuai.astralint.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Component
public class JwtUtils {

  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${security.jwt.secret:AstraLint_Default_Secret_Key_Must_Be_At_Least_256_Bits_Long_For_Production}")
  private String jwtSecret;

  @Value("${security.jwt.expiration:86400000}") // 24小时
  private long jwtExpiration;

  private static final String CLAIM_USERNAME = "username";
  private static final String CLAIM_ROLES = "roles";

  /**
   * 获取签名密钥
   */
  private SecretKey getSigningKey() {
    byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * 生成JWT Token
   */
  public String generateToken(String username, String[] roles) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIM_USERNAME, username);
    claims.put(CLAIM_ROLES, roles);

    return createToken(claims, username);
  }

  /**
   * 创建Token
   */
  private String createToken(Map<String, Object> claims, String subject) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpiration);

    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  /**
   * 从Token中获取用户名
   */
  public String getUsernameFromToken(String token) {
    try {
      Claims claims = getClaimsFromToken(token);
      return claims.getSubject();
    } catch (Exception e) {
      logger.error("从Token中获取用户名失败", e);
      return null;
    }
  }

  /**
   * 从Token中获取角色
   */
  @SuppressWarnings("unchecked")
  public String[] getRolesFromToken(String token) {
    try {
      Claims claims = getClaimsFromToken(token);
      Object rolesObj = claims.get(CLAIM_ROLES);
      if (rolesObj instanceof java.util.List) {
        java.util.List<String> rolesList = (java.util.List<String>) rolesObj;
        return rolesList.toArray(new String[0]);
      }
      return new String[0];
    } catch (Exception e) {
      logger.error("从Token中获取角色失败", e);
      return new String[0];
    }
  }

  /**
   * 从Token中获取过期时间
   */
  public Date getExpirationDateFromToken(String token) {
    try {
      Claims claims = getClaimsFromToken(token);
      return claims.getExpiration();
    } catch (Exception e) {
      logger.error("从Token中获取过期时间失败", e);
      return null;
    }
  }

  /**
   * 从Token中获取Claims
   */
  private Claims getClaimsFromToken(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * 检查Token是否过期
   */
  public boolean isTokenExpired(String token) {
    try {
      Date expiration = getExpirationDateFromToken(token);
      return expiration != null && expiration.before(new Date());
    } catch (Exception e) {
      logger.error("检查Token过期状态失败", e);
      return true;
    }
  }

  /**
   * 验证Token
   */
  public boolean validateToken(String token, String username) {
    try {
      String tokenUsername = getUsernameFromToken(token);
      return (username.equals(tokenUsername) && !isTokenExpired(token));
    } catch (Exception e) {
      logger.error("验证Token失败", e);
      return false;
    }
  }

  /**
   * 验证Token格式和签名
   */
  public boolean isValidToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("无效的JWT Token", e);
    } catch (ExpiredJwtException e) {
      logger.error("JWT Token已过期", e);
    } catch (UnsupportedJwtException e) {
      logger.error("不支持的JWT Token", e);
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims字符串为空", e);
    } catch (Exception e) {
      logger.error("JWT Token验证失败", e);
    }
    return false;
  }

  /**
   * 从请求头中提取Token
   */
  public String resolveToken(String bearerToken) {
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  /**
   * 刷新Token（生成新的Token）
   */
  public String refreshToken(String token) {
    try {
      Claims claims = getClaimsFromToken(token);
      String username = claims.getSubject();

      @SuppressWarnings("unchecked")
      java.util.List<String> rolesList = (java.util.List<String>) claims.get(CLAIM_ROLES);
      String[] roles = rolesList != null ? rolesList.toArray(new String[0]) : new String[0];

      return generateToken(username, roles);
    } catch (Exception e) {
      logger.error("刷新Token失败", e);
      return null;
    }
  }
}
