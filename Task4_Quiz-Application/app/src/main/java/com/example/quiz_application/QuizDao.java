package com.example.quiz_application;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Quiz quiz);

    @Query("SELECT * FROM quizzes WHERE accessCode = :code LIMIT 1")
    Quiz getQuizByCode(String code);

    @Query("SELECT * FROM quizzes")
    List<Quiz> getAllQuizzes();
}
