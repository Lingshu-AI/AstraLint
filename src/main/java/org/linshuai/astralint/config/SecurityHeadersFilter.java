package org.linshuai.astralint.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 安全头过滤器
 */
@Component
public class SecurityHeadersFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletResponse httpResponse = (HttpServletResponse) response;

    // Content Security Policy - 内容安全策略
    httpResponse.setHeader("Content-Security-Policy",
        "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' 'unsafe-eval' " +
            "https://unpkg.com https://cdnjs.cloudflare.com; " +
            "style-src 'self' 'unsafe-inline' " +
            "https://unpkg.com https://cdnjs.cloudflare.com; " +
            "font-src 'self' https://cdnjs.cloudflare.com; " +
            "img-src 'self' data:; " +
            "connect-src 'self'; " +
            "frame-ancestors 'none'; " +
            "base-uri 'self'; " +
            "form-action 'self'");

    // X-Content-Type-Options - 防止MIME类型嗅探
    httpResponse.setHeader("X-Content-Type-Options", "nosniff");

    // X-Frame-Options - 防止点击劫持
    httpResponse.setHeader("X-Frame-Options", "DENY");

    // X-XSS-Protection - XSS保护（虽然现代浏览器主要依赖CSP）
    httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

    // Referrer-Policy - 控制Referrer信息
    httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

    // Permissions-Policy - 功能策略
    httpResponse.setHeader("Permissions-Policy",
        "camera=(), " +
            "microphone=(), " +
            "geolocation=(), " +
            "payment=(), " +
            "usb=(), " +
            "magnetometer=(), " +
            "accelerometer=(), " +
            "gyroscope=()");

    // Cache-Control for sensitive endpoints
    if (request instanceof jakarta.servlet.http.HttpServletRequest) {
      String requestURI = ((jakarta.servlet.http.HttpServletRequest) request).getRequestURI();
      if (requestURI.startsWith("/api/admin") || requestURI.startsWith("/actuator")) {
        httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, private");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setHeader("Expires", "0");
      }
    }

    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) {
    // 初始化逻辑（如果需要）
  }

  @Override
  public void destroy() {
    // 清理逻辑（如果需要）
  }
}
