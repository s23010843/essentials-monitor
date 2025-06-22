package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, phoneEditText;
    private DatabaseHelper databaseHelper;
    private DatabaseHelper.User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        Button saveButton = findViewById(R.id.save_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        loadCurrentProfile();
    }

    private void loadCurrentProfile() {
        // TODO: Load current user data
        //nameEditText.setText("Hello World");
        //emailEditText.setText("hello.world@example.com");
        //phoneEditText.setText("(555) 123-4567");
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", "");
        currentUser = databaseHelper.getUser(userEmail);

        if (currentUser != null) {
            nameEditText.setText(currentUser.getName());
            emailEditText.setText(currentUser.getEmail());
            phoneEditText.setText(currentUser.getPhoneNumber());
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveProfile() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and email are required", Toast.LENGTH_SHORT).show();
            return;
        }


        // Update the user object
        currentUser.setName(name);
        currentUser.setEmail(email);
        currentUser.setPhoneNumber(phone);

        /*

        boolean success = databaseHelper.updateUser(currentUser);
        if (success) {
        */
            // Update email in SharedPreferences if changed
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String oldEmail = prefs.getString("user_email", "");
            if (!oldEmail.equals(email)) {
                prefs.edit().putString("user_email", email).apply();
            }

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        /*
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
        */

        // TODO: Save profile to Firebase
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();

    }
}