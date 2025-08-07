package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserAuthenticationActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_user_authentication);

            databaseHelper = new DatabaseHelper(this);

            emailEditText = findViewById(R.id.email_edit_text);
            passwordEditText = findViewById(R.id.password_edit_text);
            Button loginButton = findViewById(R.id.login_button);

            // Login button
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performLogin();
                }
            });

            // Setup signup text link
            findViewById(R.id.signup_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserAuthenticationActivity.this, UserRegistrationActivity.class));
                }
            });

            // Setup Forgot Password link
            findViewById(R.id.forgot_password_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UserAuthenticationActivity.this, "Forgot Password feature coming soon", Toast.LENGTH_SHORT).show();
                }
            });

            // Check if email was passed from signup
            String emailFromSignup = getIntent().getStringExtra("email");
            if (emailFromSignup != null && !emailFromSignup.isEmpty()) {
                emailEditText.setText(emailFromSignup);
                passwordEditText.requestFocus();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing login screen: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void performLogin() {
        try {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate input
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Email format validation
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verify credentials
            boolean isValidUser = databaseHelper.checkUser(email, password);
            if (isValidUser) {
                // Save login state and user email
                SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("is_logged_in", true);
                editor.putString("user_email", email);
                editor.apply();

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                // Go to Main Activity
                Intent intent = new Intent(UserAuthenticationActivity.this, DashboardHomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}