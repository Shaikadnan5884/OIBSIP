package com.example.quiz_application;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java.io.Serializable;
import java.util.List;

@Entity(tableName = "quizzes")
public class Quiz implements Serializable {
    @PrimaryKey
    @NonNull
    private String accessCode;
    private String title;
    private String category;
    private String createdBy;
    
    @TypeConverters(DataConverter.class)
    private List<Question> questions;

    public Quiz(String title, @NonNull String accessCode, String category, List<Question> questions, String createdBy) {
        this.title = title;
        this.accessCode = accessCode;
        this.category = category;
        this.questions = questions;
        this.createdBy = createdBy;
    }

    public String getTitle() { return title; }
    public String getAccessCode() { return accessCode; }
    public String getCategory() { return category; }
    public List<Question> getQuestions() { return questions; }
    public String getCreatedBy() { return createdBy; }

    public void setAccessCode(@NonNull String accessCode) { this.accessCode = accessCode; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
