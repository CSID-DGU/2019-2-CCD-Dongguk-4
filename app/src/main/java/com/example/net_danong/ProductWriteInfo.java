package com.example.net_danong;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductWriteInfo {

    private String title;
    private String product;
    private String price;
    private String location;
    private List<String> contents;
    private String publisher;
    private Date createdAt;

    private String category;
    private String photo;
    //평점
    private int numRatings;
    private double avgRating;

    public ProductWriteInfo() {
    }

    public ProductWriteInfo(String title, String product, String price, String location, List<String> contents, String publisher, Date createdAt, String category) {
        this.title = title;
        this.product = product;
        this.price = price;
        this.location = location;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public double getAvgRating() {
        return avgRating;
    }

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

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getContents() {
        return this.contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getCreatedAt(){ return this.createdAt; }

    public void setCreatedAt(Date createdAt){ this.createdAt = createdAt; }
}