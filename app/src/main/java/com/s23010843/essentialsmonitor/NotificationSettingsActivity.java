package com.s23010843.essentialsmonitor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationSettingsActivity extends AppCompatActivity {

    private CheckBox priceAlertsCheckBox, stockAlertsCheckBox, newsAlertsCheckBox;
    private SeekBar radiusSeekBar;
    private TextView radiusValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        priceAlertsCheckBox = findViewById(R.id.price_alerts_checkbox);
        stockAlertsCheckBox = findViewById(R.id.stock_alerts_checkbox);
        newsAlertsCheckBox = findViewById(R.id.news_alerts_checkbox);
        radiusSeekBar = findViewById(R.id.radius_seekbar);
        radiusValueTextView = findViewById(R.id.radius_value_text_view);

        setupListeners();
        loadCurrentSettings(); // optional
    }

    private void setupListeners() {
        priceAlertsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                saveNotificationSetting("price_alerts", isChecked));

        stockAlertsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                saveNotificationSetting("stock_alerts", isChecked));

        newsAlertsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                saveNotificationSetting("news_alerts", isChecked));

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusValueTextView.setText(progress + " miles");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                saveNotificationSetting("radius", seekBar.getProgress());
            }
        });
    }

    private void loadCurrentSettings() {
        // Example default
        radiusValueTextView.setText(R.string._5_miles);
    }

    private void saveNotificationSetting(String key, boolean value) {
        Toast.makeText(this, "Saved: " + key + " = " + value, Toast.LENGTH_SHORT).show();
    }

    private void saveNotificationSetting(String key, int value) {
        Toast.makeText(this, "Saved: " + key + " = " + value + " miles", Toast.LENGTH_SHORT).show();
    }
}