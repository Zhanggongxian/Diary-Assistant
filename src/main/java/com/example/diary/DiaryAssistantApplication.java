package com.example.diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 日记助手应用主类
 * 
 * 功能特性：
 * - 命令行交互式日记记录
 * - 本地大模型驱动的内容生成
 * - 数据持久化存储
 * - 严格的日记助手规则约束
 * 
 * 使用说明：
 * 1. 确保Ollama服务运行在http://localhost:11434
 * 2. 启动应用后使用Spring Shell命令进行交互
 * 3. 遵循'开始日记' -> '记录内容' -> '结束日记'的流程
 * 
 * 技术栈：
 * - Spring Boot 3.2.0
 * - Spring Shell 3.1.4
 * - Jackson JSON处理
 * - HTTP客户端调用Ollama API
 */
@SpringBootApplication
public class DiaryAssistantApplication {

    /**
     * 应用入口点
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        System.out.println("""
        📖 日记助手启动中...
        
        功能说明：
        • 本地大模型驱动的日记记录系统
        • 严格遵循日记助手规则
        • 数据自动持久化存储
        
        启动后请输入'帮助'查看可用命令
        """);
        
        SpringApplication.run(DiaryAssistantApplication.class, args);
    }
}