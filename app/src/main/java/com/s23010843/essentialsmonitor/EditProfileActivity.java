package com.s23010843.essentialsmonitor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        nameEditText = findViewById(R.id.edit_name_edit_text);
        emailEditText = findViewById(R.id.edit_email_edit_text);
        Button saveButton = findViewById(R.id.save_profile_button);
        databaseHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", "");
        User user = databaseHelper.getUser(userEmail);
        if (user != null) {
            nameEditText.setText(user.getName());
            emailEditText.setText(user.getEmail());
        }

        saveButton.setOnClickListener(v -> {
            String newName = nameEditText.getText().toString().trim();
            String newEmail = emailEditText.getText().toString().trim();
            // Add password and image fields if present in your UI
            String newPassword = ""; // TODO: get from password field
            String newImageUrl = ""; // TODO: get from image picker
            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Name and email cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            assert user != null;
            int userId = user.getId();
            int rowsUpdated = databaseHelper.updateUser(userId, newName, newEmail, newPassword, newImageUrl);
            if (rowsUpdated > 0) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user_email", newEmail);
                editor.apply();
                Toast.makeText(this, "Your profile has been updated!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Sorry, we couldn't update your profile. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
