package com.frimpong.hot_quakes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class DetailsActivity extends AppCompatActivity {

    private MapView mapView;
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        // Initialize the map view
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        // Get the GoogleMap object
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                // Add a marker to the map at a specific location
                LatLng location = new LatLng(37.7749, -122.4194);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(location)
                        .title("Marker Title");
                map.addMarker(markerOptions);
                // Move the camera to the marker location
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}