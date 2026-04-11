package com.example.women;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView txtRegister, txtForgetPassword;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgetPassword = findViewById(R.id.txtForgetPassword);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // 🔐 LOGIN
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

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    boolean isFound = false;
                    String name = "", phone = "", emergency = "";

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                        String dbEmail = userSnapshot.child("email").getValue(String.class);
                        String dbPassword = userSnapshot.child("password").getValue(String.class);

                        if (email.equals(dbEmail) && password.equals(dbPassword)) {

                            isFound = true;
                            name = userSnapshot.child("name").getValue(String.class);
                            phone = userSnapshot.child("phone").getValue(String.class);
                            emergency = userSnapshot.child("emergency").getValue(String.class);
                            break;
                        }
                    }

                    if (isFound) {

                        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("phone", phone);
                        editor.putString("emergency", emergency);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // 🔥 FORGOT PASSWORD WORKING
        txtForgetPassword.setOnClickListener(v -> {

            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show(); // test

            String email = etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Enter Email first");
                return;
            }

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    boolean found = false;

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                        String dbEmail = userSnapshot.child("email").getValue(String.class);

                        if (email.equals(dbEmail)) {

                            found = true;
                            String password = userSnapshot.child("password").getValue(String.class);

                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Your Password")
                                    .setMessage("Password: " + password)
                                    .setPositiveButton("OK", null)
                                    .show();

                            break;
                        }
                    }

                    if (!found) {
                        Toast.makeText(LoginActivity.this,
                                "Email not found",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(LoginActivity.this,
                            "Error",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        txtRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
}