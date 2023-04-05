package com.frimpong.hot_quakes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

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
    EarthquakeItem earthquakeItem;
    ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        backButton = findViewById(R.id.back_button);
        setSupportActionBar(toolbar);
//        initializeMap(savedInstanceState);
        retrievePassedItem();

        System.out.println(earthquakeItem);
        TextView title = findViewById(R.id.page_title);
        title.setText(earthquakeItem.getTitle());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    public void initializeMap(Bundle savedInstanceState){
        // Initialize the map view
//        mapView = findViewById(R.id.map_view);
//        mapView.onCreate(savedInstanceState);
//
//        // Get the GoogleMap object
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                map = googleMap;
//                // Add a marker to the map at a specific location
//                LatLng location = new LatLng(37.7749, -122.4194);
//                MarkerOptions markerOptions = new MarkerOptions()
//                        .position(location)
//                        .title("Marker Title");
//                map.addMarker(markerOptions);
//                // Move the camera to the marker location
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f));
//            }
//        });

    }

    public void retrievePassedItem(){
        String title ="", desc ="", pubDate ="", url="";
        Double depth=0.0, mag=0.0, _long=0.0, lat=0.0;
        Intent intent = getIntent();
        if(intent == null) return;
        if(intent.hasExtra(Constants.TITLE)) title = intent.getStringExtra(Constants.TITLE);
        if(intent.hasExtra(Constants.DESC)) desc = intent.getStringExtra(Constants.DESC);
        if(intent.hasExtra(Constants.PUB_DATE)) pubDate = intent.getStringExtra(Constants.PUB_DATE);
        if(intent.hasExtra(Constants.DEPTH)) depth = intent.getDoubleExtra(Constants.DEPTH,0.0);
        if(intent.hasExtra(Constants.MAGNITUDE)) mag = intent.getDoubleExtra(Constants.MAGNITUDE,0.0);
        if(intent.hasExtra(Constants.URL)) url = intent.getStringExtra(Constants.URL);
        if(intent.hasExtra(Constants.LONG)) _long = intent.getDoubleExtra(Constants.LONG,0.0);
        if(intent.hasExtra(Constants.LAT)) lat = intent.getDoubleExtra(Constants.LAT,0.0 );
        earthquakeItem = new EarthquakeItem(title, desc,pubDate,url);
        earthquakeItem.setDepth(depth);
        earthquakeItem.setMagnitude(mag);
        earthquakeItem.setLongitude(_long);
        earthquakeItem.setLatitude(lat);

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }


}