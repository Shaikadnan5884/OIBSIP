package com.example.quiz_application;

import java.util.ArrayList;
import java.util.List;

public class QuizManager {
    private static List<Quiz> quizzes = new ArrayList<>();

    static {
        // Add a default quiz
        List<Question> defaultQuestions = new ArrayList<>();
        defaultQuestions.add(new Question("What is the capital of France?", 
                new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 2));
        defaultQuestions.add(new Question("Which planet is known as the Red Planet?", 
                new String[]{"Earth", "Mars", "Jupiter", "Saturn"}, 1));
        quizzes.add(new Quiz("General Knowledge", "GK123", "General", defaultQuestions, "Admin"));
    }

    public static void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
    }

    public static Quiz getQuizByCode(String code) {
        for (Quiz quiz : quizzes) {
            if (quiz.getAccessCode().equalsIgnoreCase(code)) {
                return quiz;
            }
        }
        return null;
    }
}
