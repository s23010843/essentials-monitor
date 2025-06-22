package com.s23010843.essentialsmonitor;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReportPriceActivity extends AppCompatActivity {
    private EditText productNameEditText, priceEditText, storeNameEditText;
    private Spinner categorySpinner;
    private Button submitReportButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_price);

        databaseHelper = new DatabaseHelper(this);

        productNameEditText = findViewById(R.id.product_name_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        storeNameEditText = findViewById(R.id.store_name_edit_text);
        categorySpinner = findViewById(R.id.category_spinner);
        submitReportButton = findViewById(R.id.submit_report_button);

        setupCategorySpinner();

        submitReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPriceReport();
            }
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

        if (productName.isEmpty() || priceStr.isEmpty() || storeName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            
            // Add price report to database
            long reportId = databaseHelper.addPriceReport(productName, price, storeName, category);
            if (reportId != -1) {
                Toast.makeText(this, "Price report submitted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to submit price report", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
        }
    }
}
