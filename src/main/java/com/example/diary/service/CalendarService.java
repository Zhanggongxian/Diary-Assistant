package com.example.diary.service;

import com.example.diary.model.DiarySession;
import com.example.diary.persistence.FileDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    
    private final FileDiaryRepository diaryRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter fileDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Autowired
    public CalendarService(FileDiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }
    
    /**
     * 获取所有有日记的日期列表
     */
    public List<String> getDiaryDates() {
        List<String> diaryDates = new ArrayList<>();
        
        try {
            // 使用FileDiaryRepository的数据目录
            String dataDirectory = diaryRepository.getDataDirectory();
            Path dataPath = Paths.get(dataDirectory);
            if (Files.exists(dataPath) && Files.isDirectory(dataPath)) {
                // 遍历data目录下的所有日记文件
                Files.list(dataPath)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        // 提取日期部分：diary_2025-12-14.json -> 2025-12-14
                        if (fileName.startsWith("diary_")) {
                            String dateStr = fileName.substring(6, fileName.length() - 5);
                            try {
                                LocalDate date = LocalDate.parse(dateStr, fileDateFormatter);
                                // 使用FileDiaryRepository检查日记是否存在
                                if (diaryRepository.sessionExists(date)) {
                                    diaryDates.add(dateStr);
                                }
                            } catch (Exception e) {
                                // 日期格式不合法，跳过
                            }
                        }
                    });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return diaryDates.stream().sorted().collect(Collectors.toList());
    }
    
    /**
     * 根据日期获取日记内容
     */
    public DiarySession getDiaryByDate(String date) {
        try {
            // 使用注入的FileDiaryRepository来加载日记
            return diaryRepository.loadSession(java.time.LocalDate.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 检查指定日期是否有日记
     */
    public boolean hasDiary(String date) {
        return getDiaryDates().contains(date);
    }
}