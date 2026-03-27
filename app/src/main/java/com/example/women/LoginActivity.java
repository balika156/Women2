package com.example.women;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView txtRegister;

    SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "UsersPrefs";
    private static final String USERS_KEY = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

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

            try {
                JSONArray usersArray =
                        new JSONArray(sharedPreferences.getString(USERS_KEY, "[]"));

                boolean isFound = false;
                String name = "";

                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject user = usersArray.getJSONObject(i);

                    if (user.getString("email").equals(email) &&
                            user.getString("password").equals(password)) {

                        isFound = true;
                        name = user.getString("name");
                        break;
                    }
                }

                if (isFound) {

                    Toast.makeText(this,
                            "Login Successful",
                            Toast.LENGTH_SHORT).show();

                    // ✅ HOME ACTIVITY ला जा (IMPORTANT FIX)
                    Intent intent = new Intent(LoginActivity.this,
                            HomeActivity.class);

                    intent.putExtra("name", name);
                    intent.putExtra("email", email);

                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(this,
                            "Invalid Credentials",
                            Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        txtRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this,
                        RegisterActivity.class)));
    }
}