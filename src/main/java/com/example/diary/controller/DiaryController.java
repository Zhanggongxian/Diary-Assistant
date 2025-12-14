package com.example.diary.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.example.diary.service.DiarySessionService;

/**
 * 日记助手命令行控制器
 * 提供Spring Shell命令接口
 */
@ShellComponent
public class DiaryController {
    
    private static final Logger logger = LoggerFactory.getLogger(DiaryController.class);
    
    private final DiarySessionService diaryService;
    
    /**
     * 构造函数
     */
    @Autowired
    public DiaryController(DiarySessionService diaryService) {
        this.diaryService = diaryService;
    }
    
    /**
     * 开始日记命令
     * @return 响应消息
     */
    @ShellMethod(key = "开始日记", value = "开启当天的日记记录会话")
    public String startDiary() {
        return diaryService.processInput("开始日记");
    }
    
    /**
     * 结束日记命令
     * @return 模型生成的日记总结
     */
    @ShellMethod(key = "结束日记", value = "结束当天的日记记录并生成总结")
    public String endDiary() {
        return diaryService.processInput("让我们结束今天的日记吧");
    }
    
    /**
     * 记录日记内容
     * @param content 日记内容
     * @return 固定响应文本
     */
    @ShellMethod(key = "记录", value = "记录一条日记内容")
    public String record(@ShellOption("内容") String content) {
        return diaryService.processInput(content);
    }
    
    /**
     * 默认命令处理 - 处理直接输入的内容
     * @param content 用户直接输入的内容
     * @return 固定响应文本
     */
    @ShellMethod(key = "", value = "处理直接输入的内容")
    public String defaultCommand(String content) {
        return diaryService.processInput(content);
    }
    
    /**
     * 检查会话状态
     * @return 当前会话状态
     */
    @ShellMethod(key = "状态", value = "查看当前日记会话状态")
    public String status() {
        return diaryService.getSessionStatus();
    }
    
    /**
     * 检查Ollama服务状态
     * @return 服务状态信息
     */
    @ShellMethod(key = "检查服务", value = "检查Ollama服务是否可用")
    public String checkService() {
        boolean available = diaryService.isOllamaAvailable();
        if (available) {
            return "✅ Ollama服务可用，可以正常使用日记助手功能";
        } else {
            return "❌ Ollama服务不可用，请确保Ollama已启动并运行在http://localhost:11434";
        }
    }
    
    /**
     * 显示帮助信息
     * @return 帮助信息
     */
    @ShellMethod(key = "帮助", value = "显示日记助手使用帮助")
    public String help() {
        return """
        📖 日记助手使用指南
        
        可用命令：
        • 开始日记      - 开启当天的日记记录
        • 记录 <内容>   - 记录一条日记内容
        • 结束日记      - 结束日记并生成总结
        • 状态          - 查看当前会话状态
        • 检查服务      - 检查Ollama服务状态
        • 帮助          - 显示此帮助信息
        
        使用流程：
        1. 输入'开始日记'开始记录
        2. 使用'记录 <内容>'记录日常事件
        3. 输入'结束日记'生成完整总结
        
        注意事项：
        • 可以直接输入内容，系统会回复'收到啦'
        • 记录阶段只会回复'收到啦。'
        • 只有在结束日记时才会调用大模型
        """;
    }
}