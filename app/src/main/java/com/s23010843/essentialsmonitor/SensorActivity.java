package com.s23010843.essentialsmonitor;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity {

    TextView sensorName, sensorValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        sensorName = findViewById(R.id.sensorName);
        sensorValue = findViewById(R.id.sensorValue);

        double lat = getIntent().getDoubleExtra("lat", 0.0);
        double lon = getIntent().getDoubleExtra("lon", 0.0);

        sensorName.setText("Sensor: Temperature");
        // Simulate based on coordinates
        sensorValue.setText("Value at [" + lat + ", " + lon + "]: " + simulateTemperature(lat, lon));
    }

    private String simulateTemperature(double lat, double lon) {
        // Dummy logic
        double temp = 20 + (lat + lon) % 10;
        return String.format("%.1f°C", temp);
    }
}