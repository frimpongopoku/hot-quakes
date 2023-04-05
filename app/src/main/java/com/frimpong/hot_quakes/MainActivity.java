package com.frimpong.hot_quakes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;


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
    ProgressBar progressBar;
    RecyclerView recyclerView;
    EarthquakeViewModel earthquakeViewModel;
    private final Handler handler = new Handler();

    EditText searchBox;
    // Set a delay time (in milliseconds) for the debounce function
    private final int debounceDelay = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbar();
        setupSearchBox();
        setupRecyclerView();
        progressBar = findViewById(R.id.progressBar);

    }


    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Perform action after debounce time
            // Example: Do a search based on the text in the EditText
            String searchText = searchBox.getText().toString();
            searchByText(searchText);
        }
    };

    public void searchByText (String text) {
        List<EarthquakeItem> foundItems = earthquakeViewModel.searchEarthquakesByTitle(text);
        earthquakeViewModel.setEarthquakes(foundItems);

    }

    public void setupSearchBox(){
        searchBox = findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Cancel any pending debounce action
                handler.removeCallbacks(runnable);

                // Schedule a new debounce action
                handler.postDelayed(runnable, debounceDelay);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        });
    }



    public void setupRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final EarthquakeListAdapter adapter = new EarthquakeListAdapter(this);
        recyclerView.setAdapter(adapter);

        earthquakeViewModel = new ViewModelProvider(this).get(EarthquakeViewModel.class);
        earthquakeViewModel.getEarthquakes().observe(this, new Observer<List<EarthquakeItem>>() {
            @Override
            public void onChanged(List<EarthquakeItem> earthquakes) {
                if(earthquakes.size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.setEarthquakes(earthquakes);
            }
        });
    }


    public void testClick(View v){
        int visi = progressBar.getVisibility();
        if (visi == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

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