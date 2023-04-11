package com.frimpong.hot_quakes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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

    public void reset(){
        earthquakeList.setValue(dataBank);
    }

    private List<EarthquakeItem> sortInAscendingOrder(List<EarthquakeItem> items){

        if (items == null) return new ArrayList<>();
        List<EarthquakeItem> copy = new ArrayList<>(items);

        Collections.sort(copy, new Comparator<EarthquakeItem>() {
            @Override
            public int compare(EarthquakeItem item1, EarthquakeItem item2) {
                if(item1.getDepth() > item2.getDepth()) return 1;
                else if (item1.getDepth() < item2.getDepth()) return -1;
                return 0;
            }
        });
        return copy;
    }

    private List<EarthquakeItem> sortInDescendingOrder(List<EarthquakeItem> items){
        if (items == null) return new ArrayList<>();
        List<EarthquakeItem> copy = new ArrayList<>(items);

        Collections.sort(copy, new Comparator<EarthquakeItem>() {
            @Override
            public int compare(EarthquakeItem item1, EarthquakeItem item2) {
                if(item2.getDepth() > item1.getDepth()) return 1;
                else if (item2.getDepth() < item1.getDepth()) return -1;
                return 0;
            }
        });
        return copy;
    }

    public void sort(boolean inAscendingOrder) {
        List<EarthquakeItem> items = earthquakeList.getValue();
        if(inAscendingOrder) earthquakeList.setValue(sortInAscendingOrder(items));
        else earthquakeList.setValue(sortInDescendingOrder(items));
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

    public List<EarthquakeItem> searchByDate(int[] startDate, int[] endDate) {
        List<EarthquakeItem> results = new ArrayList<>();
        List<EarthquakeItem> earthquakes = dataBank;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
            Date start = new GregorianCalendar(startDate[0], startDate[1] , startDate[2]).getTime();
            Date end = new GregorianCalendar(endDate[0], endDate[1] , endDate[2]).getTime();
            for (EarthquakeItem item : earthquakes) {
                Date itemDate = sdf.parse(item.getPubDate());
                if (itemDate.compareTo(start) >= 0 && itemDate.compareTo(end) <= 0) {
                    results.add(item);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    }
    public void loadEarthquakes() {
        // Fetch XML data from website and parse it into a list of Earthquake objects

        InternetExplorer explorer =  new InternetExplorer(InternetExplorer.EARTH_QUAKE_URL, new AfterEffect() {
            @Override
            public void sendResponse(String response) {
                if (response == null || response.isEmpty()){
                    earthquakeList.postValue(new ArrayList<>()); // Set it to an empty array, this is how we know in the main activity that something happened
                    return;
                }
                xmlHandler = new CustomXMLHandler(response, new XMLDecoded() {
                    @Override
                    public void onItemsRetrieved(List<EarthquakeItem> items) {
                        if (items == null) return;
                        earthquakeList.postValue(items);
                        dataBank = items;
                    }
                });

            }
        });
        explorer.fetchDataInBackground();

    }
}
