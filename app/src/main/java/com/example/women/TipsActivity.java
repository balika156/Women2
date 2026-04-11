package com.example.women;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TipsActivity extends AppCompatActivity {

    Button btnCall1091, btnCall100, btnCall1098;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        btnCall1091 = findViewById(R.id.btnCall1091);
        btnCall100 = findViewById(R.id.btnCall100);
        btnCall1098 = findViewById(R.id.btnCall1098);

        btnCall1091.setOnClickListener(v -> openDialer("1091"));
        btnCall100.setOnClickListener(v -> openDialer("100"));
        btnCall1098.setOnClickListener(v -> openDialer("1098"));
    }

    private void openDialer(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }
}