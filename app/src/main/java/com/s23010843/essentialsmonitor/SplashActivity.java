package com.s23010843.essentialsmonitor;

import android.content.*;
import android.os.*;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

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
        }, 2000);
    }

    public void pageRoute(Class<?> page){
        startActivity(new Intent(this, page));
    }
}