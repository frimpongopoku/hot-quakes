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
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
    FloatingActionButton clearButton, landScapeDeepest, landscapeShallowest;
    TextView notFound, sort_desc;

    RelativeLayout mainDiv;

    Button deepestButton, shallowestButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        notFound = findViewById(R.id.notFound);
        mainDiv = findViewById(R.id.main_div);

        initializeToolbar();
        setupSearchBox();
        setupRecyclerView();
        setupClearButton();
        setupSortButtons();

    }


    public boolean landscapeMode(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public void setupLandscapeSortButtons(){
        landScapeDeepest = findViewById(R.id.land_deepest);
        landscapeShallowest = findViewById(R.id.land_shallowest);
        landScapeDeepest.setOnClickListener(descendingSort);
        landscapeShallowest.setOnClickListener(ascendingSort);
    }

    public void setupSortButtons(){
        sort_desc = findViewById(R.id.sort_desc);
        if(landscapeMode()){
            sort_desc.setVisibility(View.GONE);
            setupLandscapeSortButtons();
            return;
        }
        deepestButton = findViewById(R.id.deepest);
        shallowestButton = findViewById(R.id.shallowest);

        deepestButton.setOnClickListener(descendingSort);
        shallowestButton.setOnClickListener(ascendingSort);
    }



    private View.OnClickListener descendingSort = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            earthquakeViewModel.sort(false);
            sort_desc.setVisibility(View.VISIBLE);
            sort_desc.setText("Now arranged from largest depth to smallest");
        }
    } ;

    private View.OnClickListener ascendingSort = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            earthquakeViewModel.sort(true);
            sort_desc.setVisibility(View.VISIBLE);
            sort_desc.setText("Now arranged from smallest depth to largest");
        }
    } ;

    @Override
    protected void onResume() {
        // --- Whenever app resumes, the earthquake view model is asked to reload items. Just in case anything has changed from the API
        // --- So that the list is updated at all times
        super.onResume();
        earthquakeViewModel.loadEarthquakes();
    }

    public void notifyToRetry(){
        Snackbar snackbar = Snackbar.make(mainDiv, "Something happened, we could not load the data appropriately...!", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetSortLabel();
                notFound.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                earthquakeViewModel.loadEarthquakes();

            }
        });

        snackbar.show();
    }


    public void setupClearButton(){
        clearButton = (FloatingActionButton) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetSortLabel();
                earthquakeViewModel.reset();
                clearButton.setVisibility(View.GONE);
                notFound.setVisibility(View.GONE);
            }
        });
    }

    public void resetSortLabel(){
        if(landscapeMode()){
            sort_desc.setVisibility(View.GONE);
            return;
        }
        sort_desc.setText("You can sort by");
    }
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            resetSortLabel();
            String searchText = searchBox.getText().toString();
            searchByText(searchText);
        }
    };

    public void searchByDate (int[] start , int[] end) {
        resetSortLabel();
        notFound.setVisibility(View.GONE);
        List<EarthquakeItem> foundItems = earthquakeViewModel.searchByDate(start, end);
        earthquakeViewModel.setEarthquakes(foundItems);
        clearButton.setVisibility(View.VISIBLE);
        if (foundItems.size() == 0) notFound.setVisibility(View.VISIBLE);

    }
    public void searchByText (String text) {
        notFound.setVisibility(View.GONE);
        List<EarthquakeItem> foundItems = earthquakeViewModel.searchEarthquakesByTitle(text);
        earthquakeViewModel.setEarthquakes(foundItems);
        if (foundItems.size() == 0) notFound.setVisibility(View.VISIBLE);

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
                // --- Here, we setup a debounce functionality that only runs the search function (Debouncing is what creates the effect of searching right away as soon as you stop typing)
                // --- when a user pauses for about 500 milliseconds
                // --- So everytime a user types, a count is started for 500ms, and when they type again, the old count is cancelled and a new one is restarted
                // --- This will keep happening until the user actually stops typing, in which case
                // --- The count would actually reach 500ms which will then run the content of (runnable) (i.e. searchByText())
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
        // --- Since we are using the MVVM architecture, we setup the view model to observe any changes to the
        // --- data, and with any changes, it should re-inflate the recycler view the new content
        earthquakeViewModel = new ViewModelProvider(this).get(EarthquakeViewModel.class);
        earthquakeViewModel.getEarthquakes().observe(this, new Observer<List<EarthquakeItem>>() {
            @Override
            public void onChanged(List<EarthquakeItem> earthquakes) {
                progressBar.setVisibility(View.GONE);
                if(earthquakes == null){
                    notifyToRetry();
                    return;
                }

                recyclerView.setVisibility(View.VISIBLE);
                adapter.setEarthquakes(earthquakes);
            }
        });
    }




    private void showStartDatePickerDialog() {
        showDatePickerDialog(startYear, startMonth, startDay,null);
    }

    private void showEndDatePickerDialog(int[] minValue) {
        showDatePickerDialog(endYear, endMonth, endDay, minValue);
    }

    private void showDatePickerDialog(int year, int month, int day, int[] minValue) {
//        calendar.set(Calendar.YEAR, 2023);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        Calendar cal = Calendar.getInstance();
        if (minValue !=null){
            cal.set(Calendar.YEAR, minValue[0]);
            cal.set(Calendar.MONTH, minValue[1]);
            cal.set(Calendar.DAY_OF_MONTH, minValue[2]);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        } else {
            // set the minimum date to be 2020
            cal.set(Calendar.YEAR, 2020);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        }
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // --- Because only one date dialog is used to retrieve two dates, the "selectedDatePickerId" is used to differentiate
        // --- The first date (i.e. start date) from the second date (i.e. end date)

        if (selectedDatePickerId == START_DATE_PICKER_ID) {
            Log.d("PRINTING","You just chose date for start date");
            startYear = year;
            startMonth = month;
            startDay = dayOfMonth;
            // --- When the first date is chosen, we fire the date picker to show again,
            // --- but pass on the just-chosen date so that it would be used as the minimum date in the dialog when its
            // --- opened for the second time
            selectedDatePickerId = END_DATE_PICKER_ID;
            showEndDatePickerDialog(new int[] {year,month,dayOfMonth});
        } else if (selectedDatePickerId == END_DATE_PICKER_ID) {
            Log.d("PRINTING", "You just chose date for END DATE GEE, TOO COOL");
            endYear = year;
            endMonth = month;
            endDay = dayOfMonth;
            int[] startArr = new int[] {startYear, startMonth, startDay};
            int[] endArr = new int[] {year, month, dayOfMonth};
            searchByDate(startArr, endArr);
            selectedDatePickerId = START_DATE_PICKER_ID;

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