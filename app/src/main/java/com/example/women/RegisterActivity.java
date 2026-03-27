package com.example.women;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword, edtPhone, edtEmergency;
    Button btnRegister;
    SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "UsersPrefs";
    private static final String USERS_KEY = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bind views
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmergency = findViewById(R.id.edtEmergency);
        btnRegister = findViewById(R.id.btnRegister);

        // SharedPreferences init
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        btnRegister.setOnClickListener(v -> {

            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String emergency = edtEmergency.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                    || phone.isEmpty() || emergency.isEmpty()) {

                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Get existing users
                JSONArray usersArray =
                        new JSONArray(sharedPreferences.getString(USERS_KEY, "[]"));

                // Check duplicate email
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject user = usersArray.getJSONObject(i);
                    if (user.getString("email").equals(email)) {
                        Toast.makeText(this,
                                "This email is already registered!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Create new user
                JSONObject newUser = new JSONObject();
                newUser.put("name", name);
                newUser.put("email", email);
                newUser.put("password", password);
                newUser.put("phone", phone);
                newUser.put("emergency", emergency);

                usersArray.put(newUser);

                // Save to SharedPreferences
                sharedPreferences.edit()
                        .putString(USERS_KEY, usersArray.toString())
                        .apply();

                Toast.makeText(this,
                        "Registered successfully!",
                        Toast.LENGTH_SHORT).show();

                // 🔥 DIRECT HOME PAGE
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // back press -> app close

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,
                        "Registration failed!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
