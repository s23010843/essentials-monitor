package com.s23010843.essentialsmonitor;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "EssentialsMonitor.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_STORES = "stores";
    private static final String TABLE_PRICE_REPORTS = "price_reports";
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_USERS = "users";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    // Products Table Columns
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_BARCODE = "barcode";

    // Stores Table Columns
    private static final String KEY_STORE_NAME = "store_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_HOURS = "hours";

    // Price Reports Table Columns
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_STORE_ID = "store_id";
    private static final String KEY_PRICE = "price";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_VERIFIED = "verified";
    private static final String KEY_AVAILABILITY = "availability";

    // Favorites Table Columns
    private static final String KEY_USER_EMAIL = "user_email";

    // Users Table Columns
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_PASSWORD_HASH = "password_hash";
    private static final String KEY_LOCATION_ENABLED = "location_enabled";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PRODUCT_NAME + " TEXT NOT NULL,"
                + KEY_CATEGORY + " TEXT NOT NULL,"
                + KEY_BRAND + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_BARCODE + " TEXT UNIQUE,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + KEY_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        // Create Stores table
        String CREATE_STORES_TABLE = "CREATE TABLE " + TABLE_STORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_STORE_NAME + " TEXT NOT NULL,"
                + KEY_ADDRESS + " TEXT NOT NULL,"
                + KEY_LATITUDE + " REAL,"
                + KEY_LONGITUDE + " REAL,"
                + KEY_PHONE + " TEXT,"
                + KEY_HOURS + " TEXT,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + KEY_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        // Create Price Reports table
        String CREATE_PRICE_REPORTS_TABLE = "CREATE TABLE " + TABLE_PRICE_REPORTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PRODUCT_ID + " INTEGER NOT NULL,"
                + KEY_STORE_ID + " INTEGER NOT NULL,"
                + KEY_PRICE + " REAL NOT NULL,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_VERIFIED + " INTEGER DEFAULT 0,"
                + KEY_AVAILABILITY + " INTEGER DEFAULT 1,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + KEY_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + KEY_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_STORE_ID + ") REFERENCES " + TABLE_STORES + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")"
                + ")";

        // Create Favorites table
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_EMAIL + " TEXT NOT NULL,"
                + KEY_PRODUCT_ID + " INTEGER NOT NULL,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + KEY_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + KEY_ID + ")"
                + ")";

        // Create Users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EMAIL + " TEXT NOT NULL UNIQUE,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_PHONE_NUMBER + " TEXT,"
                + KEY_PASSWORD_HASH + " TEXT NOT NULL,"
                + KEY_LOCATION_ENABLED + " INTEGER DEFAULT 1,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + KEY_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_STORES_TABLE);
        db.execSQL(CREATE_PRICE_REPORTS_TABLE);
        db.execSQL(CREATE_FAVORITES_TABLE);
        db.execSQL(CREATE_USERS_TABLE);

        // Insert sample data
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRICE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Sample data insertion method
    private void insertSampleData(SQLiteDatabase db) {
        // Sample products
        insertProduct(db, "Milk - 1 Gallon", "Food & Groceries", "Great Value", "Whole milk, 1 gallon", "1234567890");
        insertProduct(db, "Bread - White", "Food & Groceries", "Wonder", "Sliced white bread", "1234567891");
        insertProduct(db, "Gasoline - Regular", "Fuel & Gas", "Shell", "87 Octane unleaded", "1234567892");
        insertProduct(db, "Ibuprofen 200mg", "Medicine & Healthcare", "Advil", "Pain reliever, 100 tablets", "1234567893");

        // Sample stores
        insertStore(db, "Walmart Supercenter", "123 Main St, City, State 12345", 40.7589, -73.9851, "(555) 123-4567", "24 hours");
        insertStore(db, "Target", "456 Oak Ave, City, State 12345", 40.7505, -73.9934, "(555) 234-5678", "8AM-10PM");
        insertStore(db, "CVS Pharmacy", "789 Pine St, City, State 12345", 40.7614, -73.9776, "(555) 345-6789", "7AM-11PM");

        // Sample price reports
        insertPriceReport(db, 1, 1, 3.99, 1, 1, 1);
        insertPriceReport(db, 2, 1, 2.49, 1, 1, 1);
        insertPriceReport(db, 1, 2, 4.19, 1, 1, 1);
        insertPriceReport(db, 3, 1, 3.45, 1, 1, 1);
    }

    // Insert helper methods
    private void insertProduct(SQLiteDatabase db, String name, String category, String brand, String description, String barcode) {
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, name);
        values.put(KEY_CATEGORY, category);
        values.put(KEY_BRAND, brand);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_BARCODE, barcode);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    private void insertStore(SQLiteDatabase db, String name, String address, double lat, double lng, String phone, String hours) {
        ContentValues values = new ContentValues();
        values.put(KEY_STORE_NAME, name);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_LATITUDE, lat);
        values.put(KEY_LONGITUDE, lng);
        values.put(KEY_PHONE, phone);
        values.put(KEY_HOURS, hours);
        db.insert(TABLE_STORES, null, values);
    }

    private void insertPriceReport(SQLiteDatabase db, int productId, int storeId, double price, int userId, int verified, int availability) {
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, productId);
        values.put(KEY_STORE_ID, storeId);
        values.put(KEY_PRICE, price);
        values.put(KEY_USER_ID, userId);
        values.put(KEY_VERIFIED, verified);
        values.put(KEY_AVAILABILITY, availability);
        db.insert(TABLE_PRICE_REPORTS, null, values);
    }

    // User methods
    public long addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        //values.put(KEY_PHONE_NUMBER, phoneNumber);
        values.put(KEY_PASSWORD_HASH, hashPassword(password));

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_ID},
                KEY_EMAIL + "=? AND " + KEY_PASSWORD_HASH + "=?",
                new String[]{email, hashedPassword},
                null, null, null);

        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        // Don't close db here to prevent issues with other queries
        // db.close();
        return userExists;
    }

    @SuppressLint("Range")
    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, null,
                KEY_EMAIL + "=?", new String[]{email},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            int phoneIndex = cursor.getColumnIndex(KEY_PHONE_NUMBER);
            if (phoneIndex != -1) {
                user.setPhoneNumber(cursor.getString(phoneIndex));
            } else {
                user.setPhoneNumber(null);  // Safe fallback
            }
        }

        cursor.close();
        return user;
    }

    public int getUserReportCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PRICE_REPORTS
                + " WHERE " + KEY_USER_ID + "=?", new String[]{String.valueOf(userId)});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public long addPriceReport(String productName, double price, String storeName, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, productName);
        values.put(KEY_PRICE, price);
        values.put(KEY_STORE_NAME, storeName);
        values.put(KEY_CATEGORY, category);
        values.put(KEY_CREATED_AT, System.currentTimeMillis());

        long id = db.insert(TABLE_PRICE_REPORTS, null, values);
        db.close();
        return id;
    }

    private String hashPassword(String password) {
        // Simple hash for demo purposes
        return String.valueOf(password.hashCode());
    }

    public boolean updateUser(int userId, String newName, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, newName);
        values.put(KEY_EMAIL, newEmail);

        int rowsAffected = db.update(TABLE_USERS, values, KEY_ID + "=?", new String[]{String.valueOf(userId)});
        return rowsAffected > 0;
    }


    // User class
    public static class User {

        private int id;
        private String name, email, phoneNumber;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    // Product methods
    public long addProduct(String name, String category, String brand, String description, String barcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, name);
        values.put(KEY_CATEGORY, category);
        values.put(KEY_BRAND, brand);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_BARCODE, barcode);

        long id = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, KEY_PRODUCT_NAME);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRODUCT_NAME)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
                product.setBarcode(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BARCODE)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return products;
    }

    @SuppressLint("Range")
    public List<Product> searchProducts(String query) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_PRODUCT_NAME + " LIKE ? OR " + KEY_CATEGORY + " LIKE ? OR " + KEY_BRAND + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%", "%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, KEY_PRODUCT_NAME);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRODUCT_NAME)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return products;
    }

    // Store methods
    public long addStore(String name, String address, double latitude, double longitude, String phone, String hours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STORE_NAME, name);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_PHONE, phone);
        values.put(KEY_HOURS, hours);

        long id = db.insert(TABLE_STORES, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public List<Store> getNearbyStores(double userLat, double userLng, double radiusKm) {
        List<Store> stores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STORES, null, null, null, null, null, KEY_STORE_NAME);

        if (cursor.moveToFirst()) {
            do {
                double storeLat = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE));
                double storeLng = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE));

                // Calculate distance
                double distance = calculateDistance(userLat, userLng, storeLat, storeLng);

                if (distance <= radiusKm) {
                    Store store = new Store();
                    store.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                    store.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STORE_NAME)));
                    store.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS)));
                    store.setLatitude(storeLat);
                    store.setLongitude(storeLng);
                    store.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                    store.setHours(cursor.getString(cursor.getColumnIndexOrThrow(KEY_HOURS)));
                    store.setDistance(distance);
                    stores.add(store);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return stores;
    }

    // Price report methods
    public long addPriceReport(int productId, int storeId, double price, int userId, boolean verified, boolean available) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, productId);
        values.put(KEY_STORE_ID, storeId);
        values.put(KEY_PRICE, price);
        values.put(KEY_USER_ID, userId);
        values.put(KEY_VERIFIED, verified ? 1 : 0);
        values.put(KEY_AVAILABILITY, available ? 1 : 0);

        long id = db.insert(TABLE_PRICE_REPORTS, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public List<PriceReport> getPriceHistory(int productId, int storeId) {
        List<PriceReport> reports = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_PRODUCT_ID + " = ? AND " + KEY_STORE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId), String.valueOf(storeId)};

        Cursor cursor = db.query(TABLE_PRICE_REPORTS, null, selection, selectionArgs,
                null, null, KEY_CREATED_AT + " DESC");

        if (cursor.moveToFirst()) {
            do {
                PriceReport report = new PriceReport();
                report.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                report.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PRODUCT_ID)));
                report.setStoreId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STORE_ID)));
                report.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_ID)));
                report.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                report.setVerified(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VERIFIED)) == 1);
                report.setAvailable(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_AVAILABILITY)) == 1);
                report.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CREATED_AT)));
                reports.add(report);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return reports;
    }

    // Utility method to calculate distance between two points (Haversine formula)
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // Earth radius in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;  // Distance in km
    }

    // Data model classes
    public static class Product {

        private int id;
        private String name, category, brand, description, barcode;

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }
    }

    public static class Store {

        private int id;
        private String name, address, phone, hours;
        private double latitude, longitude, distance;

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }

    public static class PriceReport {

        private int id, productId, storeId, userId;
        private double price;
        private boolean verified, available;
        private String createdAt;

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getStoreId() {
            return storeId;
        }

        public void setStoreId(int storeId) {
            this.storeId = storeId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public boolean isVerified() {
            return verified;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
