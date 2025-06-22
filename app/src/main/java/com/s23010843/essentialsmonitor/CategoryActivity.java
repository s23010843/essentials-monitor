package com.s23010843.essentialsmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private GridView categoryGridView;
    private ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryGridView = findViewById(R.id.category_grid_view);
        categories = new ArrayList<>();

        setupCategories();
        setupGridView();
    }

    private void setupCategories() {
        categories.add("Food & Groceries");
        categories.add("Medicine & Healthcare");
        categories.add("Fuel & Gas");
        categories.add("Household Items");
        categories.add("Personal Care");
        categories.add("Baby Products");
        categories.add("Pet Supplies");
        categories.add("Electronics");
    }

    private void setupGridView() {
        // TODO: Create custom adapter for grid view
        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories.get(position);
                Intent intent = new Intent(CategoryActivity.this, ProductSearchActivity.class);
                intent.putExtra("category", selectedCategory);
                startActivity(intent);
            }
        });
    }
}
