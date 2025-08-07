
package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.s23010843.essentialsmonitor.DatabaseHelper;
import com.s23010843.essentialsmonitor.ProductAdapter;
import com.s23010843.essentialsmonitor.ProductCreationActivity;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView productsRecyclerView;
    private TextView storeNameTextView;
    private DatabaseHelper databaseHelper;
    private ProductAdapter productAdapter;
    private String storeName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        int layoutId = getResources().getIdentifier("activity_product_catalog", "layout", getPackageName());
        setContentView(layoutId);
        
        databaseHelper = new DatabaseHelper(this);
        
        // Get store info from intent
        int storeId = getIntent().getIntExtra("store_id", -1);
        storeName = getIntent().getStringExtra("store_name");

        // Defensive checks to avoid crashes
        if (storeName == null) {
            storeName = "Unknown Store";
        }
        if (storeId == -1) {
            Toast.makeText(this, "Showing all products. Select a store for more details.", Toast.LENGTH_LONG).show();
        }

        // Initialize views safely - using resource names as fallback
        productsRecyclerView = findViewById(getResources().getIdentifier("search_results_recycler_view", "id", getPackageName()));
        
        // Defensive: storeNameTextView may not exist in layout, so check and assign if present
        int storeNameId = getResources().getIdentifier("store_name_text_view", "id", getPackageName());
        if (storeNameId != 0) {
            storeNameTextView = findViewById(storeNameId);
            if (storeNameTextView != null && storeName != null) {
                storeNameTextView.setText("Products at " + storeName);
            }
        }
        
        Button addProductButton = null;
        int addButtonId = getResources().getIdentifier("add_product_button", "id", getPackageName());
        if (addButtonId != 0) {
            addProductButton = findViewById(addButtonId);
        }

        setupRecyclerView();
        loadProducts();

        // Only set listener if addProductButton is present in layout
        if (addProductButton != null) {
            addProductButton.setOnClickListener(v -> startActivity(new Intent(ProductListActivity.this, ProductCreationActivity.class)));
        }
    }
    
    private void initializeViews() {

        if (storeName != null) {
            storeNameTextView.setText("Products at " + storeName);
        }
    }
    
    private void setupRecyclerView() {
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this);
        productsRecyclerView.setAdapter(productAdapter);
    }
    
    private void loadProducts() {
        // Load all products for now - in a real app you might filter by store
        List<Product> products = databaseHelper.getAllProducts();
        productAdapter.updateProducts(products);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadProducts(); // Refresh the list
        }
    }
}
