package com.s23010843.essentialsmonitor;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;

import com.s23010843.essentialsmonitor.Store;
import com.s23010843.essentialsmonitor.DatabaseHelper;

import androidx.appcompat.app.AppCompatActivity;

public class StoreManagementActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    private EditText storeNameEditText, addressEditText;
    private Button addStoreButton, updateStoreButton, deleteStoreButton;
    private RecyclerView storesRecyclerView;
    private StoreAdapter storeAdapter;
    private int selectedStoreId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button saveStoreButton = findViewById(R.id.save_store_button);
        if (saveStoreButton != null) {
            saveStoreButton.setOnClickListener(v -> {
                String name = storeNameEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                if (selectedStoreId == -1) {
                    // Add new store
                    if (name.isEmpty() || address.isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    long id = databaseHelper.addStoreByName(name, address);
                    if (id != -1) {
                        Toast.makeText(this, "Store added successfully!", Toast.LENGTH_SHORT).show();
                        clearFields();
                        loadStores();
                    } else {
                        Toast.makeText(this, "Failed to add store. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Update existing store
                    if (name.isEmpty() || address.isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int rows = databaseHelper.updateStoreByName(selectedStoreId, name, address);
                    if (rows > 0) {
                        Toast.makeText(this, "Store updated successfully!", Toast.LENGTH_SHORT).show();
                        clearFields();
                        loadStores();
                    } else {
                        Toast.makeText(this, "Failed to update store. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_management);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        databaseHelper = new DatabaseHelper(this);

        storeNameEditText = findViewById(R.id.store_name_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        addStoreButton = findViewById(getResources().getIdentifier("add_store_button", "id", getPackageName()));
        updateStoreButton = findViewById(getResources().getIdentifier("update_store_button", "id", getPackageName()));
        deleteStoreButton = findViewById(getResources().getIdentifier("delete_store_button", "id", getPackageName()));
        storesRecyclerView = findViewById(R.id.stores_recycler_view);

        storesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        storeAdapter = new StoreAdapter();
        storesRecyclerView.setAdapter(storeAdapter);

        loadStores();

        if (addStoreButton != null) {
            addStoreButton.setOnClickListener(v -> addStore());
        }
        if (updateStoreButton != null) {
            updateStoreButton.setOnClickListener(v -> updateStore());
        }
        if (deleteStoreButton != null) {
            deleteStoreButton.setOnClickListener(v -> deleteStore());
        }
    }

    private void loadStores() {
        List<Store> stores = databaseHelper.getAllStores();
        storeAdapter.setStores(stores);
    }

    private void addStore() {
        String name = storeNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        if (name.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        long id = databaseHelper.addStoreByName(name, address);
        if (id != -1) {
            Toast.makeText(this, "Store added", Toast.LENGTH_SHORT).show();
            clearFields();
            loadStores();
        } else {
            Toast.makeText(this, "Failed to add store", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStore() {
        if (selectedStoreId == -1) {
            Toast.makeText(this, "Select a store to update", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = storeNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        if (name.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        int rows = databaseHelper.updateStoreByName(selectedStoreId, name, address);
        if (rows > 0) {
            Toast.makeText(this, "Store updated", Toast.LENGTH_SHORT).show();
            clearFields();
            loadStores();
        } else {
            Toast.makeText(this, "Failed to update store", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteStore() {
        if (selectedStoreId == -1) {
            Toast.makeText(this, "Select a store to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseHelper.deleteStore(selectedStoreId);
        Toast.makeText(this, "Store deleted", Toast.LENGTH_SHORT).show();
        clearFields();
        loadStores();
    }

    private void clearFields() {
        storeNameEditText.setText("");
        addressEditText.setText("");
        // latitudeEditText and longitudeEditText removed
        selectedStoreId = -1;
    }

    // StoreAdapter inner class for RecyclerView
    private class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
        private List<Store> stores;

        public void setStores(List<Store> stores) {
            this.stores = stores;
            notifyDataSetChanged();
        }

        @Override
        public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
            return new StoreViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StoreViewHolder holder, int position) {
            Store store = stores.get(position);
            holder.nameText.setText(store.getName());
            holder.addressText.setText(store.getAddress());
            holder.itemView.setOnClickListener(v -> {
                selectedStoreId = store.getId();
                storeNameEditText.setText(store.getName());
                addressEditText.setText(store.getAddress());
            });
        }

        @Override
        public int getItemCount() {
            return stores == null ? 0 : stores.size();
        }

        class StoreViewHolder extends RecyclerView.ViewHolder {
            TextView nameText, addressText;
            StoreViewHolder(View itemView) {
                super(itemView);
                nameText = itemView.findViewById(android.R.id.text1);
                addressText = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}
