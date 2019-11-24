package com.example.net_danong;

import android.os.Parcel;
import android.os.Parcelable;

public class User {

    private String email;
    private String user_id;
    private String displayname;
    private String PhoneNumber;
    private long createdAt;


    public User(){};
    public User(String displayname, String email, String phoneNumber, long createdAt) {
        this.user_id = email.substring(0,email.lastIndexOf("@"));
        this.displayname = displayname;
        this.email = email;
        this.PhoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }
    protected User(Parcel in) {
        email = in.readString();
        user_id = in.readString();
        displayname = in.readString();
    }

    public String getDisplayname() {
        return displayname;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return PhoneNumber;
    }
    public long getCreatedAt() {
        return createdAt;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

}
