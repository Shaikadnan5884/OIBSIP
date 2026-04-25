package com.example.quiz_application;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView, resultTextView, timerTextView;
    private RadioGroup optionsRadioGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton, restartButton, viewLeaderboardButton, detailedAnalyticsButton;
    private ProgressBar progressBar;
    private LinearLayout resultLayout;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String quizTitle = "Default Quiz";
    private String userName = "Anonymous";

    private CountDownTimer countDownTimer;
    private static final long TIMER_DURATION = 15000; // 15 seconds

    private List<QuizResult.QuestionAnswer> userAnswers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize UI components
        questionTextView = findViewById(R.id.questionTextView);
        timerTextView = findViewById(R.id.timerTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.progressBar);
        resultLayout = findViewById(R.id.resultLayout);
        resultTextView = findViewById(R.id.resultTextView);
        restartButton = findViewById(R.id.restartButton);
        viewLeaderboardButton = findViewById(R.id.viewLeaderboardButton);
        detailedAnalyticsButton = findViewById(R.id.detailedAnalyticsButton);

        // Get Data from Intent
        Quiz quiz = (Quiz) getIntent().getSerializableExtra("quiz");
        userName = getIntent().getStringExtra("user_name");
        if (userName == null) userName = "Anonymous";

        if (quiz != null) {
            questionList = quiz.getQuestions();
            quizTitle = quiz.getTitle();
            setTitle(quizTitle);
        }

        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, "Error loading quiz", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayQuestion();

        nextButton.setOnClickListener(v -> {
            int selectedId = optionsRadioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(MainActivity.this, R.string.please_select_an_answer, Toast.LENGTH_SHORT).show();
                return;
            }

            if (countDownTimer != null) countDownTimer.cancel();
            recordAnswer(selectedId);
            moveToNextQuestion();
        });

        restartButton.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            score = 0;
            userAnswers.clear();
            resultLayout.setVisibility(View.GONE);
            displayQuestion();
        });

        viewLeaderboardButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
        });

        detailedAnalyticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("quiz_result", new QuizResult(quizTitle, userAnswers, score));
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        
        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(getString(R.string.timer_text, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                vibrate(200);
                Toast.makeText(MainActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                
                // Record unanswered
                Question q = questionList.get(currentQuestionIndex);
                userAnswers.add(new QuizResult.QuestionAnswer(q.getQuestion(), 
                        q.getOptions()[q.getCorrectIndex()], "No Answer", false));
                
                moveToNextQuestion();
            }
        }.start();
    }

    private void moveToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            displayQuestion();
        } else {
            showResult();
        }
    }

    private void displayQuestion() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionTextView.setText(currentQuestion.getQuestion());
        String[] options = currentQuestion.getOptions();
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);
        option4.setText(options[3]);

        optionsRadioGroup.clearCheck();
        
        progressBar.setMax(questionList.size());
        progressBar.setProgress(currentQuestionIndex + 1);
        
        startTimer();
    }

    private void recordAnswer(int selectedId) {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        int answerIndex = -1;
        if (selectedId == R.id.option1) answerIndex = 0;
        else if (selectedId == R.id.option2) answerIndex = 1;
        else if (selectedId == R.id.option3) answerIndex = 2;
        else if (selectedId == R.id.option4) answerIndex = 3;

        boolean isCorrect = (answerIndex == currentQuestion.getCorrectIndex());
        if (isCorrect) {
            score++;
        } else {
            vibrate(100);
        }

        String selectedText = (answerIndex != -1) ? currentQuestion.getOptions()[answerIndex] : "None";
        userAnswers.add(new QuizResult.QuestionAnswer(currentQuestion.getQuestion(), 
                currentQuestion.getOptions()[currentQuestion.getCorrectIndex()], 
                selectedText, isCorrect));
    }

    private void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(duration);
            }
        }
    }

    private void showResult() {
        if (countDownTimer != null) countDownTimer.cancel();
        resultLayout.setVisibility(View.VISIBLE);
        resultTextView.setText(getString(R.string.your_score, score, questionList.size()));
        
        // Save to Local Leaderboard
        new Thread(() -> {
            AppDatabase.getInstance(this).scoreDao().insert(
                    new Score(userName, quizTitle, score, questionList.size(), System.currentTimeMillis())
            );
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
