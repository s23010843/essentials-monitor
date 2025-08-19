package com.s23010843.essentialsmonitor;

import android.annotation.SuppressLint;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvWelcome;
    SharedPreferences prefs;
    Button btnProduct, btnProfile, settingsBtn, mapBtn, sensorBtn, customerProductBtn;
    String role;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnProduct = findViewById(R.id.btnProduct);
        btnProfile = findViewById(R.id.btnProfile);
        settingsBtn = findViewById(R.id.settingsBtn);
        mapBtn = findViewById(R.id.mapBtn);
        sensorBtn = findViewById(R.id.sensorBtn);
        customerProductBtn = findViewById(R.id.customerProductBtn);
        tvWelcome = findViewById(R.id.tvWelcome);

        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        role = prefs.getString("role", "customer"); // safer fallback

        tvWelcome.setText("Welcome, " + role);

        if (role.equalsIgnoreCase("owner")) {
            btnProduct.setVisibility(View.VISIBLE);
            btnProduct.setOnClickListener(v -> {
                pageRoute(ProductListActivity.class);
                toastMessage("Owner, you can update products now");
            });
        } else {
            btnProduct.setVisibility(View.GONE);
            toastMessage("Customer: limited access");
        }

        btnProfile.setOnClickListener(v -> pageRoute(ProfileActivity.class));
        settingsBtn.setOnClickListener(v -> pageRoute(SettingsActivity.class));
        mapBtn.setOnClickListener(v -> pageRoute(MapActivity.class));
        sensorBtn.setOnClickListener(v -> pageRoute(SensorActivity.class));
        customerProductBtn.setOnClickListener(v -> pageRoute(CustomerProductActivity.class));
    }

    public void pageRoute(Class<?> page) {
        startActivity(new Intent(this, page));
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}