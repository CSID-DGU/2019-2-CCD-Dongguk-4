package com.example.net_danong;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductWriteInfo {

    private String userUid;
    private String title;
    private String product;
    private String price;
    private String location;
    //private LatLng googleLocation;
    private double latitude;
    private double longitude;

    private String contents;
    private String publisher;
    private Object createdAt;

    private String category;
    private String photoUrl;
    //평점
    private int numRatings;
    private double avgRating;

    public ProductWriteInfo() {
    }

    public ProductWriteInfo(String userUid, String title, String product, String price, String location, String contents, String publisher, Object createdAt, String category, String photoUrl) {
        this.userUid = userUid;
        this.title = title;
        this.product = product;
        this.price = price;
        this.location = location;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.category = category;
        this.photoUrl = photoUrl;
        numRatings = 0;
        avgRating = 0;
    }

    public double getLatitude(){return latitude;}
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }
    public double getLongitude(){return longitude;}
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public String getUserUid() {
        return userUid;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhotoUrl(){ return this.photoUrl; }

    public void setPhotoUrl(String photoUrl){ this.photoUrl = photoUrl; }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public double getAvgRating() { return avgRating; }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProduct() {
        return this.product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) { this.location = location; }

    public String getContents() {
        return this.contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Object getCreatedAt(){ return this.createdAt; }

    public void setCreatedAt(Object createdAt){ this.createdAt = createdAt; }
}
