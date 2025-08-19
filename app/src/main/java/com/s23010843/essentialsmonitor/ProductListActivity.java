package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static utils.ApiConfig.PRODUCTS_ENDPOINT;

public class ProductListActivity extends AppCompatActivity {
    private static final String TAG = "ProductListActivity";
    private static final String API_URL = PRODUCTS_ENDPOINT;

    ListView productList;
    Button addProductBtn;
    ArrayList<String> productDisplayList = new ArrayList<>();
    ArrayList<String> productIds = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productList = findViewById(R.id.productList);
        addProductBtn = findViewById(R.id.addProductBtn);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productDisplayList);
        productList.setAdapter(adapter);

        loadProductsFromApi();

        addProductBtn.setOnClickListener(v -> startActivity(new Intent(this, AddProductActivity.class)));

        productList.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < productIds.size()) {
                String selectedId = productIds.get(position);
                Intent intent = new Intent(this, EditProductActivity.class);
                intent.putExtra("product_id", selectedId);
                startActivity(intent);
            } else {
                Log.e(TAG, "Clicked position out of bounds: " + position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProductsFromApi();
    }

    private void loadProductsFromApi() {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned: " + responseCode);
                    mainHandler.post(() -> toast("Server error: " + responseCode));
                    return;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                JSONArray jsonArray = new JSONArray(sb.toString());
                ArrayList<String> tempDisplay = new ArrayList<>();
                ArrayList<String> tempIds = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject product = jsonArray.getJSONObject(i);
                    tempIds.add(product.getString("_id"));
                    tempDisplay.add(
                            product.getString("name") +
                                    "\n$" + product.getDouble("price") +
                                    "\n" + product.optString("location", "N/A")
                    );
                }

                mainHandler.post(() -> {
                    productIds.clear();
                    productDisplayList.clear();
                    productIds.addAll(tempIds);
                    productDisplayList.addAll(tempDisplay);
                    adapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                Log.e(TAG, "Error fetching products", e);
                mainHandler.post(() -> toast("Failed to load products"));
            }
        }).start();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}