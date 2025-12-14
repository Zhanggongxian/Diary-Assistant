package com.example.diary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 日记条目模型
 * 表示用户输入的单个日记记录
 */
public class DiaryEntry {
    
    /**
     * 记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * 用户输入的原始内容
     */
    private String content;
    
    /**
     * 默认构造函数（用于JSON反序列化）
     */
    public DiaryEntry() {
    }
    
    /**
     * 带参数的构造函数
     * @param content 日记内容
     */
    public DiaryEntry(String content) {
        this.timestamp = LocalDateTime.now();
        this.content = content;
    }
    
    /**
     * 带时间戳的构造函数
     * @param timestamp 记录时间
     * @param content 日记内容
     */
    public DiaryEntry(LocalDateTime timestamp, String content) {
        this.timestamp = timestamp;
        this.content = content;
    }
    
    // Getter和Setter方法
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return "DiaryEntry{" +
                "timestamp=" + timestamp +
                ", content='" + content + '\'' +
                '}';
    }
}