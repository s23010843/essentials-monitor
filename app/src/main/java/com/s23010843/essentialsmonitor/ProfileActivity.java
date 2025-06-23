package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView userNameTextView, userEmailTextView, reportsCountTextView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Check if user is logged in, otherwise redirect to LoginActivity
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        if (!isLoggedIn) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize views
        userNameTextView = findViewById(R.id.user_name_text_view);
        userEmailTextView = findViewById(R.id.user_email_text_view);
        reportsCountTextView = findViewById(R.id.reports_count_text_view);
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        Button settingsButton = findViewById(R.id.settings_button);
        Button logoutButton = findViewById(R.id.logout_button);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Set button listeners
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, NotificationSettingsActivity.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });

        // Load user data and display profile info
        loadUserProfile();
    }

    private void loadUserProfile() {
        try {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String userEmail = prefs.getString("user_email", "");

            if (!userEmail.isEmpty()) {
                DatabaseHelper.User user = databaseHelper.getUser(userEmail);

                if (user != null) {
                    userNameTextView.setText(user.getName() != null ? user.getName() : "No Name");
                    userEmailTextView.setText(user.getEmail() != null ? user.getEmail() : "No Email");

                    int reportCount = databaseHelper.getUserReportCount(user.getId());
                    reportsCountTextView.setText(reportCount + " reports submitted");
                } else {
                    userNameTextView.setText("Unknown User");
                    userEmailTextView.setText(userEmail);
                    reportsCountTextView.setText("0 reports submitted");
                    Toast.makeText(this, "User not found in database.", Toast.LENGTH_SHORT).show();
                }
            } else {
                userNameTextView.setText("Guest User");
                userEmailTextView.setText("Not logged in");
                reportsCountTextView.setText("0 reports submitted");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void performLogout() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Clear all saved preferences including login state
        editor.apply();

        // Redirect to login, clearing activity stack
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}