package com.example.women;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword, edtPhone, edtEmergency;
    Button btnRegister;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmergency = findViewById(R.id.edtEmergency);
        btnRegister = findViewById(R.id.btnRegister);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btnRegister.setOnClickListener(v -> {

            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String emergency = edtEmergency.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                    || phone.isEmpty() || emergency.isEmpty()) {

                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = databaseReference.push().getKey();

            HashMap<String, String> userMap = new HashMap<>();
            userMap.put("name", name);
            userMap.put("email", email);
            userMap.put("password", password);
            userMap.put("phone", phone);
            userMap.put("emergency", emergency);

            databaseReference.child(userId).setValue(userMap)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            // 🔐 Save locally
                            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("phone", phone);
                            editor.putString("emergency", emergency);
                            editor.apply();

                            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(this, LoginActivity.class));
                            finish();

                        } else {
                            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}