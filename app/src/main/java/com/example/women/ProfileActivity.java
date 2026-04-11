package com.example.women;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_profile);

            TextView tvName = findViewById(R.id.tvName);
            TextView tvEmail = findViewById(R.id.tvEmail);
            TextView tvPhone = findViewById(R.id.tvPhone);
            TextView tvEmergency = findViewById(R.id.tvEmergency);
            Button btnLogout = findViewById(R.id.btnLogout);

            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);

            if (tvName != null)
                tvName.setText("Name: " + sp.getString("name", "N/A"));

            if (tvEmail != null)
                tvEmail.setText("Email: " + sp.getString("email", "N/A"));

            if (tvPhone != null)
                tvPhone.setText("Phone: " + sp.getString("phone", "N/A"));

            if (tvEmergency != null)
                tvEmergency.setText("Emergency: " + sp.getString("emergency", "N/A"));

            btnLogout.setOnClickListener(v -> {
                sp.edit().clear().apply();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });

        } catch (Exception e) {
            Log.e("PROFILE_ERROR", e.toString());
        }
    }
}