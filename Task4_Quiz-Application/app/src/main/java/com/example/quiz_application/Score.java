package com.example.quiz_application;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "leaderboard")
public class Score {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String playerName;
    public String quizTitle;
    public int score;
    public int totalQuestions;
    public long timestamp;

    public Score(String playerName, String quizTitle, int score, int totalQuestions, long timestamp) {
        this.playerName = playerName;
        this.quizTitle = quizTitle;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timestamp = timestamp;
    }
}
