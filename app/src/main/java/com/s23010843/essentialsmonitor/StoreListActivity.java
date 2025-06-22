package com.s23010843.essentialsmonitor;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class StoreListActivity extends AppCompatActivity {
    private ArrayList<String> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);

        ListView storeListView = findViewById(R.id.store_list_view);
        storeList = new ArrayList<>();

        loadNearbyStores();

        // Set adapter after loading stores
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                storeList
        );
        storeListView.setAdapter(adapter);
    }

    private void loadNearbyStores() {
        // TODO: Load nearby stores dynamically from your DB or API
        storeList.add("Walmart - 0.5 miles");
        storeList.add("Target - 0.8 miles");
        storeList.add("Kroger - 1.2 miles");
        storeList.add("CVS Pharmacy - 1.5 miles");
    }
}