package com.s23010843.essentialsmonitor;

import static utils.ApiConfig.PRODUCTS_ENDPOINT;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class CustomerProductActivity extends AppCompatActivity {

    ListView customerProductList;
    ArrayList<String> products = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private static final String API_URL = PRODUCTS_ENDPOINT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_product_list);

        customerProductList = findViewById(R.id.customerProductList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        customerProductList.setAdapter(adapter);

        loadProductsFromApi();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadProductsFromApi() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(API_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    int responseCode = conn.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        Log.e("API_ERROR", "Response Code: " + responseCode);
                        return null;
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                    in.close();
                    return sb.toString();

                } catch (Exception e) {
                    Log.e("API_ERROR", "Exception: ", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result == null) {
                    toast("Failed to load products");
                    return;
                }

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    products.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject product = jsonArray.getJSONObject(i);
                        String name = product.getString("name");
                        double price = product.getDouble("price");
                        String location = product.optString("location", "Location not available");

                        products.add(name + "\n$" + price + "\n" + location);
                    }

                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Log.e("PARSE_ERROR", "Error parsing products", e);
                    toast("Error parsing product data");
                }
            }
        }.execute();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}