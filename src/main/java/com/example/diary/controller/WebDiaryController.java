package com.example.diary.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.diary.service.DiarySessionService;

/**
 * Web日记助手控制器
 * 提供Web界面操作日记助手的功能
 */
@Controller
public class WebDiaryController {
    
    private final DiarySessionService diaryService;
    
    @Autowired
    public WebDiaryController(DiarySessionService diaryService) {
        this.diaryService = diaryService;
    }
    
    /**
     * 显示日记助手主页
     */
    @GetMapping("/")
    public String index(Model model) {
        // 检查服务状态
        boolean serviceAvailable = diaryService.isOllamaAvailable();
        String sessionStatus = diaryService.getSessionStatus();
        
        model.addAttribute("serviceAvailable", serviceAvailable);
        model.addAttribute("sessionStatus", sessionStatus);
        
        return "index";
    }
    
    /**
     * 开始日记记录
     */
    @PostMapping("/start")
    @ResponseBody
    public Map<String, Object> startDiary() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String result = diaryService.processInput("开始日记");
            response.put("success", true);
            response.put("message", result);
            response.put("sessionStatus", diaryService.getSessionStatus());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "开始日记失败：" + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 记录日记内容
     */
    @PostMapping("/record")
    @ResponseBody
    public Map<String, Object> recordDiary(@RequestParam String content) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String result = diaryService.processInput(content);
            response.put("success", true);
            response.put("message", result);
            response.put("sessionStatus", diaryService.getSessionStatus());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "记录日记失败：" + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 结束日记并生成总结
     */
    @PostMapping("/end")
    @ResponseBody
    public Map<String, Object> endDiary() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String result = diaryService.processInput("让我们结束今天的日记吧");
            response.put("success", true);
            response.put("message", result);
            response.put("sessionStatus", diaryService.getSessionStatus());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "结束日记失败：" + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取当前会话状态
     */
    @GetMapping("/status")
    @ResponseBody
    public Map<String, Object> getStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String status = diaryService.getSessionStatus();
            boolean serviceAvailable = diaryService.isOllamaAvailable();
            
            response.put("success", true);
            response.put("sessionStatus", status);
            response.put("serviceAvailable", serviceAvailable);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取状态失败：" + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 显示日历页面
     */
    @GetMapping("/calendar")
    public String calendar() {
        return "calendar";
    }
}