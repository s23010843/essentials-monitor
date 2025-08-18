package com.s23010843.essentialsmonitor;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupUsername, signupPassword;
    Spinner roleSpinner;
    Button registerBtn;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signupName);
        signupUsername = findViewById(R.id.signupUsername);
        signupPassword = findViewById(R.id.signupPassword);
        roleSpinner = findViewById(R.id.roleSpinner);
        registerBtn = findViewById(R.id.registerBtn);

        db = new DatabaseHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"owner", "customer"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        registerBtn.setOnClickListener(v -> {
            String name = signupName.getText().toString().trim();
            String user = signupUsername.getText().toString().trim();
            String pass = signupPassword.getText().toString().trim();
            String role = roleSpinner.getSelectedItem().toString();

            // Validate input
            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                toastMessage("Please fill all fields");
                return;
            }

            // Check if username already exists
            if (isUsernameExists(user)) {
                toastMessage("Username already exists");
                return;
            }

            // Insert user into auth table and respective role table
            SQLiteDatabase sqlDB = db.getWritableDatabase();
            sqlDB.beginTransaction();
            try {
                ContentValues authValues = new ContentValues();
                authValues.put("username", user);
                authValues.put("password", pass);
                authValues.put("role", role);

                long authResult = sqlDB.insert("auth", null, authValues);
                if (authResult == -1) {
                    toastMessage("Registration failed");
                    return;
                }

                ContentValues profileValues = new ContentValues();
                profileValues.put("name", name);
                profileValues.put("username", user);
                profileValues.put("profileImg", "");
                profileValues.put("socialLinks", "");

                long profileResult;
                if (role.equals("owner")) {
                    profileResult = sqlDB.insert("owner", null, profileValues);
                } else {
                    profileResult = sqlDB.insert("customer", null, profileValues);
                }

                if (profileResult == -1) {
                    toastMessage("Registration failed");
                    return;
                }

                sqlDB.setTransactionSuccessful();
                toastMessage("Registered successfully");
                finish();
            } finally {
                sqlDB.endTransaction();
                sqlDB.close();
            }
        });

        TextView goLogin = findViewById(R.id.goLogin);
        goLogin.setOnClickListener(v -> pageRoute(LoginActivity.class));
    }

    private boolean isUsernameExists(String username) {
        SQLiteDatabase sqlDB = db.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT username FROM auth WHERE username=?", new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        sqlDB.close();
        return exists;
    }

    public void pageRoute(Class<?> page) {
        startActivity(new Intent(this, page));
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}