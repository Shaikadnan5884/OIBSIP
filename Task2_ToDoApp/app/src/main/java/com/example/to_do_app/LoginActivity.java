package com.example.to_do_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etEmail, etPassword;
    TextInputLayout tilEmail, tilPassword;
    Button btnLogin;
    TextView tvRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- SESSION CHECK ---
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int savedUserId = pref.getInt("userId", -1);

        // If userId is found, user is already logged in
        if (savedUserId != -1) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close LoginActivity so user can't go back to it
            return;
        }
        // ---------------------

        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    String email = etEmail.getText().toString().trim();
                    String pass = etPassword.getText().toString().trim();

                    int userId = db.checkUser(email, pass);
                    if (userId != -1) {
                        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("userId", userId);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateData() {
        boolean isValid = true;
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Enter a valid email");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(pass)) {
            tilPassword.setError("Password is required");
            isValid = false;
        } else {
            tilPassword.setError(null);
        }

        return isValid;
    }
}
