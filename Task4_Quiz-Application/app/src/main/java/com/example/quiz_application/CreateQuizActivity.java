package com.example.quiz_application;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CreateQuizActivity extends AppCompatActivity {

    private EditText quizTitleEditText, quizCodeEditText;
    private EditText newQuestionEditText, newOption1EditText, newOption2EditText, newOption3EditText, newOption4EditText;
    private RadioGroup correctOptionRadioGroup;
    private Spinner categorySpinner;
    private Button addQuestionButton, saveQuizButton;
    private TextView questionCountTextView;

    private List<Question> tempQuestions = new ArrayList<>();
    private String userName = "Anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        userName = getIntent().getStringExtra("user_name");
        if (userName == null) userName = "Anonymous";

        quizTitleEditText = findViewById(R.id.quizTitleEditText);
        quizCodeEditText = findViewById(R.id.quizCodeEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        newQuestionEditText = findViewById(R.id.newQuestionEditText);
        newOption1EditText = findViewById(R.id.newOption1EditText);
        newOption2EditText = findViewById(R.id.newOption2EditText);
        newOption3EditText = findViewById(R.id.newOption3EditText);
        newOption4EditText = findViewById(R.id.newOption4EditText);
        correctOptionRadioGroup = findViewById(R.id.correctOptionRadioGroup);
        addQuestionButton = findViewById(R.id.addQuestionButton);
        saveQuizButton = findViewById(R.id.saveQuizButton);
        questionCountTextView = findViewById(R.id.questionCountTextView);

        // Setup Category Spinner
        String[] categories = {"General Knowledge", "Science", "History", "Sports", "Technology"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        addQuestionButton.setOnClickListener(v -> addQuestion());
        saveQuizButton.setOnClickListener(v -> saveQuiz());
    }

    private void addQuestion() {
        String questionText = newQuestionEditText.getText().toString().trim();
        String o1 = newOption1EditText.getText().toString().trim();
        String o2 = newOption2EditText.getText().toString().trim();
        String o3 = newOption3EditText.getText().toString().trim();
        String o4 = newOption4EditText.getText().toString().trim();
        int selectedId = correctOptionRadioGroup.getCheckedRadioButtonId();

        if (questionText.isEmpty() || o1.isEmpty() || o2.isEmpty() || o3.isEmpty() || o4.isEmpty() || selectedId == -1) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int correctIndex = 0;
        if (selectedId == R.id.correctOption1) correctIndex = 0;
        else if (selectedId == R.id.correctOption2) correctIndex = 1;
        else if (selectedId == R.id.correctOption3) correctIndex = 2;
        else if (selectedId == R.id.correctOption4) correctIndex = 3;

        Question q = new Question(questionText, new String[]{o1, o2, o3, o4}, correctIndex);
        tempQuestions.add(q);

        // Reset fields
        newQuestionEditText.setText("");
        newOption1EditText.setText("");
        newOption2EditText.setText("");
        newOption3EditText.setText("");
        newOption4EditText.setText("");
        correctOptionRadioGroup.clearCheck();

        questionCountTextView.setText("Add Question " + (tempQuestions.size() + 1));
        Toast.makeText(this, "Question added!", Toast.LENGTH_SHORT).show();
    }

    private void saveQuiz() {
        String title = quizTitleEditText.getText().toString().trim();
        String code = quizCodeEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (title.isEmpty() || code.isEmpty() || tempQuestions.isEmpty()) {
            Toast.makeText(this, "Title, Code and questions required", Toast.LENGTH_SHORT).show();
            return;
        }

        Quiz newQuiz = new Quiz(title, code, category, tempQuestions, userName);
        
        // Save locally to Room
        new Thread(() -> {
            AppDatabase.getInstance(this).quizDao().insert(newQuiz);
            runOnUiThread(() -> {
                Toast.makeText(CreateQuizActivity.this, "Quiz Created by " + userName, Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
