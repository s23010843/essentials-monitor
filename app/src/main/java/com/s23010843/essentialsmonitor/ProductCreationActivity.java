package com.s23010843.essentialsmonitor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProductCreationActivity extends AppCompatActivity {
    private EditText productNameEditText, brandEditText, descriptionEditText;
    private Spinner categorySpinner;
    private Button saveButton;
    private DatabaseHelper databaseHelper;
    private android.net.Uri selectedImageUri;
    private android.widget.ImageView productImageView;
    private androidx.activity.result.ActivityResultLauncher<android.content.Intent> imagePickerLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_creation);
        
        databaseHelper = new DatabaseHelper(this);
        
        initializeViews();
        setupCategorySpinner();

        // Image picker logic
        Button selectImageButton = findViewById(R.id.select_image_button);
        productImageView = findViewById(R.id.product_image_view);
        imagePickerLauncher = registerForActivityResult(
            new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    productImageView.setImageURI(selectedImageUri);
                }
            }
        );
        selectImageButton.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
        
        saveButton.setOnClickListener(v -> saveProduct());
    }
    
    private void initializeViews() {
        productNameEditText = findViewById(R.id.product_name_edit_text);
        brandEditText = findViewById(R.id.brand_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        categorySpinner = findViewById(R.id.category_spinner);
        saveButton = findViewById(R.id.save_button);
    }
    
    private void setupCategorySpinner() {
        String[] categories = {
            "Food & Groceries",
            "Medicine & Health",
            "Household Items",
            "Personal Care",
            "Baby Products",
            "Pet Supplies",
            "Automotive",
            "Electronics",
            "Other"
        };
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }
    
    private void saveProduct() {
        String productName = productNameEditText.getText().toString().trim();
        String brand = brandEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String imageUriString = selectedImageUri != null ? selectedImageUri.toString() : null;

        if (productName.isEmpty()) {
            Toast.makeText(this, "Product name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category.equals("Select Category")) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save product to database with image
        long productId = databaseHelper.addProduct(productName, category, brand, description, imageUriString);

        if (productId != -1) {
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
        }
    }
}