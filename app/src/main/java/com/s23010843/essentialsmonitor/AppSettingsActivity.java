package com.s23010843.essentialsmonitor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AppSettingsActivity extends AppCompatActivity {
    
    private Switch priceAlertsSwitch;
    private Switch promotionsSwitch;
    private Switch appUpdatesSwitch;
    private Button saveButton;
    private SharedPreferences prefs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        
        // Initialize SharedPreferences
        prefs = getSharedPreferences("notification_prefs", MODE_PRIVATE);
        
        // Initialize views
        initViews();
        
        // Load current settings
        loadSettings();
        
        // Set button listener
        saveButton.setOnClickListener(v -> saveSettings());
    }
    
    private void initViews() {
        priceAlertsSwitch = findViewById(R.id.price_alerts_switch);
        promotionsSwitch = findViewById(R.id.promotions_switch);
        appUpdatesSwitch = findViewById(R.id.app_updates_switch);
        saveButton = findViewById(R.id.save_settings_button);
    }
    
    private void loadSettings() {
        // Load saved preferences or set defaults
        priceAlertsSwitch.setChecked(prefs.getBoolean("price_alerts", true));
        promotionsSwitch.setChecked(prefs.getBoolean("promotions", false));
        appUpdatesSwitch.setChecked(prefs.getBoolean("app_updates", true));
    }
    
    private void saveSettings() {
        // Save preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("price_alerts", priceAlertsSwitch.isChecked());
        editor.putBoolean("promotions", promotionsSwitch.isChecked());
        editor.putBoolean("app_updates", appUpdatesSwitch.isChecked());
        editor.apply();
        
        // show a confirmation message
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}