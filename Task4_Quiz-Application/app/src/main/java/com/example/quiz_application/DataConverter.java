package com.example.quiz_application;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {
    @TypeConverter
    public String fromQuestionList(List<Question> questions) {
        if (questions == null) return null;
        Gson gson = new Gson();
        return gson.toJson(questions);
    }

    @TypeConverter
    public List<Question> toQuestionList(String questionsString) {
        if (questionsString == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Question>>() {}.getType();
        return gson.fromJson(questionsString, type);
    }
}
