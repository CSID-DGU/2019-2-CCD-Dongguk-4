package com.example.net_danong;

import java.util.ArrayList;
import java.util.Date;

public class WriteInfo {
    private String title;
    private String product;
    private String price;
    private String location;
    private ArrayList<String> contents;
    private String publisher;
    private Date createdAt;

    public WriteInfo(String title, String product, String price, String location, ArrayList<String> contents, String publisher,Date createdAt) {
        this.title = title;
        this.product = product;
        this.price = price;
        this.location = location;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
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

    public ArrayList<String> getContents() {
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