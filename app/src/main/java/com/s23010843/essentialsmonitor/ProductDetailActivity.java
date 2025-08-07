package com.s23010843.essentialsmonitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView productNameTextView, priceTextView, storeTextView, lastUpdatedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);

        productNameTextView = findViewById(R.id.product_name_text_view);
        priceTextView = findViewById(R.id.price_text_view);
        storeTextView = findViewById(R.id.store_text_view);
        lastUpdatedTextView = findViewById(R.id.last_updated_text_view);
        Button reportPriceButton = findViewById(R.id.report_price_button);
        Button viewLocationButton = findViewById(R.id.view_location_button);
        Button addToFavoritesButton = findViewById(R.id.add_to_favorites_button);
        final ImageView productImage = findViewById(R.id.product_image);

        reportPriceButton.setOnClickListener(v ->
            startActivity(new Intent(ProductDetailActivity.this, PriceReportActivity.class)));

        viewLocationButton.setOnClickListener(v ->
            startActivity(new Intent(ProductDetailActivity.this, MapViewActivity.class)));

        addToFavoritesButton.setOnClickListener(v ->
            Toast.makeText(ProductDetailActivity.this, "Product added to favorites!", Toast.LENGTH_SHORT).show());

        productImage.setOnClickListener(v -> {
            // Example: Change image to a placeholder (replace with image picker logic as needed)
            productImage.setImageResource(R.drawable.ic_product_placeholder);
            Toast.makeText(ProductDetailActivity.this, "Product image changed.", Toast.LENGTH_SHORT).show();
        });

        loadProductDetails();
    }

    @SuppressLint("SetTextI18n")
    private void loadProductDetails() {
        // Load product details from intent extras or database
        productNameTextView.setText("Sample Product");
        priceTextView.setText("$9.99");
        storeTextView.setText("Sample Store");
        lastUpdatedTextView.setText("Last updated: 2 hours ago");
    }
}