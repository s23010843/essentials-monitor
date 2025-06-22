package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView productNameTextView, priceTextView, storeTextView, lastUpdatedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productNameTextView = findViewById(R.id.product_name_text_view);
        priceTextView = findViewById(R.id.price_text_view);
        storeTextView = findViewById(R.id.store_text_view);
        lastUpdatedTextView = findViewById(R.id.last_updated_text_view);
        Button reportPriceButton = findViewById(R.id.report_price_button);
        Button viewLocationButton = findViewById(R.id.view_location_button);

        reportPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, ReportPriceActivity.class));
            }
        });

        viewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, MapViewActivity.class));
            }
        });

        loadProductDetails();
    }

    private void loadProductDetails() {
        // TODO: Load product details from intent extras or database
        productNameTextView.setText("Sample Product");
        priceTextView.setText("$9.99");
        storeTextView.setText("Sample Store");
        lastUpdatedTextView.setText("Last updated: 2 hours ago");
    }
}
