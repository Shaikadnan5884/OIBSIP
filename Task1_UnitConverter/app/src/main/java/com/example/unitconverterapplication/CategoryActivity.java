package com.example.unitconverterapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        setupCard(R.id.cardLength, "Length");
        setupCard(R.id.cardWeight, "Weight");
        setupCard(R.id.cardTemp, "Temperature");
        setupCard(R.id.cardArea, "Area");
        setupCard(R.id.cardVolume, "Volume");
        setupCard(R.id.cardSpeed, "Speed");
        setupCard(R.id.cardPressure, "Pressure");
        setupCard(R.id.cardPower, "Power");
        setupCard(R.id.cardNumberSystem, "NumberSystem");
    }

    private void setupCard(int id, final String category) {
        CardView card = findViewById(id);
        if (card != null) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                    intent.putExtra("CATEGORY", category);
                    startActivity(intent);
                }
            });
        }
    }
}