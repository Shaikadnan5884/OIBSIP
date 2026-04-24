package com.example.to_do_app;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etUsername, etEmail, etPassword;
    TextInputLayout tilUsername, tilEmail, tilPassword;
    Button btnRegister;
    TextView tvLogin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etRegUsername);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        
        tilUsername = findViewById(R.id.tilRegUsername);
        tilEmail = findViewById(R.id.tilRegEmail);
        tilPassword = findViewById(R.id.tilRegPassword);
        
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    String user = etUsername.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String pass = etPassword.getText().toString().trim();

                    long val = db.addUser(user, email, pass);
                    if (val > 0) {
                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean validateData() {
        boolean isValid = true;
        String user = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(user)) {
            tilUsername.setError("Username is required");
            isValid = false;
        } else {
            tilUsername.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Enter a valid email address");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(pass)) {
            tilPassword.setError("Password is required");
            isValid = false;
        } else if (pass.length() < 6) {
            tilPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            tilPassword.setError(null);
        }

        return isValid;
    }
}
