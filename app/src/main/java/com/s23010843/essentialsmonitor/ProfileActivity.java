package com.s23010843.essentialsmonitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import database.DatabaseHelper;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvUsername, tvSocialLinks;
    ImageView profileImg;
    DatabaseHelper db;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvUsername);
        tvSocialLinks = findViewById(R.id.tvSocialLinks);
        profileImg = findViewById(R.id.profileImg);

        db = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        String profileUsername = R.string.profile_username + username;
        String nameFromDB = db.getUserNameByUsername(username);
        String profileName = R.string.profile_name + nameFromDB;
        String socialLink = db.getSocialLinksByUsername(username);
        String imagePath = db.getProfileImagePath(username);

        if (username == null || username.isEmpty()) {
            toastMessage("No logged in user found");
            finish();
            return;
        }

        // Load user info
        tvName.setText(profileName);
        tvUsername.setText(profileUsername);
        tvSocialLinks.setText(socialLink);
        tvSocialLinks.setTextColor(R.color.link);

        tvSocialLinks.setOnClickListener(v -> {
            if (!socialLink.equals("Not provided")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(socialLink));
                startActivity(browserIntent);
            } else {
                toastMessage("No social link provided");
            }
        });

        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                profileImg.setImageURI(Uri.fromFile(imgFile));
            } else {
                profileImg.setImageResource(R.drawable.ic_user_placeholder);
            }
        } else {
            profileImg.setImageResource(R.drawable.ic_user_placeholder);
        }
    }

    public void goToEditProfile(android.view.View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("username", tvUsername.getText().toString().replace("Username: ", ""));
        startActivity(intent);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}