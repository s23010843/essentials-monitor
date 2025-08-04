package com.s23010843.essentialsmonitor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import java.util.List;

public class SensorManager implements LocationListener, SensorEventListener {
    private final Context context;
    private final LocationManager locationManager;
    private final android.hardware.SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private final SensorDataListener sensorDataListener;

    // Location update settings
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000; // 1 second
    private static final float MIN_DISTANCE_CHANGE = 10; // 10 meters

    public interface SensorDataListener {
        void onLocationChanged(Location location);
        void onLocationPermissionRequired();
        void onAccelerometerDataChanged(float x, float y, float z);
        void onGyroscopeDataChanged(float x, float y, float z);
        void onSensorError(String error);
    }

    public SensorManager(Context context, SensorDataListener listener) {
        this.context = context;
        this.sensorDataListener = listener;
        
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (android.hardware.SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        
        initializeSensors();
    }

    private void initializeSensors() {
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }
    }

    public void startLocationUpdates() {
        if (locationManager == null) {
            if (sensorDataListener != null) {
                sensorDataListener.onSensorError("Sensor: LocationManager not available");
            }
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            if (sensorDataListener != null) {
                sensorDataListener.onLocationPermissionRequired();
            }
            return;
        }

        try {
            // Request location updates from GPS
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE,
                        this
                );
            }

            // Request location updates from Network
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE,
                        this
                );
            }

            // Get last known location
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null && sensorDataListener != null) {
                sensorDataListener.onLocationChanged(lastKnownLocation);
            }

        } catch (SecurityException e) {
            if (sensorDataListener != null) {
                sensorDataListener.onSensorError("Sensor: Location permission denied: " + e.getMessage());
            }
        }
    }

    public void stopLocationUpdates() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(this);
            } catch (SecurityException e) {
                // Permission might have been revoked
            }
        }
    }

    public Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        try {
            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            // Return the most accurate location
            if (gpsLocation != null && networkLocation != null) {
                return gpsLocation.getAccuracy() < networkLocation.getAccuracy() ? gpsLocation : networkLocation;
            } else if (gpsLocation != null) {
                return gpsLocation;
            } else {
                return networkLocation;
            }
        } catch (SecurityException e) {
            return null;
        }
    }

    public void startSensorUpdates() {
        if (sensorManager != null) {
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, android.hardware.SensorManager.SENSOR_DELAY_NORMAL);
            }
            if (gyroscope != null) {
                sensorManager.registerListener(this, gyroscope, android.hardware.SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    public void stopSensorUpdates() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    public boolean isLocationEnabled() {
        if (locationManager == null) return false;
        
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
               locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public List<String> getAvailableProviders() {
        if (locationManager != null) {
            return locationManager.getProviders(true);
        }
        return null;
    }

    // LocationListener methods
    @Override
    public void onLocationChanged(Location location) {
        if (sensorDataListener != null) {
            sensorDataListener.onLocationChanged(location);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle provider status changes if needed
        // This method is deprecated in API level 29+ but required for compatibility
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle provider enabled
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle provider disabled
    }

    // SensorEventListener methods
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensorDataListener == null) return;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorDataListener.onAccelerometerDataChanged(
                        event.values[0], event.values[1], event.values[2]);
                break;
            case Sensor.TYPE_GYROSCOPE:
                sensorDataListener.onGyroscopeDataChanged(
                        event.values[0], event.values[1], event.values[2]);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    public void cleanup() {
        stopLocationUpdates();
        stopSensorUpdates();
    }
}
