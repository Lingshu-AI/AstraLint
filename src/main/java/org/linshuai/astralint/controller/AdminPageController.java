package org.linshuai.astralint.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面控制器
 */
@Controller
public class AdminPageController {

  /**
   * 管理页面首页
   */
  @GetMapping("/admin")
  public String adminPage() {
    return "redirect:/admin/index.html";
  }

  /**
   * 代码审查页面
   */
  @GetMapping("/review")
  public String reviewPage() {
    return "redirect:/review.html";
  }

  /**
   * 安全代码审查页面
   */
  @GetMapping("/secure-review")
  public String secureReviewPage() {
    return "redirect:/secure-review.html";
  }
}
