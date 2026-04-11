package com.example.women;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView txtHome;
    TextView txtSOS;
    TextView txtProfile;
    Button btnSafeRoutes, btnContacts, btnHistory, btnTips;
    ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Bottom nav
        txtHome = findViewById(R.id.txt_home);
        txtSOS = findViewById(R.id.txt_sos);
        txtProfile = findViewById(R.id.txt_profile);

        // Buttons
        btnSafeRoutes = findViewById(R.id.btn_safe_routes);
        btnContacts = findViewById(R.id.btn_contacts);
        btnHistory = findViewById(R.id.btn_history);
        btnTips = findViewById(R.id.btn_tips);

        btnMenu = findViewById(R.id.btn_menu);

        // ✅ Navigation

        txtHome.setOnClickListener(v -> {
            // already here
        });

        txtSOS.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        txtProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));

        // ✅ FEATURE BUTTONS

        btnSafeRoutes.setOnClickListener(v ->
                startActivity(new Intent(this, SafeRoutesActivity.class)));

        btnContacts.setOnClickListener(v ->
                startActivity(new Intent(this, ContactsActivity.class)));

        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        btnTips.setOnClickListener(v ->
                startActivity(new Intent(this, TipsActivity.class)));
    }
}