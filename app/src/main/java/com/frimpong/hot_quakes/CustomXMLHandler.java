package com.frimpong.hot_quakes;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CustomXMLHandler {
    private String xmlString;

    private List<EarthquakeItem> items = new ArrayList<>();

    public XMLDecoded responseInterface;
    public CustomXMLHandler(String xmlString){
        this.xmlString = xmlString;
        this.process();
    }

    public CustomXMLHandler(String xmlString, XMLDecoded callback){
        this.xmlString = xmlString;
        this.responseInterface = callback;
        this.process();
    }

    private EarthquakeItem makeItem (String title, String desc, String pubDate, String url, String _long, String lat){
        EarthquakeItem item = null ;
        if (title != null && !title.isEmpty()) {
            String[] titleArr = title.split(":");
            title = titleArr[2].split(",")[0];
        }
        String[] descArr = desc.split(";");
        String magnitude = descArr[descArr.length-1]; // BPR (could differ here) TODO: remove comment before submission
        String depth = descArr[descArr.length-2];
        item = new EarthquakeItem(title, desc,pubDate,url);
        Double mag=0.0, dep = 0.0;
        System.out.println(desc);
        if(magnitude != null && !magnitude.isEmpty()){
            // Before split looks like this:  "Magnitude: 6.8"
                String val = magnitude.split(":")[1];
                if (!val.trim().isEmpty())mag = Double.valueOf(val);
        }
        if(depth != null && !depth.isEmpty()){
            // before split, looks like this: "Depth: 65 km"
            String last = depth.split(":")[1];
            last = last.trim();
            String val = last.split(" ")[0];
            if (!val.trim().isEmpty()) dep = Double.valueOf(val);// before split looks like this: "56 km"
        }
        item.setMagnitude(mag);
        item.setDepth(dep);
        item.setLongitude(Double.valueOf(_long));
        item.setLatitude(Double.valueOf(lat));

        return item;
    }
    public void process () {
        if( this.xmlString == null) {
            Log.d(Constants.LOG_TAG,"XML String is Empty....");
            return;
        }
//        Log.d(TAG,xmlString);


        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput( new StringReader(this.xmlString));

            int eventType = parser.getEventType();
            // Traverse through the XML document
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.getName().equalsIgnoreCase("item")) {
                    String title = "";
                    String description = "";
                    String pubDate = "";
                    String link = "";
                    String _long = "";
                    String _lat = "";

                    while (eventType != XmlPullParser.END_TAG || !parser.getName().equalsIgnoreCase("item")) {
                        if (eventType == XmlPullParser.START_TAG) {
                            switch (parser.getName().toLowerCase()) {
                                case "title":
                                    title = parser.nextText();
                                    break;
                                case "description":
                                    description = parser.nextText();
                                    break;
                                case "pubdate":
                                    pubDate = parser.nextText();
                                    break;
                                case "link":
                                    link = parser.nextText();
                                    break;
                                case "geo:lat":
                                    _lat = parser.nextText();
                                    break;
                                case "geo:long":
                                    _long = parser.nextText();
                                    break;
                                default:
                                    break;
                            }
                        }
                        eventType = parser.next();
                    }
                    EarthquakeItem earthquakeItem =  makeItem(title,description,pubDate,link,_long,_lat);
                    items.add(earthquakeItem);
                    System.out.println(earthquakeItem);
                    System.out.println("----------------------------");
                }
                eventType = parser.next();
            }
            responseInterface.onItemsRetrieved(items);
        } catch (XmlPullParserException | IOException e) {
            responseInterface.onItemsRetrieved(null);
            Log.d(Constants.ERROR_TAG, e.toString());
        }
    }

}