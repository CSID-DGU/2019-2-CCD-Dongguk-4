package com.example.net_danong;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {

    public static final String FIELD_REGION = "region";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_POPULARITY = "numRatings";
    public static final String FIELD_AVG_RATING = "avgRating";

    private String userUId;
    private String userName;
    private String name;
    private String region;
    private String category;
    private String photo;
    private int price;
    private int numRatings;
    private double avgRating;

    public Product() {}

    public Product(FirebaseUser user, String name, String region, String category, String photo,
                   int price, int numRatings, double avgRating) {
        this.userUId = user.getUid();
        this.userName = user.getDisplayName();
        if (TextUtils.isEmpty(this.userName)) {
            this.userName = user.getEmail();
        }
        this.name = name;
        this.region = region;
        this.category = category;
        this.price = price;
        this.numRatings = numRatings;
        this.avgRating = avgRating;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String city) {
        this.region = city;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
}
