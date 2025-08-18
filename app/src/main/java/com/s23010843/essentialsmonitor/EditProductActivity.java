package com.s23010843.essentialsmonitor;

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

import org.json.JSONObject;

import static utils.ApiConfig.PRODUCTS_ENDPOINT;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class EditProductActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText inputName, inputPrice, inputLocation, searchLocation;
    Button updateBtn, deleteBtn, searchBtn;
    GoogleMap mMap;
    String productId;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    private double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        inputName = findViewById(R.id.inputName);
        inputPrice = findViewById(R.id.inputPrice);
        inputLocation = findViewById(R.id.inputLocation);
        searchLocation = findViewById(R.id.searchLocation);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        searchBtn = findViewById(R.id.searchBtn);

        productId = getIntent().getStringExtra("product_id");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchBtn.setOnClickListener(v -> {
            String name = searchLocation.getText().toString().trim();
            if (!name.isEmpty()) geoLocateAndMove(name);
        });

        updateBtn.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String priceStr = inputPrice.getText().toString().trim();
            String locationStr = inputLocation.getText().toString().trim();
            if (name.isEmpty() || priceStr.isEmpty()) {
                toast("Name & price required");
                return;
            }
            double price = Double.parseDouble(priceStr);
            updateProduct(productId, name, price, locationStr);
        });

        deleteBtn.setOnClickListener(v -> deleteProduct(productId));

        loadProductDetails(productId);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void loadProductDetails(String id) {
        new Thread(() -> {
            try {
                URL url = new URL(PRODUCTS_ENDPOINT + "/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) sb.append(line);
                in.close();

                JSONObject product = new JSONObject(sb.toString());
                String name = product.getString("name");
                double price = product.getDouble("price");
                String locationTxt = product.optString("location", "");
                lat = product.optDouble("lat", 0);
                lon = product.optDouble("lon", 0);

                mainHandler.post(() -> {
                    inputName.setText(name);
                    inputPrice.setText(String.valueOf(price));
                    inputLocation.setText(locationTxt);

                    if (mMap != null && (lat != 0 || lon != 0)) {
                        LatLng point = new LatLng(lat, lon);
                        updateMap(point, "Location");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> toast("Failed to load product"));
            }
        }).start();
    }

    private void updateProduct(String id, String name, double price, String location) {
        new Thread(() -> {
            try {
                URL url = new URL(PRODUCTS_ENDPOINT + "/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("name", name);
                payload.put("price", price);
                payload.put("location", location);
                payload.put("lat", lat);
                payload.put("lon", lon);

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes());
                os.close();

                int response = conn.getResponseCode();
                mainHandler.post(() -> {
                    if (response == HttpURLConnection.HTTP_OK) {
                        toast("Updated");
                        finish();
                    } else toast("Update failed");
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> toast("Error occurred"));
            }
        }).start();
    }

    private void deleteProduct(String id) {
        new Thread(() -> {
            try {
                URL url = new URL(PRODUCTS_ENDPOINT + "/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                int response = conn.getResponseCode();
                mainHandler.post(() -> {
                    if (response == HttpURLConnection.HTTP_OK) {
                        toast("Deleted");
                        finish();
                    } else toast("Deletion failed");
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> toast("Error occurred"));
            }
        }).start();
    }

    private void geoLocateAndMove(String locationName) {
        new Thread(() -> {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocationName(locationName, 1);
                if (list != null && !list.isEmpty()) {
                    Address addr = list.get(0);
                    LatLng point = new LatLng(addr.getLatitude(), addr.getLongitude());
                    lat = point.latitude;
                    lon = point.longitude;

                    mainHandler.post(() -> {
                        inputLocation.setText(addr.getAddressLine(0));
                        updateMap(point, "Selected");
                    });
                } else mainHandler.post(() -> toast("Not found"));
            } catch (IOException e) {
                e.printStackTrace();
                mainHandler.post(() -> toast("Error searching location"));
            }
        }).start();
    }

    private void updateMap(LatLng point, String title) {
        if (mMap != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(point).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14f));
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}