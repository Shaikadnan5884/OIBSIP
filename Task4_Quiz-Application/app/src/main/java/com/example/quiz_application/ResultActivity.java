package com.example.quiz_application;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Detailed Analytics");
        }

        QuizResult result = (QuizResult) getIntent().getSerializableExtra("quiz_result");
        if (result == null) {
            finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.analyticsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AnalyticsAdapter(result.getUserAnswers()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsAdapter.ViewHolder> {
        private final List<QuizResult.QuestionAnswer> answers;

        AnalyticsAdapter(List<QuizResult.QuestionAnswer> answers) {
            this.answers = answers;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            QuizResult.QuestionAnswer item = answers.get(position);
            holder.questionText.setText((position + 1) + ". " + item.getQuestion());
            
            String status = item.isCorrect() ? "[CORRECT] " : "[WRONG] ";
            String details = status + "Your: " + item.getSelectedAnswer() + " | Correct: " + item.getCorrectAnswer();
            holder.detailText.setText(details);
            
            holder.detailText.setTextColor(item.isCorrect() ? Color.parseColor("#41AB41") : Color.RED);
        }

        @Override
        public int getItemCount() {
            return answers.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView questionText, detailText;

            ViewHolder(View itemView) {
                super(itemView);
                questionText = itemView.findViewById(android.R.id.text1);
                detailText = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}
