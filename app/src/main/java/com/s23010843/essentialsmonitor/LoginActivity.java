package com.s23010843.essentialsmonitor;

import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText username, password;
    Button loginBtn;
    DatabaseHelper db;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        db = new DatabaseHelper(this);
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        loginBtn.setOnClickListener(v -> {
            String userInput = username.getText().toString().trim();
            String passInput = password.getText().toString().trim();

            // Validate input
            if (userInput.isEmpty()) {
                showToast("Please enter username");
                return;
            }
            if (passInput.isEmpty()) {
                showToast("Please enter password");
                return;
            }

            Cursor c = null;
            try {
                c = db.checkUser(userInput, passInput);
                if (c != null && c.moveToFirst()) {
                    // Valid credentials
                    prefs.edit()
                            .putBoolean("isLoggedIn", true)
                            .putString("username", userInput)
                            .apply();

                    pageRoute(MainActivity.class);
                    finish();
                } else {
                    showToast("Invalid credentials");
                }
            } finally {
                if (c != null) c.close();
            }
        });

        TextView goSignup = findViewById(R.id.goSignup);
        goSignup.setOnClickListener(v -> pageRoute(SignupActivity.class));
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void pageRoute(Class<?> page) {
        startActivity(new Intent(this, page));
    }
}