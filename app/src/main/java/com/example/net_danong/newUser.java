package com.example.net_danong;

import java.util.List;

public class newUser {
    private String userId;
    private List<String> userAddress;

    public newUser(String userId, List<String> userAddress) {
        this.userId = userId;
        this.userAddress = userAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(List<String> userAddress) {
        this.userAddress = userAddress;
    }
}









