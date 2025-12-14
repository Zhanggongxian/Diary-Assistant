package com.example.diary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 日记会话模型
 * 表示一天的日记记录会话
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiarySession {
    
    /**
     * 会话日期（用于标识当天的日记）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    /**
     * 会话开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    /**
     * 会话结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    /**
     * 是否活跃（是否正在记录中）
     */
    private boolean active;
    
    /**
     * 当天的日记条目列表
     */
    private List<DiaryEntry> entries;
    
    /**
     * 模型生成的完整版日记
     */
    private String generatedDiary;
    
    /**
     * 模型生成的五点条列总结
     */
    private String summary;
    
    /**
     * 模型生成的洞察与建议
     */
    private String insights;
    
    /**
     * 日记条目数量（用于JSON序列化兼容性）
     */
    private int entryCount;
    
    /**
     * 日记条目内容列表（用于JSON序列化兼容性）
     */
    private List<String> entryContents;
    
    /**
     * 默认构造函数
     */
    public DiarySession() {
        this.entries = new ArrayList<>();
        this.active = false;
    }
    
    /**
     * 开始新的日记会话
     */
    public void startSession() {
        this.date = LocalDate.now();
        this.startTime = LocalDateTime.now();
        this.active = true;
        this.entries.clear();
        this.generatedDiary = null;
        this.summary = null;
        this.insights = null;
    }
    
    /**
     * 结束日记会话
     */
    public void endSession() {
        this.endTime = LocalDateTime.now();
        this.active = false;
    }
    
    /**
     * 添加日记条目
     * @param content 日记内容
     */
    public void addEntry(String content) {
        if (this.active) {
            this.entries.add(new DiaryEntry(content));
        }
    }
    
    /**
     * 获取所有日记条目的文本内容（按时间顺序）
     * @return 按时间顺序排列的日记内容列表
     */
    public List<String> getEntryContents() {
        List<String> contents = new ArrayList<>();
        for (DiaryEntry entry : entries) {
            contents.add(entry.getContent());
        }
        return contents;
    }
    
    /**
     * 获取日记条目的数量
     * @return 条目数量
     */
    public int getEntryCount() {
        return entries.size();
    }
    
    // Getter和Setter方法
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public List<DiaryEntry> getEntries() {
        return entries;
    }
    
    public void setEntries(List<DiaryEntry> entries) {
        this.entries = entries;
    }
    
    public String getGeneratedDiary() {
        return generatedDiary;
    }
    
    public void setGeneratedDiary(String generatedDiary) {
        this.generatedDiary = generatedDiary;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getInsights() {
        return insights;
    }
    
    public void setInsights(String insights) {
        this.insights = insights;
    }
    
    @Override
    public String toString() {
        return "DiarySession{" +
                "date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", active=" + active +
                ", entries=" + entries.size() +
                ", generatedDiary='" + (generatedDiary != null ? "[已生成]" : "[未生成]") + '\'' +
                '}';
    }
}