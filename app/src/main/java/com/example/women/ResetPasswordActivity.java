package com.example.women;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText edtNewPassword;
    Button btnReset;

    DatabaseReference databaseReference;
    String userEmail; // login activity कडून येणार

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnReset = findViewById(R.id.btnReset);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // 📩 Email get from Intent
        userEmail = getIntent().getStringExtra("email");

        btnReset.setOnClickListener(v -> {

            String newPassword = edtNewPassword.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword)) {
                edtNewPassword.setError("Enter new password");
                return;
            }

            // 🔥 Firebase update logic
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                        String dbEmail = userSnapshot.child("email").getValue(String.class);

                        if (userEmail.equals(dbEmail)) {

                            userSnapshot.getRef().child("password").setValue(newPassword);

                            Toast.makeText(ResetPasswordActivity.this,
                                    "Password Updated Successfully",
                                    Toast.LENGTH_SHORT).show();

                            finish(); // back to login
                            return;
                        }
                    }

                    Toast.makeText(ResetPasswordActivity.this,
                            "User not found",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(ResetPasswordActivity.this,
                            "Error updating password",
                            Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}