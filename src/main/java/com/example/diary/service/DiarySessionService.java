package com.example.diary.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.diary.model.DiarySession;
import com.example.diary.persistence.FileDiaryRepository;

/**
 * 日记会话管理服务
 * 负责管理日记会话的生命周期和业务逻辑
 */
@Service
public class DiarySessionService {
    
    private static final Logger logger = LoggerFactory.getLogger(DiarySessionService.class);
    
    /**
     * 开始日记命令
     */
    private static final String START_COMMAND = "开始日记";
    
    /**
     * 结束日记命令
     */
    private static final String END_COMMAND = "让我们结束今天的日记吧";
    
    /**
     * 固定响应文本
     */
    private static final String FIXED_RESPONSE = "收到啦。";
    
    /**
     * 开始日记时的欢迎语
     */
    private static final String WELCOME_MESSAGE = "今日记录，每一笔都是生活的小闪光～";
    
    /**
     * 当前活跃的日记会话
     */
    private DiarySession currentSession;
    
    private final FileDiaryRepository diaryRepository;
    private final OllamaService ollamaService;
    private final PromptBuilder promptBuilder;
    
    /**
     * 构造函数
     */
    @Autowired
    public DiarySessionService(FileDiaryRepository diaryRepository, 
                              OllamaService ollamaService, 
                              PromptBuilder promptBuilder) {
        this.diaryRepository = diaryRepository;
        this.ollamaService = ollamaService;
        this.promptBuilder = promptBuilder;
    }
    
    /**
     * 处理用户输入
     * @param input 用户输入
     * @return 处理结果
     */
    public String processInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "请输入有效内容";
        }
        
        String trimmedInput = input.trim();
        
        // 处理特殊命令
        switch (trimmedInput) {
            case "开始日记":
                return startDiarySession();
            case "让我们结束今天的日记吧":
                return endDiarySession();
            case "状态":
                return getSessionStatus();
            default:
                // 处理普通内容输入（包括直接输入的内容）
                return processContentInput(trimmedInput);
        }
    }
    
    /**
     * 开始日记会话
     * @return 响应消息
     */
    private String startDiarySession() {
        // 检查是否已有活跃会话
        if (currentSession != null && currentSession.isActive()) {
            return "今天已经开始了日记记录，请继续记录或输入'结束日记'来结束";
        }
        
        // 创建新的日记会话（去掉每天只能记录一次的限制）
        currentSession = new DiarySession();
        currentSession.startSession();
        
        logger.info("开始新的日记会话，日期：{}", LocalDate.now());
        
        return WELCOME_MESSAGE;
    }
    
    /**
     * 处理普通内容输入
     * @param content 用户输入的内容
     * @return 处理结果
     */
    private String processContentInput(String content) {
        // 如果有活跃会话，记录内容并回复"收到啦"
        if (currentSession != null && currentSession.isActive()) {
            currentSession.addEntry(content);
            logger.info("记录日记条目：{}", content);
            
            // 立即保存会话到文件，防止数据丢失
            try {
                diaryRepository.saveSession(currentSession);
                logger.info("日记会话已立即保存，当前记录数：{}", currentSession.getEntryCount());
            } catch (Exception e) {
                logger.error("保存日记会话失败，但继续处理：{}", e.getMessage());
                // 即使保存失败也继续处理，避免影响用户体验
            }
            
            return FIXED_RESPONSE;
        } else {
            // 如果没有活跃会话，直接回复"收到啦"，避免"No command found"错误
            logger.info("直接输入内容：{}", content);
            return FIXED_RESPONSE;
        }
    }
    
    /**
     * 记录日记条目
     * @param content 日记内容
     * @return 固定响应文本
     */
    private String recordDiaryEntry(String content) {
        currentSession.addEntry(content);
        logger.info("记录日记条目：{}", content);
        return FIXED_RESPONSE;
    }
    
    /**
     * 结束日记会话
     * @return 模型生成的日记总结
     */
    private String endDiarySession() {
        // 检查是否有活跃会话
        if (currentSession == null || !currentSession.isActive()) {
            return "当前没有活跃的日记会话，请先输入'开始日记'";
        }
        
        // 检查是否有日记记录
        if (currentSession.getEntryCount() == 0) {
            currentSession.endSession();
            currentSession = null;
            // 返回特殊标识，让前端知道不需要显示总结
            return "NO_CONTENT";
        }
        
        try {
            logger.info("结束日记会话，开始调用大模型生成总结，记录数量：{}", currentSession.getEntryCount());
            
            // 构建Prompt
            String prompt = promptBuilder.buildDiaryProcessingPrompt(currentSession.getEntryContents());
            
            // 调用大模型
            String modelResponse = ollamaService.generateContent(prompt);
            
            // 解析模型响应（简化处理，实际应用中可能需要更复杂的解析）
            parseModelResponse(modelResponse);
            
            // 结束会话并保存
            currentSession.endSession();
            diaryRepository.saveSession(currentSession);
            
            logger.info("日记会话结束并保存成功");
            
            // 返回模型生成的完整内容
            return buildFinalOutput();
            
        } catch (Exception e) {
            logger.error("结束日记会话时发生错误", e);
            currentSession.endSession();
            currentSession = null;
            return "处理日记时发生错误：" + e.getMessage();
        }
    }
    
    /**
     * 解析模型响应
     * @param modelResponse 模型响应内容
     */
    private void parseModelResponse(String modelResponse) {
        // 使用正确的分隔符进行解析
        String[] sections = modelResponse.split("=== ");
        
        if (sections.length >= 4) { // 因为有标题行，所以至少需要4个部分
            currentSession.setGeneratedDiary(extractSection(sections[1], "完整版日记"));
            currentSession.setSummary(extractSection(sections[2], "五点条列总结"));
            currentSession.setInsights(extractSection(sections[3], "洞察与建议"));
        } else {
            // 如果解析失败，尝试更智能的解析
            parseModelResponseIntelligently(modelResponse);
        }
    }
    
    /**
     * 智能解析模型响应
     * @param modelResponse 模型响应内容
     */
    private void parseModelResponseIntelligently(String modelResponse) {
        // 尝试按行解析
        String[] lines = modelResponse.split("\n");
        StringBuilder diary = new StringBuilder();
        StringBuilder summary = new StringBuilder();
        StringBuilder insights = new StringBuilder();
        
        String currentSection = "";
        
        for (String line : lines) {
            if (line.contains("完整版日记")) {
                currentSection = "diary";
                continue;
            } else if (line.contains("五点条列总结")) {
                currentSection = "summary";
                continue;
            } else if (line.contains("洞察与建议")) {
                currentSection = "insights";
                continue;
            }
            
            // 跳过空行和分隔符
            if (line.trim().isEmpty() || line.startsWith("===")) {
                continue;
            }
            
            // 根据当前部分添加内容
            switch (currentSection) {
                case "diary":
                    diary.append(line).append("\n");
                    break;
                case "summary":
                    summary.append(line).append("\n");
                    break;
                case "insights":
                    insights.append(line).append("\n");
                    break;
                default:
                    // 如果没有识别到部分，默认添加到完整版日记
                    diary.append(line).append("\n");
                    break;
            }
        }
        
        // 设置解析结果
        String diaryContent = diary.toString().trim();
        String summaryContent = summary.toString().trim();
        String insightsContent = insights.toString().trim();
        
        currentSession.setGeneratedDiary(diaryContent.isEmpty() ? "无内容" : diaryContent);
        currentSession.setSummary(summaryContent.isEmpty() ? "无总结内容" : summaryContent);
        currentSession.setInsights(insightsContent.isEmpty() ? "无洞察建议" : insightsContent);
    }
    
    /**
     * 提取特定部分的内容
     */
    private String extractSection(String text, String sectionName) {
        String[] lines = text.split("\n");
        StringBuilder content = new StringBuilder();
        boolean inSection = false;
        
        for (String line : lines) {
            if (line.contains(sectionName)) {
                inSection = true;
                continue;
            }
            if (inSection && line.trim().isEmpty() && content.length() > 0) {
                break; // 遇到空行且已有内容时结束
            }
            if (inSection && !line.startsWith("===")) {
                content.append(line).append("\n");
            }
        }
        
        String result = content.toString().trim();
        return result.isEmpty() ? "无内容" : result;
    }
    
    /**
     * 构建最终输出
     * @return 格式化后的完整输出
     */
    private String buildFinalOutput() {
        StringBuilder output = new StringBuilder();
        output.append("📝 今日日记总结\n\n");
        
        output.append("📖 完整版日记：\n");
        output.append(currentSession.getGeneratedDiary()).append("\n\n");
        
        output.append("📋 五点条列总结：\n");
        output.append(currentSession.getSummary()).append("\n\n");
        
        output.append("💡 洞察与建议：\n");
        output.append(currentSession.getInsights());
        
        return output.toString();
    }
    
    /**
     * 获取当前会话状态
     * @return 会话状态信息
     */
    public String getSessionStatus() {
        if (currentSession == null) {
            return "当前没有活跃的日记会话";
        }
        
        if (currentSession.isActive()) {
            return String.format("日记记录中，已记录%d条内容", currentSession.getEntryCount());
        } else {
            return "日记会话已结束";
        }
    }
    
    /**
     * 获取当前活跃的会话
     * @return 当前会话
     */
    public DiarySession getCurrentSession() {
        return currentSession;
    }
    
    /**
     * 检查Ollama服务是否可用
     * @return 是否可用
     */
    public boolean isOllamaAvailable() {
        return ollamaService.isServiceAvailable();
    }
}