package com.example.diary.model;

/**
 * Ollama API 请求模型
 */
public class OllamaRequest {
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 提示词
     */
    private String prompt;
    
    /**
     * 是否流式输出
     */
    private boolean stream;
    
    /**
     * 默认构造函数
     */
    public OllamaRequest() {
    }
    
    /**
     * 带参数的构造函数
     * @param model 模型名称
     * @param prompt 提示词
     * @param stream 是否流式输出
     */
    public OllamaRequest(String model, String prompt, boolean stream) {
        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
    }
    
    // Getter和Setter方法
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getPrompt() {
        return prompt;
    }
    
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    
    public boolean isStream() {
        return stream;
    }
    
    public void setStream(boolean stream) {
        this.stream = stream;
    }
    
    @Override
    public String toString() {
        return "OllamaRequest{" +
                "model='" + model + '\'' +
                ", prompt='" + prompt + '\'' +
                ", stream=" + stream +
                '}';
    }
}