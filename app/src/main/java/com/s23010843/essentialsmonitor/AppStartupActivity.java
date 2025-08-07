package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class AppStartupActivity extends AppCompatActivity {
    private static final int SPLASH_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_app_startup);

            new Handler(Looper.getMainLooper()).postDelayed(this::checkLoginStatus, SPLASH_TIMEOUT);
        } catch (Exception e) {
            // If splash fails, go directly to login
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AppStartupActivity.this, UserAuthenticationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkLoginStatus() {
        // Check SharedPreferences for login state
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        String loginMethod = prefs.getString("login_method", "");

        // Also check Google Sign In status
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        
        Intent intent;
        if (isLoggedIn && (googleAccount != null || !"google".equals(loginMethod))) {
            // User is logged in
            intent = new Intent(AppStartupActivity.this, DashboardHomeActivity.class);
        } else {
            // User is not logged in
            intent = new Intent(AppStartupActivity.this, UserAuthenticationActivity.class);
        }
        
        startActivity(intent);
        finish();
    }
}