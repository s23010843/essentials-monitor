package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_dashboard_home);

            // Hide ActionBar i.e, Header
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            // Check if user is logged in
            checkUserLogin();

            // Initialize bottom navigation
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnItemSelectedListener(this::handleNavigationItemSelected);

            // Set up button click listeners
            setupButtonListeners();

        } catch (Exception e) {
            Toast.makeText(this, "Error initializing dashboard: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void checkUserLogin() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        
        if (!isLoggedIn) {
            // Redirect to login
            Intent intent = new Intent(this, UserAuthenticationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupButtonListeners() {
        // Products button
        findViewById(R.id.btn_products).setOnClickListener(v -> startActivity(new Intent(DashboardHomeActivity.this, ProductListActivity.class)));

        // Map button
        findViewById(R.id.btn_map).setOnClickListener(v -> startActivity(new Intent(DashboardHomeActivity.this, MapViewActivity.class)));

        // Reports button
        findViewById(R.id.btn_reports).setOnClickListener(v -> startActivity(new Intent(DashboardHomeActivity.this, ReportAnalyticsActivity.class)));

        // Profile button
        findViewById(R.id.btn_profile).setOnClickListener(v -> startActivity(new Intent(DashboardHomeActivity.this, UserProfileActivity.class)));

        // Add Product button
        findViewById(R.id.btn_add_product).setOnClickListener(v -> startActivity(new Intent(DashboardHomeActivity.this, ProductCreationActivity.class)));

        // Settings button
        findViewById(R.id.btn_settings).setOnClickListener(v -> startActivity(new Intent(DashboardHomeActivity.this, AppSettingsActivity.class)));
    }

    private boolean handleNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_dashboard) {
            // Already on dashboard
            return true;
        } else if (itemId == R.id.nav_sensor) {
            startActivity(new Intent(this, GeoSensorActivity.class));
            return true;
        } else if (itemId == R.id.nav_map) {
            startActivity(new Intent(this, MapViewActivity.class));
            return true;
        } else if (itemId == R.id.nav_profile) {
            startActivity(new Intent(this, UserProfileActivity.class));
            return true;
        }
        return false;
    }
}
