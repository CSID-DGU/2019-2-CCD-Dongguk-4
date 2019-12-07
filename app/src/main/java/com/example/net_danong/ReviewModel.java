package com.example.net_danong;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;


public class ReviewModel {
    private String userUId;
    private String userName;
    private double rating;
    private String text;
    private String rvPhoto;
    private @ServerTimestamp Date timestamp;

    public ReviewModel() {}

    public ReviewModel(FirebaseUser user, double rating, String text, String rvPhoto) {
        this.userUId = user.getUid();
        this.userName = user.getEmail().substring(0,user.getEmail().lastIndexOf("@"));
        this.rating = rating;
        this.text = text;
        this.rvPhoto = rvPhoto;
    }

    public String getRvPhoto() {
        return rvPhoto;
    }

    public void setRvPhoto(String rvPhoto) {
        this.rvPhoto = rvPhoto;
    }

    public String getUserUId() {
        return userUId;
    }

    public void setUserUId(String userId) {
        this.userUId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
