package org.linshuai.astralint.constant;

/**
 * 代码审查常量类
 */
public class CodeReviewConstants {
    
    // 审查类型
    public static final String REVIEW_TYPE_BASIC = "BASIC";
    public static final String REVIEW_TYPE_SECURITY = "SECURITY";
    public static final String REVIEW_TYPE_PERFORMANCE = "PERFORMANCE";
    public static final String REVIEW_TYPE_COMPREHENSIVE = "COMPREHENSIVE";
    
    // 优先级
    public static final String PRIORITY_LOW = "LOW";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_HIGH = "HIGH";
    
    // 状态
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_FAILED = "FAILED";
    
    // 仓库类型
    public static final String REPOSITORY_TYPE_GITLAB = "GITLAB";
    public static final String REPOSITORY_TYPE_GITHUB = "GITHUB";
    public static final String REPOSITORY_TYPE_GITEE = "GITEE";
    
    // AI模型提供商
    public static final String AI_PROVIDER_ALIBABA = "ALIBABA";
    public static final String AI_PROVIDER_OPENAI = "OPENAI";
    public static final String AI_PROVIDER_ANTHROPIC = "ANTHROPIC";
    
    // 默认配置
    public static final int DEFAULT_MAX_FILE_SIZE = 10000;
    public static final int DEFAULT_TIMEOUT_MS = 30000;
    public static final double DEFAULT_TEMPERATURE = 0.7;
    public static final int DEFAULT_MAX_TOKENS = 4000;
    public static final int DEFAULT_REVIEW_THRESHOLD = 100;
    
    // 缓存名称
    public static final String CACHE_AI_MODELS = "aiModels";
    public static final String CACHE_REPOSITORIES = "repositories";
    public static final String CACHE_CODE_REVIEWS = "codeReviews";
    public static final String CACHE_USER_SESSIONS = "userSessions";
    
    // 安全相关
    public static final String SECURITY_ISSUE_SQL_INJECTION = "SQL_INJECTION";
    public static final String SECURITY_ISSUE_XSS = "XSS";
    public static final String SECURITY_ISSUE_CSRF = "CSRF";
    public static final String SECURITY_ISSUE_PATH_TRAVERSAL = "PATH_TRAVERSAL";
    public static final String SECURITY_ISSUE_COMMAND_INJECTION = "COMMAND_INJECTION";
    
    // 性能问题类型
    public static final String PERFORMANCE_ISSUE_N_PLUS_ONE = "N_PLUS_ONE";
    public static final String PERFORMANCE_ISSUE_MEMORY_LEAK = "MEMORY_LEAK";
    public static final String PERFORMANCE_ISSUE_SLOW_ALGORITHM = "SLOW_ALGORITHM";
    public static final String PERFORMANCE_ISSUE_INEFFICIENT_QUERY = "INEFFICIENT_QUERY";
    
    // 代码质量问题类型
    public static final String QUALITY_ISSUE_CODE_DUPLICATION = "CODE_DUPLICATION";
    public static final String QUALITY_ISSUE_COMPLEX_METHOD = "COMPLEX_METHOD";
    public static final String QUALITY_ISSUE_LONG_METHOD = "LONG_METHOD";
    public static final String QUALITY_ISSUE_MISSING_DOCUMENTATION = "MISSING_DOCUMENTATION";
    
    // 严重程度
    public static final String SEVERITY_LOW = "LOW";
    public static final String SEVERITY_MEDIUM = "MEDIUM";
    public static final String SEVERITY_HIGH = "HIGH";
    public static final String SEVERITY_CRITICAL = "CRITICAL";
    
    // 消息模板
    public static final String MSG_REVIEW_STARTED = "代码审查已启动";
    public static final String MSG_REVIEW_COMPLETED = "代码审查已完成";
    public static final String MSG_REVIEW_FAILED = "代码审查失败";
    public static final String MSG_NO_CHANGES = "无代码变更";
    public static final String MSG_REPOSITORY_NOT_CONFIGURED = "仓库未配置自动审查";
    public static final String MSG_REPOSITORY_DISABLED = "仓库已禁用自动审查";
    
    // 错误消息
    public static final String ERROR_INVALID_PAYLOAD = "无效的payload格式";
    public static final String ERROR_SIGNATURE_VERIFICATION_FAILED = "签名验证失败";
    public static final String ERROR_PROCESSING_FAILED = "处理失败";
    public static final String ERROR_CONNECTION_FAILED = "连接失败";
    
    // 私有构造函数防止实例化
    private CodeReviewConstants() {
        throw new UnsupportedOperationException("常量类不能实例化");
    }
} 