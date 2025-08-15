package org.linshuai.astralint.config;

import org.linshuai.astralint.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Autowired
  private JwtUtils jwtUtils;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      String jwt = getJwtFromRequest(request);

      if (jwt != null && jwtUtils.isValidToken(jwt)) {
        String username = jwtUtils.getUsernameFromToken(jwt);
        String[] roles = jwtUtils.getRolesFromToken(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          // 创建权限列表
          List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase(Locale.ROOT)))
              .collect(Collectors.toList());

          // 创建认证对象
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
              authorities);
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          // 设置安全上下文
          SecurityContextHolder.getContext().setAuthentication(authentication);

          logger.debug("用户 {} 认证成功，角色: {}", username, Arrays.toString(roles));
        }
      }
    } catch (Exception e) {
      logger.error("无法设置用户认证: {}", e.getMessage());
      // 不要抛出异常，让请求继续，由Spring Security处理未认证的请求
    }

    filterChain.doFilter(request, response);
  }

  /**
   * 从请求中提取JWT Token
   */
  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    return jwtUtils.resolveToken(bearerToken);
  }

  /**
   * 判断是否需要过滤
   */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();

    // 静态资源和公开端点跳过JWT验证
    return path.startsWith("/static/") ||
        path.equals("/") ||
        path.equals("/index.html") ||
        path.equals("/review.html") ||
        path.equals("/secure-review.html") ||
        path.startsWith("/actuator/health") ||
        path.startsWith("/actuator/info") ||
        path.startsWith("/api/webhook/") ||
        path.equals("/api/code-review/health") ||
        path.equals("/api/code-review/quick") ||
        path.equals("/api/auth/login");
  }
}
