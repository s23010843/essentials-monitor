package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText inputLat, inputLon, searchLocation;
    private TextView addressTv;
    private double lat = 12.9716, lon = 77.5946;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        inputLat = findViewById(R.id.inputLat);
        inputLon = findViewById(R.id.inputLon);
        searchLocation = findViewById(R.id.searchLocation);
        addressTv = findViewById(R.id.addressTv);
        Button searchBtn = findViewById(R.id.searchBtn);
        Button saveLocation = findViewById(R.id.saveLocation);
        Button viewSensorBtn = findViewById(R.id.viewSensorBtn);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchBtn.setOnClickListener(v -> {
            String locationName = searchLocation.getText().toString().trim();
            if (!locationName.isEmpty()) {
                geoLocateAndMove(locationName);
            }
        });

        saveLocation.setOnClickListener(v -> {
            try {
                lat = Double.parseDouble(inputLat.getText().toString());
                lon = Double.parseDouble(inputLon.getText().toString());
                LatLng selectedLocation = new LatLng(lat, lon);
                updateMap(selectedLocation, "Manual Location");
                reverseGeocode(selectedLocation);
                toast("Location saved");
            } catch (NumberFormatException e) {
                toast("Invalid latitude or longitude");
            }
        });

        viewSensorBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SensorActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng defaultLocation = new LatLng(lat, lon);
        updateMap(defaultLocation, "Default Location");
        reverseGeocode(defaultLocation);

        mMap.setOnMapClickListener(point -> {
            lat = point.latitude;
            lon = point.longitude;

            inputLat.setText(String.format("%.5f", lat));
            inputLon.setText(String.format("%.5f", lon));

            updateMap(point, "Selected Location");
            reverseGeocode(point);
        });
    }

    private void updateMap(LatLng point, String title) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(point).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14f));
    }

    private void reverseGeocode(LatLng point) {
        new Thread(() -> {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                String addressText = (addresses != null && !addresses.isEmpty())
                        ? addresses.get(0).getAddressLine(0)
                        : "Unknown location";
                handler.post(() -> addressTv.setText(addressText));
            } catch (IOException e) {
                String i = "Error getting address";
                handler.post(() -> addressTv.setText(i));
            }
        }).start();
    }

    private void geoLocateAndMove(String locationName) {
        new Thread(() -> {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
                if (addressList != null && !addressList.isEmpty()) {
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    lat = latLng.latitude;
                    lon = latLng.longitude;

                    handler.post(() -> {
                        inputLat.setText(String.format("%.5f", lat));
                        inputLon.setText(String.format("%.5f", lon));
                        addressTv.setText(address.getAddressLine(0));
                        updateMap(latLng, "Search Result");
                    });
                } else {
                    handler.post(() -> toast("No results found"));
                }
            } catch (IOException e) {
                handler.post(() -> toast("Error searching location"));
            }
        }).start();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}