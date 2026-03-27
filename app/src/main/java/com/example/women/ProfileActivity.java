package com.example.women;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btnLogout = findViewById(R.id.btnLogout);

        // 🔥 Safe data handling (NO CRASH)
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");

        if (name != null)
            tvName.setText(name);
        else
            tvName.setText("User");

        if (email != null)
            tvEmail.setText(email);
        else
            tvEmail.setText("No Email");

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this,
                    LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        });
    }
}