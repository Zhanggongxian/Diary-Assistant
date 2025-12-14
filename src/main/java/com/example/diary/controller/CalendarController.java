package com.example.diary.controller;

import com.example.diary.model.CalendarResponse;
import com.example.diary.model.DiarySession;
import com.example.diary.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    
    @Autowired
    private CalendarService calendarService;
    
    /**
     * 获取所有有日记的日期列表
     */
    @GetMapping("/dates")
    public ResponseEntity<List<String>> getDiaryDates() {
        List<String> diaryDates = calendarService.getDiaryDates();
        return ResponseEntity.ok(diaryDates);
    }
    
    /**
     * 根据日期获取日记内容
     */
    @GetMapping("/diary/{date}")
    public ResponseEntity<CalendarResponse> getDiaryByDate(@PathVariable String date) {
        DiarySession diary = calendarService.getDiaryByDate(date);
        List<String> diaryDates = calendarService.getDiaryDates();
        
        CalendarResponse response = new CalendarResponse(diaryDates, diary);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 检查指定日期是否有日记
     */
    @GetMapping("/hasDiary/{date}")
    public ResponseEntity<Boolean> hasDiary(@PathVariable String date) {
        boolean hasDiary = calendarService.hasDiary(date);
        return ResponseEntity.ok(hasDiary);
    }
}