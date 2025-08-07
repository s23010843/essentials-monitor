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

public class UserRegistrationActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Link UI elements
        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        Button signupButton = findViewById(R.id.signup_button);

        // Set listener for sign-up button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignup();
            }
        });

        // Setup login text link
        findViewById(R.id.login_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRegistrationActivity.this, UserAuthenticationActivity.class));
                finish();
            }
        });
    }

    private void performSignup() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Enhanced validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length() < 2) {
            Toast.makeText(this, "Name must be at least 2 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        // Email format validation
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }

        int password_num= 8;
        if (password.length() < password_num) {
            Toast.makeText(this, "Password must be at least " + password_num + " characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Additional password strength check
        if (!isPasswordStrong(password)) {
            Toast.makeText(this, "Password should contain letters and numbers for better security", Toast.LENGTH_LONG).show();
            return;
        }

        // Add user to database
        long userId = databaseHelper.addUser(name, email, password, "");
        if (userId != -1) {
            // Save user email to SharedPreferences
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_email", email);
            editor.apply();

            Toast.makeText(this, "Account created successfully! Please sign in.", Toast.LENGTH_SHORT).show();

            // Redirect to login
            Intent intent = new Intent(UserRegistrationActivity.this, UserAuthenticationActivity.class);
            intent.putExtra("email", email); // Pre-fill email on login screen
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to create account. Email may already exist.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPasswordStrong(String password) {
        return password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*");
    }
}