package com.example.women;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class SafeRoutesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng safePlace;
    private Button btnPolice, btnHospital, btnNavigate, btnSOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_routes);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnPolice = findViewById(R.id.btnPolice);
        btnHospital = findViewById(R.id.btnHospital);
        btnNavigate = findViewById(R.id.btnNavigate);
        btnSOS = new Button(this); // Optional floating SOS button

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment == null) {
            Toast.makeText(this, "Map loading failed", Toast.LENGTH_LONG).show();
            return;
        }
        mapFragment.getMapAsync(this);

        btnPolice.setOnClickListener(v -> showNearbyPlaces("police"));
        btnHospital.setOnClickListener(v -> showNearbyPlaces("hospital"));
        btnNavigate.setOnClickListener(v -> navigateToSafePlace());
        btnSOS.setOnClickListener(v -> sendSOS());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                return;
            }

            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));

            // 🔵 Example safe place
            safePlace = new LatLng(location.getLatitude() + 0.01, location.getLongitude() + 0.01);
            mMap.addMarker(new MarkerOptions().position(safePlace).title("Safe Place"));

            // 🔴 Example danger zone
            LatLng dangerZone = new LatLng(location.getLatitude() + 0.005, location.getLongitude() + 0.005);
            mMap.addCircle(new CircleOptions()
                    .center(dangerZone)
                    .radius(100)
                    .strokeColor(0xFFFF0000)
                    .fillColor(0x44FF0000));

            drawRoute(current, safePlace);
        });
    }

    private void drawRoute(LatLng origin, LatLng destination) {
        // Use Directions API (pseudo-code)
        // You must fetch the polyline JSON from Google Directions API
        // Here we simulate a straight line polyline
        mMap.addPolyline(new PolylineOptions()
                .add(origin, destination)
                .width(10)
                .color(0xFF00FF00));
    }

    private void showNearbyPlaces(String type) {
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) return;
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());

            // Use Places API to find nearby police/hospitals (pseudo-code)
            // Example marker
            LatLng nearby = new LatLng(current.latitude + 0.002, current.longitude + 0.002);
            mMap.addMarker(new MarkerOptions().position(nearby).title(type + " nearby"));
        });
    }

    private void navigateToSafePlace() {
        if (safePlace == null) return;

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + safePlace.latitude + "," + safePlace.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void sendSOS() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) return;
            String message = "SOS! I need help. My location: " +
                    "https://www.google.com/maps/search/?api=1&query=" +
                    location.getLatitude() + "," + location.getLongitude();

            // Send SMS to trusted contact (requires SMS permission)
            Toast.makeText(this, "SOS sent: " + message, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 1 &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            recreate();
        }
    }
}