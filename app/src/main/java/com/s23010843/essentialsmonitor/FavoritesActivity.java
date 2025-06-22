package com.s23010843.essentialsmonitor;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private ListView favoritesListView;
    private TextView emptyFavoritesText;

    private ArrayList<String> favoriteProducts;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritesListView = findViewById(R.id.favorites_list_view);
        emptyFavoritesText = findViewById(R.id.empty_favorites_text);
        Button clearFavoritesButton = findViewById(R.id.clear_favorites_button);

        favoriteProducts = new ArrayList<>();

        loadFavorites();

        clearFavoritesButton.setOnClickListener(v -> {
            favoriteProducts.clear();
            adapter.notifyDataSetChanged();
            updateEmptyView();
        });
    }

    private void loadFavorites() {
        // TODO: Load favorites from a database or SharedPreferences in a real app
        favoriteProducts.add("Milk - $3.99 at Walmart");
        favoriteProducts.add("Bread - $2.49 at Target");
        favoriteProducts.add("Gasoline - $3.45 at Shell");
        favoriteProducts.add("Eggs - $4.99 at Kroger");

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                favoriteProducts
        );

        favoritesListView.setAdapter(adapter);
        updateEmptyView();
    }

    private void updateEmptyView() {
        if (favoriteProducts.isEmpty()) {
            favoritesListView.setVisibility(View.GONE);
            emptyFavoritesText.setVisibility(View.VISIBLE);
        } else {
            favoritesListView.setVisibility(View.VISIBLE);
            emptyFavoritesText.setVisibility(View.GONE);
        }
    }
}