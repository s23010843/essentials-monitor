package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    ImageView profileImg;
    EditText editName, editSocial;
    Button saveBtn;

    Uri selectedImageUri = null;  // To keep track of selected image URI

    DatabaseHelper db;
    String username;  // username passed via intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImg = findViewById(R.id.profileImg);
        editName = findViewById(R.id.editName);
        editSocial = findViewById(R.id.editSocial);
        saveBtn = findViewById(R.id.saveBtn);

        db = new DatabaseHelper(this);

        // Get username from Intent extras
        username = getIntent().getStringExtra("username");

        // Load existing data
        loadUserProfile();

        // Set click listener on profile image to pick a new one
        profileImg.setOnClickListener(v -> openImageChooser());

        // Save button click listener
        saveBtn.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String social = editSocial.getText().toString().trim();

            if (name.isEmpty()) {
                toastMessage("Please enter your name");
                return;
            }

            String imagePath = null;
            if (selectedImageUri != null) {
                imagePath = copyImageToInternalStorage(selectedImageUri);
                if (imagePath == null) {
                    toastMessage("Failed to save image");
                    return;
                }
            } else {
                imagePath = db.getProfileImagePath(username);
            }

            boolean updated = db.updateUserProfile(username, name, social, imagePath);
            if (updated) {
                toastMessage("Profile updated");
                finish(); // Close activity and go back
            } else {
                toastMessage("Failed to update profile");
            }
        });
    }

    private void loadUserProfile() {
        // Load name
        String name = db.getUserNameByUsername(username);
        editName.setText(name);

        // Load social link
        String socialLink = db.getSocialLinksByUsername(username);
        editSocial.setText(socialLink);

        // Load profile image path and set image if available
        String imagePath = db.getProfileImagePath(username);
        int defaultImg = R.drawable.ic_user_placeholder;

        if (imagePath != null && !imagePath.isEmpty()) {


            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                profileImg.setImageURI(Uri.fromFile(imgFile));
            } else {
                profileImg.setImageResource(defaultImg);
            }
        } else {
            profileImg.setImageResource(defaultImg);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            profileImg.setImageURI(selectedImageUri);
        }
    }

    private String copyImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream == null) return null;

            String fileName = "profile_" + System.currentTimeMillis() + ".jpg";

            File directory = new File(getFilesDir(), "profile_images");
            if (!directory.exists()) {
                directory.mkdir();
            }

            File imageFile = new File(directory, fileName);
            OutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return imageFile.getAbsolutePath();  // Return internal storage path

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}