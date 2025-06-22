package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private TextView userNameTextView, userEmailTextView, reportsCountTextView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        databaseHelper = new DatabaseHelper(this);

        userNameTextView = findViewById(R.id.user_name_text_view);
        userEmailTextView = findViewById(R.id.user_email_text_view);
        reportsCountTextView = findViewById(R.id.reports_count_text_view);
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        Button settingsButton = findViewById(R.id.settings_button);
        Button logoutButton = findViewById(R.id.logout_button);

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

        loadUserProfile();
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", "");
        
        if (!userEmail.isEmpty()) {
            DatabaseHelper.User user = databaseHelper.getUser(userEmail);
            if (user != null) {
                userNameTextView.setText(user.getName());
                userEmailTextView.setText(user.getEmail());
                
                int reportCount = databaseHelper.getUserReportCount(user.getId());
                reportsCountTextView.setText(reportCount + R.string.reports_submitted);
            }
        } else {
            userNameTextView.setText(R.string.guest_user);
            userEmailTextView.setText(R.string.not_logged_in);
            reportsCountTextView.setText(R.string._0_reports_submitted);
        }
    }

    private void performLogout() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
