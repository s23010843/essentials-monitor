package com.s23010843.essentialsmonitor;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private ListView resultsListView;
    private ArrayList<String> searchResults;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        searchEditText = findViewById(R.id.search_edit_text);
        Button searchButton = findViewById(R.id.search_button);
        resultsListView = findViewById(R.id.search_results_list_view);
        searchResults = new ArrayList<>();

        databaseHelper = new DatabaseHelper(this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();

        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        List<DatabaseHelper.Product> matchedProducts = databaseHelper.searchProducts(query);

        searchResults.clear();
        if (matchedProducts.isEmpty()) {
            searchResults.add("No products found for \"" + query + "\".");
        } else {
            for (DatabaseHelper.Product product : matchedProducts) {
                String result = product.getName() + " - " + product.getCategory() + " (" + product.getBrand() + ")";
                searchResults.add(result);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                searchResults
        );
        resultsListView.setAdapter(adapter);
    }
}