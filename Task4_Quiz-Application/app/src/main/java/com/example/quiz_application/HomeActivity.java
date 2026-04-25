package com.example.quiz_application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private EditText userNameEditText, accessCodeEditText;
    private Button createQuizButton, joinQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userNameEditText = findViewById(R.id.userNameEditText);
        accessCodeEditText = findViewById(R.id.accessCodeEditText);
        createQuizButton = findViewById(R.id.createQuizButton);
        joinQuizButton = findViewById(R.id.joinQuizButton);

        createQuizButton.setOnClickListener(v -> {
            String userName = userNameEditText.getText().toString().trim();
            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter your name first", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(HomeActivity.this, CreateQuizActivity.class);
            intent.putExtra("user_name", userName);
            startActivity(intent);
        });

        joinQuizButton.setOnClickListener(v -> {
            String userName = userNameEditText.getText().toString().trim();
            String code = accessCodeEditText.getText().toString().trim();
            
            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter your name first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (code.isEmpty()) {
                Toast.makeText(this, "Please enter an access code", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                Quiz quiz = AppDatabase.getInstance(HomeActivity.this).quizDao().getQuizByCode(code);
                runOnUiThread(() -> {
                    if (quiz != null) {
                        startQuiz(quiz, userName);
                    } else {
                        Toast.makeText(HomeActivity.this, "Quiz not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }

    private void startQuiz(Quiz quiz, String userName) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.putExtra("quiz", quiz);
        intent.putExtra("user_name", userName);
        startActivity(intent);
    }
}
