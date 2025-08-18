package com.s23010843.essentialsmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppDB.db";
    private static final int DATABASE_VERSION = 1;  // bump version since schema changed

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE auth (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT, " +
                "role TEXT)");

        db.execSQL("CREATE TABLE owner (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "username TEXT UNIQUE, " +
                "profileImg TEXT, " +
                "socialLinks TEXT)");

        db.execSQL("CREATE TABLE customer (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "username TEXT UNIQUE, " +
                "profileImg TEXT, " +
                "socialLinks TEXT)");

        // Product table with location column added
        db.execSQL("CREATE TABLE product (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "location TEXT, " +
                "owner_id INTEGER, " +
                "FOREIGN KEY(owner_id) REFERENCES owner(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables on upgrade, then recreate
        db.execSQL("DROP TABLE IF EXISTS auth");
        db.execSQL("DROP TABLE IF EXISTS owner");
        db.execSQL("DROP TABLE IF EXISTS customer");
        db.execSQL("DROP TABLE IF EXISTS product");
        onCreate(db);
    }

    // Get user name by username
    public String getUserNameByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT name FROM owner WHERE username=?", new String[]{username});
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("name"));
            }
            if (cursor != null) cursor.close();

            cursor = db.rawQuery("SELECT name FROM customer WHERE username=?", new String[]{username});
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("name"));
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return "Not Found";
    }

    // Get social links by username
    public String getSocialLinksByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT socialLinks FROM owner WHERE username=?", new String[]{username});
            if (cursor != null && cursor.moveToFirst()) {
                String result = cursor.getString(cursor.getColumnIndexOrThrow("socialLinks"));
                if (result != null && !result.isEmpty()) return result;
            }
            if (cursor != null) cursor.close();

            cursor = db.rawQuery("SELECT socialLinks FROM customer WHERE username=?", new String[]{username});
            if (cursor != null && cursor.moveToFirst()) {
                String result = cursor.getString(cursor.getColumnIndexOrThrow("socialLinks"));
                if (result != null && !result.isEmpty()) return result;
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return "Not provided";
    }

    // Get profile image path by username
    public String getProfileImagePath(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT profileImg FROM owner WHERE username=?", new String[]{username});
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("profileImg"));
            }
            if (cursor != null) cursor.close();

            cursor = db.rawQuery("SELECT profileImg FROM customer WHERE username=?", new String[]{username});
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("profileImg"));
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    // Update profile with image path
    public boolean updateUserProfile(String username, String name, String socialLinks, String profileImgPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("socialLinks", socialLinks);
        values.put("profileImg", profileImgPath);

        int rows = db.update("owner", values, "username=?", new String[]{username});
        if (rows > 0) return true;

        rows = db.update("customer", values, "username=?", new String[]{username});
        return rows > 0;
    }

    // Update profile without image path
    public boolean updateUserProfile(String username, String name, String socialLinks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("socialLinks", socialLinks);

        int rows = db.update("owner", values, "username=?", new String[]{username});
        if (rows > 0) return true;

        rows = db.update("customer", values, "username=?", new String[]{username});
        return rows > 0;
    }

    // Check user credentials
    public Cursor checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM auth WHERE username=? AND password=?", new String[]{username, password});
    }

    // Insert product with location
    public long insertProduct(String name, double price, String location, int ownerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("location", location);
        cv.put("owner_id", ownerId);
        return db.insert("product", null, cv);
    }

    // Get all products (id, name, price, location)
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id, name, price, location FROM product", null);
    }
}