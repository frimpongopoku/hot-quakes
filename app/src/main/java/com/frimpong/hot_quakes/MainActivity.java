package com.frimpong.hot_quakes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import com.frimpong.hot_quakes.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int START_DATE_PICKER_ID = 0;
    private static final int END_DATE_PICKER_ID = 1;
    private int selectedDatePickerId = START_DATE_PICKER_ID;
    private int startYear, startMonth, startDay;
    private int endYear, endMonth, endDay;
    // Create a Calendar instance
    Calendar calendar = Calendar.getInstance();

    LoadingModel loading = new LoadingModel();
    ActivityMainBinding binding;
    EarthquakeViewModel earthquakeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Bind the loadingModel instance to the layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLoading(loading);
        initializeToolbar();
        setupRecyclerView();

    }

    public void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final EarthquakeListAdapter adapter = new EarthquakeListAdapter(this);
        recyclerView.setAdapter(adapter);

        earthquakeViewModel = new ViewModelProvider(this).get(EarthquakeViewModel.class);
        earthquakeViewModel.getEarthquakes().observe(this, new Observer<List<EarthquakeItem>>() {
            @Override
            public void onChanged(List<EarthquakeItem> earthquakes) {
                adapter.setEarthquakes(earthquakes);
            }
        });
    }


    public void testClick(View v){
        List<EarthquakeItem> list = earthquakeViewModel.getEarthquakes().getValue();
        EarthquakeItem item = new EarthquakeItem();
        item.setTitle("Earthquake in Germany");
        item.setPubDate("1st February 4400");
        item.setDepth(45.6);
        item.setMagnitude(7.0);
        list.add(item);
        earthquakeViewModel.setEarthquakes(list);
//        Boolean b = !loading.isLoading();
//        loading.setLoading(b);
//        Log.d("PRINTING", "testClick: Just clicked you still! "+b);
//        binding.notifyPropertyChanged(BR.loading);
    }

    private void showStartDatePickerDialog() {
        showDatePickerDialog(startYear, startMonth, startDay);
    }

    private void showEndDatePickerDialog() {
        showDatePickerDialog(endYear, endMonth, endDay);
    }

    private void showDatePickerDialog(int year, int month, int day) {
        calendar.set(Calendar.YEAR, 2023);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (selectedDatePickerId == START_DATE_PICKER_ID) {
            Log.d("PRINTING","You just chose date for start date");
            startYear = year;
            startMonth = month;
            startDay = dayOfMonth;
            selectedDatePickerId = END_DATE_PICKER_ID;
            showEndDatePickerDialog();
        } else if (selectedDatePickerId == END_DATE_PICKER_ID) {
            Log.d("PRINTING", "You just chose date for END DATE GEE, TOO COOL");
            endYear = year;
            endMonth = month;
            endDay = dayOfMonth;

            // Do something with the selected dates here
        }
    }


    public void  initializeToolbar(){
        // Initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the click listener for the date picker button
        ImageButton datePickerButton = (ImageButton) findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePickerDialog();
            }
        });
    }
}