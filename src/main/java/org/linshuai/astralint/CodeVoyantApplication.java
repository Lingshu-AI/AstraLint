package org.linshuai.astralint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * CodeVoyant 智能代码审查系统主应用类
 */
@SpringBootApplication
@EnableAsync
public class CodeVoyantApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeVoyantApplication.class, args);
    }
} 