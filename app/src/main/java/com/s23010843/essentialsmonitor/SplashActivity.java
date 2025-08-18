package com.s23010843.essentialsmonitor;

import android.content.*;
import android.os.*;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay for 2 seconds
        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

            if (isLoggedIn) {
                pageRoute(MainActivity.class);
            } else {
                pageRoute(LoginActivity.class);
            }
            finish();
        }, SPLASH_TIMEOUT);
    }

    public void pageRoute(Class<?> page) {
        startActivity(new Intent(this, page));
    }
}