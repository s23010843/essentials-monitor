package com.s23010843.essentialsmonitor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private EditText locationSearchEditText;
    private Button searchLocationButton;
    private Button myLocationButton;
    private Button manageStoresButton;
    private DatabaseHelper databaseHelper;
    private boolean locationPermissionGranted = false;

    // Default location (New York, USA) if location is not available
    private final LatLng defaultLocation = new LatLng(40.7589, -73.9851);
    private static final int DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_explorer);

        databaseHelper = new DatabaseHelper(this);

        // Initialize UI elements
        locationSearchEditText = findViewById(R.id.location_search_edit_text);
        searchLocationButton = findViewById(R.id.search_location_button);
        myLocationButton = findViewById(R.id.my_location_button);
        manageStoresButton = findViewById(R.id.manage_stores_button);

        // Initialize the FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Get the SupportMapFragment and request notification when map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set up click listeners
        setupClickListeners();
    }

    private void setupClickListeners() {
        searchLocationButton.setOnClickListener(v -> searchLocation());
        myLocationButton.setOnClickListener(v -> getCurrentLocation());
        manageStoresButton.setOnClickListener(v -> startActivity(new Intent(this, StoreManagementActivity.class)));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Request location permissions
        getLocationPermission();

        // Set up map click listener for store details
        googleMap.setOnMarkerClickListener(marker -> {
            showStoreDetails(marker);
            return true;
        });

        // Update location UI and load stores
        updateLocationUI();
        loadStoresFromDatabase();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            updateLocationUI();
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                updateLocationUI();
                getCurrentLocation();
            }
        }
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e) {
            // Permission not granted, location functionality disabled
            Toast.makeText(this, "Location permission not granted: " + e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void getCurrentLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        lastKnownLocation = task.getResult();
                        LatLng currentLatLng = new LatLng(lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
                        loadStoresFromDatabase();
                    } else {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            // Permission not granted for location access
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void searchLocation() {
        String locationQuery = locationSearchEditText.getText().toString().trim();
        if (locationQuery.isEmpty()) {
            Toast.makeText(this, "Please enter a location to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationQuery, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                locationSearchEditText.setText("");
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error searching location", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadStoresFromDatabase() {
        if (googleMap == null) return;
        
        // Clear existing markers
        googleMap.clear();
        
        // Get all stores from database
        List<Store> stores = databaseHelper.getAllStores();
        
        // Add markers for each store
        for (Store store : stores) {
            LatLng storeLatLng = new LatLng(store.getLatitude(), store.getLongitude());
            
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(storeLatLng)
                    .title(store.getName())
                    .snippet(store.getAddress())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            
            // Store the store ID as marker tag for reference
            if (marker != null) {
                marker.setTag(store.getId());
            }
        }
        
        if (stores.isEmpty()) {
            Toast.makeText(this, "No stores found. Add stores using Store Management.", Toast.LENGTH_LONG).show();
        }
    }

    private void showStoreDetails(Marker marker) {
        if (marker.getTag() == null) return;
        
        int storeId = (Integer) marker.getTag();
        
        // Create options dialog
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(marker.getTitle())
                .setMessage(marker.getSnippet())
                .setPositiveButton("Navigate", (dialog, which) -> {
                    // Open Google Maps for navigation
                    LatLng destination = marker.getPosition();
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + 
                            destination.latitude + "," + destination.longitude);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(this, "Google Maps not installed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("View Products", (dialog, which) -> {
                    // Navigate to products for this store
                    Intent intent = new Intent(this, ProductListActivity.class);
                    intent.putExtra("store_id", storeId);
                    intent.putExtra("store_name", marker.getTitle());
                    startActivity(intent);
                })
                .setNeutralButton("Close", null)
                .show();
    }
}
