package com.s23010843.essentialsmonitor;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapManager implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Context context;
    private List<Marker> storeMarkers;
    private OnMapReadyListener mapReadyListener;

    public interface OnMapReadyListener {
        void onMapReady(GoogleMap googleMap);
    }

    public GoogleMapManager(Context context, OnMapReadyListener listener) {
        this.context = context;
        this.mapReadyListener = listener;
        this.storeMarkers = new ArrayList<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable location controls if permission is granted
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // Set default location (example: New York City)
        LatLng defaultLocation = new LatLng(40.7128, -74.0060);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));

        if (mapReadyListener != null) {
            mapReadyListener.onMapReady(googleMap);
        }

        loadNearbyStores();
    }

    public void updateUserLocation(Location location) {
        if (mMap != null && location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            loadNearbyStores(userLocation);
        }
    }

    private void loadNearbyStores() {
        // Default stores for demo
        addStoreMarker(new LatLng(40.7589, -73.9851), "Walmart Supercenter", "24/7 Grocery Store");
        addStoreMarker(new LatLng(40.7505, -73.9934), "Target", "Department Store");
        addStoreMarker(new LatLng(40.7614, -73.9776), "CVS Pharmacy", "Pharmacy & Convenience");
        addStoreMarker(new LatLng(40.7282, -73.9942), "Whole Foods Market", "Organic Grocery");
    }

    private void loadNearbyStores(LatLng userLocation) {
        clearStoreMarkers();

        // Add stores around user location for demo
        double lat = userLocation.latitude;
        double lng = userLocation.longitude;

        addStoreMarker(new LatLng(lat + 0.01, lng + 0.01), "Local Grocery", "Neighborhood Store");
        addStoreMarker(new LatLng(lat - 0.01, lng + 0.02), "Gas Station", "Fuel & Convenience");
        addStoreMarker(new LatLng(lat + 0.02, lng - 0.01), "Pharmacy Plus", "Medicine & Health");
    }

    private void addStoreMarker(LatLng position, String title, String snippet) {
        if (mMap != null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(title)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            if (marker != null) {
                storeMarkers.add(marker);
            }
        }
    }

    private void clearStoreMarkers() {
        for (Marker marker : storeMarkers) {
            marker.remove();
        }
        storeMarkers.clear();
    }

    public void searchStoresNearLocation(LatLng location, String query) {
        clearStoreMarkers();

        if (query.toLowerCase().contains("pharmacy")) {
            addStoreMarker(new LatLng(location.latitude + 0.005, location.longitude),
                    "CVS Pharmacy", "Prescription & Health Products");
            addStoreMarker(new LatLng(location.latitude - 0.005, location.longitude + 0.01),
                    "Walgreens", "Pharmacy & Convenience");
        } else if (query.toLowerCase().contains("grocery")) {
            addStoreMarker(new LatLng(location.latitude + 0.01, location.longitude + 0.01),
                    "Walmart", "Grocery & General Merchandise");
            addStoreMarker(new LatLng(location.latitude - 0.01, location.longitude),
                    "Kroger", "Supermarket");
        } else {
            // Default or no matches
            loadNearbyStores(location);
        }
    }

    public GoogleMap getMap() {
        return mMap;
    }
}