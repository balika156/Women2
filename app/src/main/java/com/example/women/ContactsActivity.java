package com.example.women;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private LinearLayout contactList;
    private Button btnAddContact;

    // Simple in-memory contact list
    private ArrayList<Contact> contacts = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactList = findViewById(R.id.contactList);
        btnAddContact = findViewById(R.id.btnAddContact);

        // Sample default contact
        contacts.add(new Contact("ABC", "9876543210"));

        displayContacts();

        btnAddContact.setOnClickListener(v -> showAddContactDialog());
    }

    private void displayContacts() {
        contactList.removeAllViews();

        for (Contact contact : contacts) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(8, 8, 8, 8);

            // Name + Number
            TextView text = new TextView(this);
            text.setText(contact.name + ": " + contact.number);
            text.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            row.addView(text);

            // CALL Button
            Button callBtn = new Button(this);
            callBtn.setText("CALL");
            callBtn.setOnClickListener(v -> makeCall(contact.number));
            row.addView(callBtn);

            // SMS Button
            Button smsBtn = new Button(this);
            smsBtn.setText("SEND SMS");
            smsBtn.setOnClickListener(v -> sendSMS(contact.number));
            row.addView(smsBtn);

            contactList.addView(row);
        }
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Contact");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16,16,16,16);

        final TextView nameInput = new TextView(this);
        nameInput.setHint("Name");
        layout.addView(nameInput);

        final TextView numberInput = new TextView(this);
        numberInput.setHint("Phone Number");
        layout.addView(numberInput);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String number = numberInput.getText().toString().trim();
            if(!name.isEmpty() && !number.isEmpty()){
                contacts.add(new Contact(name, number));
                displayContacts();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void makeCall(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            startActivity(callIntent);
        } else {
            Toast.makeText(this, "Call permission required", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

    private void sendSMS(String number) {
        String msg = "🚨 SOS! I need help. My location: https://www.google.com/maps/search/?api=1&query=LAT,LNG";
        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
        smsIntent.putExtra("sms_body", msg);
        startActivity(smsIntent);
    }

    // Simple Contact model
    private static class Contact {
        String name, number;
        Contact(String name, String number){
            this.name = name;
            this.number = number;
        }
    }
}