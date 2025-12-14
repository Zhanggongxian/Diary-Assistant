package com.example.diary.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.example.diary.model.DiarySession;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 文件日记存储库
 * 负责日记数据的持久化存储
 */
@Repository
public class FileDiaryRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(FileDiaryRepository.class);
    
    /**
     * 数据存储目录
     */
    @Value("${diary.data.directory:./data}")
    private String dataDirectory;
    
    private final ObjectMapper objectMapper;
    
    /**
     * 构造函数
     */
    public FileDiaryRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.findAndRegisterModules();
    }
    
    /**
     * 保存日记会话
     * @param session 日记会话
     */
    public void saveSession(DiarySession session) {
        try {
            // 确保数据目录存在
            ensureDataDirectoryExists();
            
            // 生成文件名：diary_yyyy-MM-dd.json
            String fileName = "diary_" + session.getDate().format(DateTimeFormatter.ISO_DATE) + ".json";
            File file = new File(dataDirectory, fileName);
            
            // 写入JSON文件
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, session);
            
            logger.info("日记会话已保存：{}，包含{}条记录", fileName, session.getEntryCount());
            
        } catch (IOException e) {
            logger.error("保存日记会话失败", e);
            throw new RuntimeException("保存日记会话失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 根据日期加载日记会话
     * @param date 日期
     * @return 日记会话，如果不存在则返回null
     */
    public DiarySession loadSession(LocalDate date) {
        try {
            String fileName = "diary_" + date.format(DateTimeFormatter.ISO_DATE) + ".json";
            File file = new File(dataDirectory, fileName);
            
            if (!file.exists()) {
                return null;
            }
            
            DiarySession session = objectMapper.readValue(file, DiarySession.class);
            logger.info("日记会话已加载：{}，包含{}条记录", fileName, session.getEntryCount());
            
            return session;
            
        } catch (IOException e) {
            logger.error("加载日记会话失败", e);
            throw new RuntimeException("加载日记会话失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 检查指定日期的日记是否存在
     * @param date 日期
     * @return 是否存在
     */
    public boolean sessionExists(LocalDate date) {
        String fileName = "diary_" + date.format(DateTimeFormatter.ISO_DATE) + ".json";
        File file = new File(dataDirectory, fileName);
        return file.exists();
    }
    
    /**
     * 确保数据目录存在
     */
    private void ensureDataDirectoryExists() {
        try {
            Path path = Paths.get(dataDirectory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("创建数据目录：{}", dataDirectory);
            }
        } catch (IOException e) {
            logger.error("创建数据目录失败：{}", dataDirectory, e);
            throw new RuntimeException("创建数据目录失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 获取数据目录路径
     * @return 数据目录路径
     */
    public String getDataDirectory() {
        return dataDirectory;
    }
}