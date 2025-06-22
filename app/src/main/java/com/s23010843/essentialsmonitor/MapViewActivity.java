package com.s23010843.essentialsmonitor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapViewActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private GoogleMapManager mapManager;
    private FusedLocationProviderClient locationClient;

    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        searchEditText = findViewById(R.id.location_search_edit_text);
        Button searchButton = findViewById(R.id.search_location_button);
        Button myLocationButton = findViewById(R.id.my_location_button);

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        if (checkLocationPermission()) {
            initializeMap();
        } else {
            requestLocationPermission();
        }

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
                return;
            }

            if (checkLocationPermission()) {
                locationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null && mapManager != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mapManager.searchStoresNearLocation(currentLatLng, query);
                    } else {
                        Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                requestLocationPermission();
            }
        });

        myLocationButton.setOnClickListener(v -> {
            if (checkLocationPermission()) {
                locationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null && mapManager != null) {
                        mapManager.updateUserLocation(location);
                    } else {
                        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                requestLocationPermission();
            }
        });
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);

        if (mapFragment != null) {
            mapManager = new GoogleMapManager(this, googleMap -> {
                Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
            });
            mapFragment.getMapAsync(mapManager);
        }
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeMap();
        } else {
            Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show();
        }
    }
}