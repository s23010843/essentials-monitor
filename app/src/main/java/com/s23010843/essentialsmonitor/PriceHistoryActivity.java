package com.s23010843.essentialsmonitor;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PriceHistoryActivity extends AppCompatActivity {
    private TextView productNameTextView, currentPriceTextView, averagePriceTextView;
    private TextView highestPriceTextView, lowestPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_history);

        productNameTextView = findViewById(R.id.product_name_text_view);
        currentPriceTextView = findViewById(R.id.current_price_text_view);
        averagePriceTextView = findViewById(R.id.average_price_text_view);
        highestPriceTextView = findViewById(R.id.highest_price_text_view);
        lowestPriceTextView = findViewById(R.id.lowest_price_text_view);

        loadPriceHistory();
    }

    private void loadPriceHistory() {
        // TODO: Load price history from database
        productNameTextView.setText("Milk (1 Gallon)");
        currentPriceTextView.setText("Current: $3.99");
        averagePriceTextView.setText("30-day avg: $4.12");
        highestPriceTextView.setText("Highest: $4.49");
        lowestPriceTextView.setText("Lowest: $3.79");
    }
}
