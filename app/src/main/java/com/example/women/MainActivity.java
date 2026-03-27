package com.example.women;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnSOS;
    TextView txtHome, txtSOSNav, txtProfile;

    // 🔵 Bluetooth
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    OutputStream outputStream;


    private static final String DEVICE_NAME = "HC-05";
    private static final UUID BT_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // 📩 Emergency contacts
    private final String[] emergencyContacts = {
            "9322984105", "9923851021", "9834336987"
    };

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSOS = findViewById(R.id.btnSOS);
        txtHome = findViewById(R.id.txt_home);
        txtSOSNav = findViewById(R.id.txt_sos);
        txtProfile = findViewById(R.id.txt_profile);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 🔐 Permissions (SMS + Bluetooth + CALL)
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.CALL_PHONE
        }, 1);

        btnSOS.setOnClickListener(v -> triggerSOS());

        txtHome.setOnClickListener(v ->
                startActivity(new Intent(this, HomeActivity.class)));

        txtSOSNav.setOnClickListener(v -> { });

        txtProfile.setOnClickListener(v ->
                startActivity(new Intent(this, com.example.women.ProfileActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectBluetooth();
    }

    // 🔗 Connect to HC-05
    private void connectBluetooth() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) return;

        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this,
                    "Turn ON Bluetooth",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Set<BluetoothDevice> devices =
                    bluetoothAdapter.getBondedDevices();

            for (BluetoothDevice device : devices) {
                if (DEVICE_NAME.equals(device.getName())) {
                    bluetoothSocket =
                            device.createRfcommSocketToServiceRecord(BT_UUID);
                    bluetoothSocket.connect();
                    outputStream = bluetoothSocket.getOutputStream();

                    Toast.makeText(this,
                            "Bluetooth Connected",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this,
                    "Bluetooth connection failed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // 🔴 SOS BUTTON
    private void triggerSOS() {
        sendSMS();              // 📩 SMS FIRST
        activateBuzzerAndLED();// 🔔 Arduino

        // 📞 CALL AFTER SMS (SAFE)
        new Handler().postDelayed(this::makeCall, 3000);

        Toast.makeText(this,
                "SOS Sent",
                Toast.LENGTH_LONG).show();
    }

    // 📩 SMS (NEVER BLOCKED)
    private void sendSMS() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,
                    "SMS permission denied",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String message =
                "🚨 SOS ALERT!\nI am in danger. Please help me immediately.";

        try {
            SmsManager smsManager = SmsManager.getDefault();

            for (String number : emergencyContacts) {
                smsManager.sendTextMessage(
                        number, null, message, null, null);
            }

            Toast.makeText(this,
                    "SOS SMS sent",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this,
                    "Failed to send SMS",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // 🔔 Arduino Buzzer + LED
    private void activateBuzzerAndLED() {
        try {
            if (outputStream != null) {
                outputStream.write("1".getBytes());
            }
        } catch (Exception ignored) {}
    }

    // 📞 EMERGENCY CALL
    private void makeCall() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) return;

        Intent intent = new Intent(
                Intent.ACTION_CALL,
                Uri.parse("tel:112"));
        startActivity(intent);
    }
}
