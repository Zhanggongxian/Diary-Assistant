package com.example.diary.model;

/**
 * Ollama API 响应模型
 */
public class OllamaResponse {
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 创建时间
     */
    private String created_at;
    
    /**
     * 响应内容
     */
    private String response;
    
    /**
     * 是否完成
     */
    private boolean done;
    
    /**
     * 默认构造函数
     */
    public OllamaResponse() {
    }
    
    // Getter和Setter方法
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getCreated_at() {
        return created_at;
    }
    
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public boolean isDone() {
        return done;
    }
    
    public void setDone(boolean done) {
        this.done = done;
    }
    
    @Override
    public String toString() {
        return "OllamaResponse{" +
                "model='" + model + '\'' +
                ", created_at='" + created_at + '\'' +
                ", response='" + response + '\'' +
                ", done=" + done +
                '}';
    }
}