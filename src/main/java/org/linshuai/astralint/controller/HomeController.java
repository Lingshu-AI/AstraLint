package org.linshuai.astralint.controller;


import org.linshuai.astralint.service.AiModelConfigService;
import org.linshuai.astralint.service.RepositoryConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    private AiModelConfigService aiModelConfigService;
    
    @Autowired
    private RepositoryConfigService repositoryConfigService;
    
    /**
     * 首页视图
     */
    @GetMapping("/")
    public String index(Model model) {
        try {
            Map<String, Object> dashboard = new HashMap<>();
            
            // AI模型统计
            dashboard.put("totalAiModels", aiModelConfigService.getAllModelConfigs().size());
            
            // 仓库统计
            dashboard.put("totalRepositories", repositoryConfigService.getAllRepositoryConfigs().size());
            
            // 默认值
            dashboard.put("totalReviews", 0);
            dashboard.put("totalIssues", 0);
            
            model.addAttribute("dashboard", dashboard);
        } catch (Exception e) {
            logger.error("获取首页统计数据失败", e);
            // 提供默认数据
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("totalReviews", 0);
            dashboard.put("totalIssues", 0);
            dashboard.put("totalRepositories", 0);
            dashboard.put("totalAiModels", 0);
            model.addAttribute("dashboard", dashboard);
        }
        
        return "index";
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}