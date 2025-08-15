package org.linshuai.astralint.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 安全配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS配置
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF配置 - 对于API，暂时禁用CSRF，但保留webhook的保护
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/webhook/**")
                        .disable())

                // 会话管理
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 安全头配置
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.deny())
                        .contentTypeOptions(contentTypeOptions -> {
                        })
                        .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                                .maxAgeInSeconds(31536000)
                                .includeSubDomains(true))
                        .referrerPolicy(referrerPolicy -> referrerPolicy
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)))

                // 权限配置
                .authorizeHttpRequests(authz -> authz
                        // 公开的健康检查端点
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                        // 静态资源
                        .requestMatchers("/static/**", "/admin/**", "/admin", "/", "/index.html", "/review.html",
                                "/secure-review.html")
                        .permitAll()

                        // 认证端点
                        .requestMatchers("/api/auth/**").permitAll()

                        // Webhook端点 - 需要自定义验证
                        .requestMatchers("/api/webhook/**").permitAll()

                        // 公开的代码审查API
                        .requestMatchers("/api/code-review/health", "/api/code-review/quick").permitAll()

                        // 管理API需要ADMIN角色
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 其他API端点需要认证
                        .requestMatchers("/api/**").authenticated()

                        // 其他请求允许访问
                        .anyRequest().permitAll())

                // 添加JWT过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 生产环境应该设置具体的域名
        // 开发环境允许localhost
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*",
                "https://localhost:*",
                "http://127.0.0.1:*",
                "https://127.0.0.1:*"
        // 生产环境添加具体域名
        // "https://yourdomain.com"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "X-Gitlab-Event",
                "X-Gitlab-Token",
                "X-GitHub-Event",
                "X-Hub-Signature-256",
                "X-Gitee-Event",
                "X-Gitee-Token"));

        configuration.setExposedHeaders(Arrays.asList(
                "Authorization"));

        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
