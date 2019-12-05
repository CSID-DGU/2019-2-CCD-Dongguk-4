package com.example.net_danong;

import android.os.Parcel;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class User {

    private String userUid;
    private String email;
    private String userId;
    private String displayName;
    private String PhoneNum;
    private com.google.firebase.Timestamp createdAt;
    private String photoURL;
    private String pushToken;


    public User(){

    };
    public User(String displayName, String email, String phoneNum, com.google.firebase.Timestamp createdAt) {
        this.userId = email.substring(0,email.lastIndexOf("@"));
        this.displayName = displayName;
        this.email = email;
        this.PhoneNum = phoneNum;
        this.createdAt = createdAt;
    }
    public User(String userUid, String displayName, String email, String phoneNum, com.google.firebase.Timestamp createdAt) {
        this.userUid = userUid;
        this.userId = email.substring(0,email.lastIndexOf("@"));
        this.displayName = displayName;
        this.email = email;
        this.PhoneNum = phoneNum;
        this.createdAt = createdAt;
    }
    protected User(Parcel in) {
        email = in.readString();
        userId = in.readString();
        displayName = in.readString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNum() {
        return PhoneNum;
    }
    public com.google.firebase.Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public void setCreatedAt(com.google.firebase.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
