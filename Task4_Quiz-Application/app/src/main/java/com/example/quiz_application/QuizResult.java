package com.example.quiz_application;

import java.io.Serializable;
import java.util.List;

public class QuizResult implements Serializable {
    private List<QuestionAnswer> userAnswers;
    private int score;
    private String quizTitle;

    public QuizResult(String quizTitle, List<QuestionAnswer> userAnswers, int score) {
        this.quizTitle = quizTitle;
        this.userAnswers = userAnswers;
        this.score = score;
    }

    public List<QuestionAnswer> getUserAnswers() { return userAnswers; }
    public int getScore() { return score; }
    public String getQuizTitle() { return quizTitle; }

    public static class QuestionAnswer implements Serializable {
        private String question;
        private String correctAnswer;
        private String selectedAnswer;
        private boolean isCorrect;

        public QuestionAnswer(String question, String correctAnswer, String selectedAnswer, boolean isCorrect) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.selectedAnswer = selectedAnswer;
            this.isCorrect = isCorrect;
        }

        public String getQuestion() { return question; }
        public String getCorrectAnswer() { return correctAnswer; }
        public String getSelectedAnswer() { return selectedAnswer; }
        public boolean isCorrect() { return isCorrect; }
    }
}
