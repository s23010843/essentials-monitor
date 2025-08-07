package com.s23010843.essentialsmonitor;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PriceReportActivity extends AppCompatActivity {
    private EditText productNameEditText, priceEditText, storeNameEditText;
    private Spinner categorySpinner;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_submission);

        databaseHelper = new DatabaseHelper(this);

        productNameEditText = findViewById(R.id.product_name_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        storeNameEditText = findViewById(R.id.store_name_edit_text);
        categorySpinner = findViewById(R.id.category_spinner);
        Button submitReportButton = findViewById(R.id.submit_report_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        setupCategorySpinner();

        submitReportButton.setOnClickListener(v -> submitPriceReport());

        cancelButton.setOnClickListener(v -> {
            // Cancel the report and return to previous activity
            finish();
        });
    }

    private void setupCategorySpinner() {
        String[] categories = {"Food & Beverages", "Personal Care", "Household Items", "Electronics", "Clothing", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void submitPriceReport() {
        String productName = productNameEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String storeName = storeNameEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        // Enhanced validation
        if (productName.isEmpty() || priceStr.isEmpty() || storeName.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (productName.length() < 2) {
            Toast.makeText(this, "Product name must be at least 2 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (storeName.length() < 2) {
            Toast.makeText(this, "Store name must be at least 2 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            
            // Validate price range
            if (price <= 0) {
                Toast.makeText(this, "Price must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (price > 10000) {
                Toast.makeText(this, "Price seems too high. Please verify.", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Add price report to database
            long reportId = databaseHelper.addPriceReport(productName, price, storeName, category);
            if (reportId != -1) {
                Toast.makeText(this, "Price report submitted successfully! Thank you for contributing.", Toast.LENGTH_LONG).show();
                clearFields();
                new android.os.Handler().postDelayed(this::finish, 1500);
            } else {
                Toast.makeText(this, "Could not submit price report. Please check your network or try again later.", Toast.LENGTH_LONG).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price (numbers only)", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        productNameEditText.setText("");
        priceEditText.setText("");
        storeNameEditText.setText("");
        categorySpinner.setSelection(0);
    }
}