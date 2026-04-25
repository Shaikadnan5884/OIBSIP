package com.example.quiz_application;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert
    void insert(Score score);

    @Query("SELECT * FROM leaderboard ORDER BY score DESC, timestamp DESC")
    List<Score> getAllScores();
}
