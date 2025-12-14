package com.example.diary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.diary.model.OllamaRequest;
import com.example.diary.model.OllamaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Ollama服务类
 * 负责与本地大语言模型进行HTTP通信
 */
@Service
public class OllamaService {
    
    private static final Logger logger = LoggerFactory.getLogger(OllamaService.class);
    
    /**
     * Ollama API基础URL
     */
    @Value("${ollama.url:http://localhost:11434}")
    private String ollamaUrl;
    
    /**
     * 默认模型名称
     */
    @Value("${ollama.model:qwen2.5:7b}")
    private String defaultModel;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    /**
     * 构造函数
     */
    public OllamaService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 调用Ollama API生成内容
     * @param prompt 提示词
     * @return 模型生成的响应内容
     */
    public String generateContent(String prompt) {
        return generateContent(prompt, defaultModel);
    }
    
    /**
     * 调用Ollama API生成内容（指定模型）
     * @param prompt 提示词
     * @param model 模型名称
     * @return 模型生成的响应内容
     */
    public String generateContent(String prompt, String model) {
        try {
            logger.info("调用Ollama API，模型：{}，提示词长度：{}", model, prompt.length());
            
            // 构建请求
            OllamaRequest request = new OllamaRequest(model, prompt, false);
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<OllamaRequest> entity = new HttpEntity<>(request, headers);
            
            // 发送请求
            String apiUrl = ollamaUrl + "/api/generate";
            ResponseEntity<OllamaResponse> response = restTemplate.postForEntity(
                apiUrl, entity, OllamaResponse.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                OllamaResponse ollamaResponse = response.getBody();
                logger.info("Ollama API调用成功，响应长度：{}", 
                    ollamaResponse.getResponse() != null ? ollamaResponse.getResponse().length() : 0);
                
                return ollamaResponse.getResponse();
            } else {
                logger.error("Ollama API调用失败，状态码：{}", response.getStatusCode());
                throw new RuntimeException("Ollama API调用失败，状态码：" + response.getStatusCode());
            }
            
        } catch (Exception e) {
            logger.error("调用Ollama API时发生错误", e);
            throw new RuntimeException("调用Ollama API失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 检查Ollama服务是否可用
     * @return 是否可用
     */
    public boolean isServiceAvailable() {
        try {
            String healthUrl = ollamaUrl + "/api/tags";
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            logger.warn("Ollama服务不可用：{}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取Ollama服务URL
     * @return 服务URL
     */
    public String getOllamaUrl() {
        return ollamaUrl;
    }
    
    /**
     * 获取默认模型名称
     * @return 模型名称
     */
    public String getDefaultModel() {
        return defaultModel;
    }
}