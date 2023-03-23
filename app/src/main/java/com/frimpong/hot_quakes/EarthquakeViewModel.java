package com.frimpong.hot_quakes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeViewModel extends ViewModel {
    private MutableLiveData<List<EarthquakeItem>> earthquakeList;

    public LiveData<List<EarthquakeItem>> getEarthquakes() {
        if (earthquakeList == null) {
            earthquakeList = new MutableLiveData<>();
            loadEarthquakes();
        }
        return earthquakeList;
    }

    private void loadEarthquakes() {
        // Fetch XML data from website and parse it into a list of Earthquake objects
        List<EarthquakeItem> earthquakes = new ArrayList<>();
        // TODO: Fetch XML data and parse it into a list of Earthquake objects
        EarthquakeItem item = new EarthquakeItem();
        item.setTitle("Earth quake in turkey");
        item.setPubDate("1st February 2037");
        item.setDepth(45.6);
        item.setMagnitude(7.0);
        earthquakes.add(item);
        earthquakeList.setValue(earthquakes);
    }
}
