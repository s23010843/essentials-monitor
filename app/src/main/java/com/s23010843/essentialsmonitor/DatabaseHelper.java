package com.s23010843.essentialsmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple DatabaseHelper for basic CRUD operations
 * Simplified SQLite database without complex features
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Add new methods for name/address only
    public long addStoreByName(String name, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STORE_NAME, name);
        values.put(KEY_ADDRESS, address);
        return db.insert(TABLE_STORES, null, values);
    }

    public int updateStoreByName(int id, String name, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STORE_NAME, name);
        values.put(KEY_ADDRESS, address);
        return db.update(TABLE_STORES, values, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Database Info
    private static final String DATABASE_NAME = "EssentialsMonitor.db";
    private static final int DATABASE_VERSION = 4; // Incremented due to schema changes

    // Table Names
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_STORES = "stores";
    private static final String TABLE_PRICE_REPORTS = "price_reports";
    private static final String TABLE_USERS = "users";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // Products Table Columns
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE_URL = "image_url";

    // Stores Table Columns
    private static final String KEY_STORE_NAME = "store_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_HOURS = "hours";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    // Price Reports Table Columns
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_STORE_ID = "store_id";
    private static final String KEY_PRICE = "price";

    // Users Table Columns
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_IMAGE_URL = "image_url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PRODUCT_NAME + " TEXT NOT NULL,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_BRAND + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        // Create Stores table
        String CREATE_STORES_TABLE = "CREATE TABLE " + TABLE_STORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_STORE_NAME + " TEXT NOT NULL,"
                + KEY_ADDRESS + " TEXT NOT NULL,"
                + KEY_HOURS + " TEXT,"
                + KEY_LATITUDE + " REAL,"
                + KEY_LONGITUDE + " REAL,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        // Create Price Reports table
        String CREATE_PRICE_REPORTS_TABLE = "CREATE TABLE " + TABLE_PRICE_REPORTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PRODUCT_ID + " INTEGER NOT NULL,"
                + KEY_STORE_ID + " INTEGER NOT NULL,"
                + KEY_PRICE + " REAL NOT NULL,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + KEY_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_STORE_ID + ") REFERENCES " + TABLE_STORES + "(" + KEY_ID + ")"
                + ")";

        // Create Users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_USER_EMAIL + " TEXT UNIQUE NOT NULL, "
                + KEY_USER_NAME + " TEXT NOT NULL, "
                + KEY_USER_PASSWORD + " TEXT, "
                + KEY_USER_IMAGE_URL + " TEXT, "
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_STORES_TABLE);
        db.execSQL(CREATE_PRICE_REPORTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);

        // Insert sample data
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRICE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Sample products with image URLs
        String[] products = {
            "Apple,Fruits,Fresh,Red apples,https://example.com/apple.jpg",
            "Milk,Dairy,Local,Fresh milk 1L,https://example.com/milk.jpg",
            "Bread,Bakery,Daily,White bread loaf,https://example.com/bread.jpg",
            "Rice,Grains,Premium,Jasmine rice 5kg,https://example.com/rice.jpg",
            "Chicken,Meat,Fresh,Chicken breast 1kg,https://example.com/chicken.jpg"
        };

        for (String product : products) {
            String[] parts = product.split(",");
            ContentValues values = new ContentValues();
            values.put(KEY_PRODUCT_NAME, parts[0]);
            values.put(KEY_CATEGORY, parts[1]);
            values.put(KEY_BRAND, parts[2]);
            values.put(KEY_DESCRIPTION, parts[3]);
            values.put(KEY_IMAGE_URL, parts.length > 4 ? parts[4] : null);
            db.insert(TABLE_PRODUCTS, null, values);
        }

        // Sample stores
        String[] stores = {
            // USA
            "SuperMart,123 Main St,40.7128,-74.0060",
            "Fresh Market,456 Oak Ave,40.7589,-73.9851",
            "Quick Shop,789 Pine Rd,40.7831,-73.9712",
            "Whole Foods,200 Market St,40.7306,-73.9352",
            "Trader Joe's,300 Broadway,40.7120,-74.0100",
            // UK
            "Tesco,1 High St,51.5074,-0.1278",
            "Sainsbury's,22 Oxford St,51.5154,-0.1410",
            "Waitrose,15 Regent St,51.5112,-0.1234",
            "Morrisons,8 Piccadilly,51.5098,-0.1337",
            "ASDA,25 Baker St,51.5205,-0.1570",
            // Japan
            "Lawson,3 Chome-1-1 Marunouchi,35.6812,139.7671",
            "FamilyMart,2-7-2 Ginza,35.6717,139.7650",
            "7-Eleven,1-5-1 Shibuya,35.6580,139.7016",
            "Aeon,4-1-1 Minami,35.6895,139.6917",
            "Ito-Yokado,5-24-8 Sendagaya,35.6785,139.7196",
            // Australia
            "Woolworths,100 George St,-33.8688,151.2093",
            "Coles,200 Pitt St,-33.8708,151.2073",
            "IGA,50 Elizabeth St,-37.8136,144.9631",
            "Aldi,300 Swanston St,-37.8100,144.9650",
            "FoodWorks,400 Collins St,-37.8170,144.9560",
            // India
            "Reliance Fresh,5 MG Road,28.6139,77.2090",
            "Big Bazaar,10 Connaught Place,28.6304,77.2177",
            "D-Mart,15 Andheri West,19.1197,72.8468",
            "Spencer's,20 Park St,22.5726,88.3639",
            "More Supermarket,25 Brigade Rd,12.9716,77.5946",
            // Canada
            "Loblaws,50 Bloor St,43.6510,-79.3470",
            "Metro,100 King St,43.6532,-79.3832",
            "Sobeys,200 Queen St,43.6535,-79.3841",
            "No Frills,300 Yonge St,43.6540,-79.3807",
            "Superstore,400 Dundas St,43.6550,-79.3790",
            // Maldives
            "STO Supermart,Chaandhanee Magu,4.1755,73.5093",
            "Agora,Henveiru,4.1750,73.5080",
            "Redwave,Maafannu,4.1667,73.5000",
            "Centro,Henveiru,4.1752,73.5085",
            "Market,Maafannu,4.1670,73.5010",
            // Sri Lanka
            "Cargills Food City,123 Galle Rd,6.9271,79.8612",
            "Keells Super,456 Duplication Rd,6.9061,79.8688",
            "Arpico Supercentre,789 Kandy Rd,7.2906,80.6337",
            "Laugfs Super,321 Havelock Rd,6.8941,79.8651",
            "Sathosa,654 Colombo St,6.9275,79.8617",
            // France
            "Carrefour,10 Rue de Rivoli,48.8566,2.3522",
            "Auchan,20 Champs Elysees,48.8698,2.3078",
            "Intermarché,30 Boulevard St,48.8530,2.3499",
            "Leclerc,40 Rue de Lyon,48.8442,2.3730",
            "Monoprix,50 Rue de la Paix,48.8675,2.3318",
            // Spain
            "Mercadona,20 Gran Via,40.4168,-3.7038",
            "Carrefour Express,30 Calle Mayor,40.4154,-3.7074",
            "Dia,40 Calle de Serrano,40.4352,-3.6877",
            "Eroski,50 Calle de Toledo,40.4100,-3.7075",
            "Alcampo,60 Calle de Goya,40.4262,-3.6795",
            // Germany
            "Edeka,30 Alexanderplatz,52.5200,13.4050",
            "Rewe,40 Friedrichstrasse,52.5208,13.3872",
            "Lidl,50 Kurfürstendamm,52.5010,13.3300",
            "Aldi Süd,60 Potsdamer Platz,52.5096,13.3760",
            "Kaufland,70 Hauptstrasse,52.4860,13.4280",
            // Portugal
            "Pingo Doce,40 Avenida da Liberdade,38.7223,-9.1393",
            "Continente,50 Rua Augusta,38.7139,-9.1335",
            "Intermarché,60 Rua do Ouro,38.7078,-9.1357",
            "Minipreço,70 Rua da Prata,38.7100,-9.1360",
            "Auchan,80 Rua de Santa Marta,38.7220,-9.1450"
        };

        for (String store : stores) {
            String[] parts = store.split(",");
            ContentValues values = new ContentValues();
            values.put(KEY_STORE_NAME, parts[0]);
            values.put(KEY_ADDRESS, parts[1]);
            values.put(KEY_LATITUDE, Double.parseDouble(parts[2]));
            values.put(KEY_LONGITUDE, Double.parseDouble(parts[3]));
            db.insert(TABLE_STORES, null, values);
        }
    }

    // ===== CRUD OPERATIONS =====

    // CREATE
    public long addProduct(String name, String category, String brand, String description, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, name);
        values.put(KEY_CATEGORY, category);
        values.put(KEY_BRAND, brand);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_IMAGE_URL, imageUrl);
        long id = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return id;
    }

    // Overloaded method for backward compatibility
    public long addProduct(String name, String category, String brand, String description) {
        return addProduct(name, category, brand, description, null);
    }

    public long addStore(String name, String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STORE_NAME, name);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        long id = db.insert(TABLE_STORES, null, values);
        db.close();
        return id;
    }

    public long addPriceReport(int productId, int storeId, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, productId);
        values.put(KEY_STORE_ID, storeId);
        values.put(KEY_PRICE, price);
        long id = db.insert(TABLE_PRICE_REPORTS, null, values);
        db.close();
        return id;
    }

    // Helper methods to get IDs by name
    public int getProductIdByName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{KEY_ID}, 
                               KEY_PRODUCT_NAME + "=?", new String[]{productName}, 
                               null, null, null);
        
        int productId = -1;
        if (cursor.moveToFirst()) {
            productId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
        }
        cursor.close();
        db.close();
        return productId;
    }

    public int getStoreIdByName(String storeName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STORES, new String[]{KEY_ID}, 
                               KEY_STORE_NAME + "=?", new String[]{storeName}, 
                               null, null, null);
        
        int storeId = -1;
        if (cursor.moveToFirst()) {
            storeId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
        }
        cursor.close();
        db.close();
        return storeId;
    }

    // Overloaded addPriceReport method that accepts names
    public long addPriceReport(String productName, double price, String storeName, String category) {
        // Get IDs from names
        int productId = getProductIdByName(productName);
        int storeId = getStoreIdByName(storeName);
        
        // Check if both IDs were found
        if (productId == -1 || storeId == -1) {
            return -1; // Return error if product or store not found
        }
        
        // Call the original method with IDs
        return addPriceReport(productId, storeId, price);
    }

    // READ
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY " + KEY_PRODUCT_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRODUCT_NAME)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL)));
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STORES + " ORDER BY " + KEY_STORE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Store store = new Store();
                store.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                store.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STORE_NAME)));
                store.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS)));
                store.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                store.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                stores.add(store);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return stores;
    }

    public List<PriceReport> getAllPriceReports() {
        List<PriceReport> priceReports = new ArrayList<>();
        String selectQuery = "SELECT pr.*, p." + KEY_PRODUCT_NAME + ", s." + KEY_STORE_NAME + 
                           " FROM " + TABLE_PRICE_REPORTS + " pr" +
                           " LEFT JOIN " + TABLE_PRODUCTS + " p ON pr." + KEY_PRODUCT_ID + " = p." + KEY_ID +
                           " LEFT JOIN " + TABLE_STORES + " s ON pr." + KEY_STORE_ID + " = s." + KEY_ID +
                           " ORDER BY pr." + KEY_CREATED_AT + " DESC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PriceReport priceReport = new PriceReport();
                priceReport.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                priceReport.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PRODUCT_ID)));
                priceReport.setStoreId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STORE_ID)));
                priceReport.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                priceReport.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CREATED_AT)));
                priceReport.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRODUCT_NAME)));
                priceReport.setStoreName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STORE_NAME)));
                // Note: verified field is not in the current schema, so it defaults to false
                priceReport.setVerified(false);
                priceReports.add(priceReport);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return priceReports;
    }

    public int getUserReportCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_PRICE_REPORTS;
        Cursor cursor = db.rawQuery(countQuery, null);
        
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    // SEARCH
    public List<Product> searchProducts(String searchTerm) {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + 
                           " WHERE " + KEY_PRODUCT_NAME + " LIKE ? OR " +
                           KEY_CATEGORY + " LIKE ? OR " +
                           KEY_BRAND + " LIKE ?" +
                           " ORDER BY " + KEY_PRODUCT_NAME;
        
        String searchPattern = "%" + searchTerm + "%";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{searchPattern, searchPattern, searchPattern});

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRODUCT_NAME)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL)));
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    // UPDATE
    public int updateProduct(int id, String name, String category, String brand, String description, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, name);
        values.put(KEY_CATEGORY, category);
        values.put(KEY_BRAND, brand);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_IMAGE_URL, imageUrl);
        
        int rowsUpdated = db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?", 
                                  new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated;
    }

    // Overloaded method for backward compatibility
    public int updateProduct(int id, String name, String category, String brand, String description) {
        return updateProduct(id, name, category, brand, description, null);
    }

    public int updateStore(int id, String name, String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STORE_NAME, name);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        
        int rowsUpdated = db.update(TABLE_STORES, values, KEY_ID + " = ?", 
                                  new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated;
    }

    // DELETE
    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteStore(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STORES, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Get database statistics
    public int getProductCount() {
        String countQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getStoreCount() {
        String countQuery = "SELECT * FROM " + TABLE_STORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // ===== USER AND ORDER OPERATIONS =====
    // Update user info
    public int updateUser(int id, String name, String email, String password, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, name);
        values.put(KEY_USER_EMAIL, email);
        if (password != null && !password.isEmpty()) {
            values.put(KEY_USER_PASSWORD, password);
        }
        if (imageUrl != null && !imageUrl.isEmpty()) {
            values.put(KEY_USER_IMAGE_URL, imageUrl);
        }
        int rowsUpdated = db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated;
    }

    // User CRUD operations
    public long addUser(String email, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_EMAIL, email);
        values.put(KEY_USER_NAME, name);
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // Overloaded method for registration with password (name, email, password)
    public long addUser(String name, String email, String password, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_EMAIL, email);
        values.put(KEY_USER_NAME, name);
        values.put(KEY_USER_PASSWORD, password);
        values.put(KEY_USER_IMAGE_URL, imageUrl);

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_USER_EMAIL + "=?", 
                               new String[]{email}, null, null, null);
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_EMAIL)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_NAME)));
            user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CREATED_AT)));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_PASSWORD));
            user.setPassword(password != null ? password : "");
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_IMAGE_URL));
            user.setImageUrl(imageUrl != null ? imageUrl : "");
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_USER_EMAIL + "=?", 
                               new String[]{email}, null, null, null);
        
        boolean userExists = cursor.moveToFirst();
        cursor.close();
        db.close();
        
        // For demo purposes, accept any password if user exists
        // In production, implement proper password hashing and verification
        return userExists;
    }
}