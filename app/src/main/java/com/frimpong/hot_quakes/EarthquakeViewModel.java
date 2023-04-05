package com.frimpong.hot_quakes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeViewModel extends ViewModel {
    private MutableLiveData<List<EarthquakeItem>> earthquakeList;
    private List<EarthquakeItem> dataBank = new ArrayList<>();
    CustomXMLHandler xmlHandler;
    public LiveData<List<EarthquakeItem>> getEarthquakes() {
        if (earthquakeList == null) {
            earthquakeList = new MutableLiveData<>();
            loadEarthquakes();
        }
        return earthquakeList;
    }

    public void setEarthquakes(List<EarthquakeItem> newEarthquakeList) {
        earthquakeList.setValue(newEarthquakeList);
    }


    public List<EarthquakeItem> searchEarthquakesByTitle(String searchText) {
        List<EarthquakeItem> matchingEarthquakes = new ArrayList<>();
        List<EarthquakeItem> earthquakes = dataBank;
        for (EarthquakeItem earthquake : earthquakes) {
            if (earthquake.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                matchingEarthquakes.add(earthquake);
            }
        }
        return matchingEarthquakes;
    }
    private void loadEarthquakes() {
        // Fetch XML data from website and parse it into a list of Earthquake objects
        InternetExplorer task = (InternetExplorer) new InternetExplorer().execute();
        task.onComplete = new AfterEffect() {
            @Override
            public void sendResponse(String response) {
                xmlHandler = new CustomXMLHandler(response, new XMLDecoded() {
                    @Override
                    public void onItemsRetrieved(List<EarthquakeItem> items) {
                        if (items == null) return;
                        earthquakeList.setValue(items);
                        dataBank = items;
                    }
                });

            }
        };
    }
}
