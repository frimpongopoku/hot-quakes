package com.frimpong.hot_quakes;

import androidx.annotation.NonNull;

public class EarthquakeItem {
    private String title ="";
    private String description ="";
    private String url ="";
    private String pubDate="";
    private String category="";
    private Double latitude;
    private Double longitude;

    private Double depth;
    private Double magnitude;

    public EarthquakeItem() {
    }
    public EarthquakeItem(String title, String desc, String pubDate, String link) {
        this.title = title;
        this.description = desc;
        this.pubDate = pubDate;
        this.url = link;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public int getColorRepresentation(){
        if (this.magnitude >6 && this.magnitude <=6.8)  return R.color.medium_magnitude;
        else if (this.magnitude >6.8 )  return R.color.high_magnitude;
        return R.color.low_magnitude;
    }
    @NonNull
    @Override
    public String toString() {
        return this.title +"\n" + this.description + "\n" + this.depth + ", "+this.magnitude+"\n"+this.latitude+", "+this.longitude+"\n";
    }
}

