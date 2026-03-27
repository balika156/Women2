package com.example.women;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public EditText etEmail, etPassword;
    Button btnLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.tvEmail);
        etPassword = findViewById(R.id.btnSOS);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Enter Email");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Enter Password");
                return;
            }

            if (email.equals("admin@gmail.com") &&
                    password.equals("123456")) {

                Toast.makeText(this,
                        "Login Successful",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(
                        LoginActivity.this,
                        ProfileActivity.class));

            } else {
                Toast.makeText(this,
                        "Invalid Credentials",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}