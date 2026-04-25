package com.example.quiz_application;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Score.class, Quiz.class}, version = 2)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScoreDao scoreDao();
    public abstract QuizDao quizDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "quiz_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
