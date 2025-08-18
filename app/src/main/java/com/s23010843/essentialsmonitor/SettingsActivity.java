package com.s23010843.essentialsmonitor;

import android.content.*;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Switch themeSwitch;
    Button logoutBtn;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        themeSwitch = findViewById(R.id.themeSwitch);
        logoutBtn = findViewById(R.id.logoutBtn);
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        boolean isDark = prefs.getBoolean("dark_mode", false);
        themeSwitch.setChecked(isDark);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            toast("Theme set. Restart app to apply.");
        });

        logoutBtn.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}