package com.example.diary.service;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Prompt构建器
 * 负责构建符合要求的模型提示词
 */
@Component
public class PromptBuilder {
    
    /**
     * 日记助手规则Prompt（必须写成Java字符串常量）
     */
    private static final String DIARY_ASSISTANT_RULES = """
        你将作为我的长期日记助手，严格遵循以下规则：
        
        1. 角色定位
           - 你是我的日记整理助手
           - 你的任务是帮我整理和总结日记内容
        
        2. 内容处理原则
           - 严格基于我提供的原始记录进行整理
           - 不得添加、删除或修改任何事实性内容
           - 保持内容的客观性和真实性
        
        3. 输出格式要求
           - 完整版日记
           - 五点条列总结
           - 洞察与建议

        4. 禁止行为
           - 禁止无中生有
         - 禁止心理诊断 
          - 禁止模板化鸡汤
        """;
    
    /**
     * 构建完整的日记处理Prompt
     * @param entries 当天的日记条目内容列表
     * @return 完整的Prompt字符串
     */
    public String buildDiaryProcessingPrompt(List<String> entries) {
        if (entries == null || entries.isEmpty()) {
            throw new IllegalArgumentException("日记条目不能为空");
        }
        
        StringBuilder promptBuilder = new StringBuilder();
        
        // 添加系统指令
        promptBuilder.append(DIARY_ASSISTANT_RULES).append("\n\n");
        
        // 添加原始记录
        promptBuilder.append("以下是我今天的全部原始记录（按时间顺序）：\n");
        for (int i = 0; i < entries.size(); i++) {
            promptBuilder.append(i + 1).append(".  ").append(entries.get(i)).append("\n");
        }
        
        // 添加任务指令
        promptBuilder.append("\n现在请根据规则，完成：\n");
        promptBuilder.append("1）完整版日记\n");
        promptBuilder.append("2）五点条列总结\n");
        promptBuilder.append("3）洞察与建议\n");
        
        // 添加严格的输出格式要求
        promptBuilder.append("\n请严格按照以下格式输出，不要添加任何额外内容：\n");
        promptBuilder.append("=== 完整版日记 ===\n");
        promptBuilder.append("[将当日全部记录整合为一篇结构清晰、表达更成熟、语言自然流畅的日记。不得添加任何我未提及的事实、行为或情绪。不得拔高、不合理美化，也不得弱化原有感受。保留第一人称与真实语气。]\n\n");
        promptBuilder.append("=== 五点条列总结 ===\n");
        promptBuilder.append("1. [今天发生了什么 / 我的整体状态]\n");
        promptBuilder.append("2. [今天的灵感或重要想法]\n");
        promptBuilder.append("3. [我表达过的积极愿望]\n");
        promptBuilder.append("4. [今天值得感谢的事]\n");
        promptBuilder.append("5. [未完成或隐含的待办事项]\n\n");
        promptBuilder.append("=== 洞察与建议 ===\n");
        promptBuilder.append("[以心理学专业视角 + 人生导师式语气进行分析。重点放在：情绪模式、行为倾向、内在需求或潜在压力。提供温和、具体、不说教的建议或鼓励。不进行病理化判断，不贴标签。]\n\n");
        promptBuilder.append("重要提示：请确保每个部分之间用空行分隔，不要重复标题，严格按照格式输出。");
        
        return promptBuilder.toString();
    }
    
    /**
     * 获取日记助手规则（用于调试或显示）
     * @return 日记助手规则
     */
    public String getDiaryAssistantRules() {
        return DIARY_ASSISTANT_RULES;
    }
}