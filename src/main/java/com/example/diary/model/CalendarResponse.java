package com.example.diary.model;

import java.util.List;

public class CalendarResponse {
    private List<String> diaryDates; // 有日记的日期列表
    private DiarySession selectedDiary; // 选中的日记内容
    
    public CalendarResponse() {}
    
    public CalendarResponse(List<String> diaryDates, DiarySession selectedDiary) {
        this.diaryDates = diaryDates;
        this.selectedDiary = selectedDiary;
    }
    
    // Getters and Setters
    public List<String> getDiaryDates() {
        return diaryDates;
    }
    
    public void setDiaryDates(List<String> diaryDates) {
        this.diaryDates = diaryDates;
    }
    
    public DiarySession getSelectedDiary() {
        return selectedDiary;
    }
    
    public void setSelectedDiary(DiarySession selectedDiary) {
        this.selectedDiary = selectedDiary;
    }
}