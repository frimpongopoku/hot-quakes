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
        EarthquakeItem item2 = new EarthquakeItem();
        item2.setTitle("Earth quake in Portugal");
        item2.setPubDate("1st February 1992");
        item2.setDepth(5.6);
        item2.setMagnitude(18.0);
        earthquakes.add(item2);
        earthquakeList.setValue(earthquakes);
    }
}
