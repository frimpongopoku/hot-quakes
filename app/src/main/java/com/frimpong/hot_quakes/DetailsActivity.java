package com.frimpong.hot_quakes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        FrameLayout bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setPeekHeight(200); // Initial height of bottom sheet when docked
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // Bottom sheet is collapsed by default

    }
}