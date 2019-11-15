package com.example.net_danong;

class User {
    String Displayname;

    String Email;
    String PhoneNumber;
    long createdAt;

    public User(){};
    public User(String displayname, String email, String phoneNumber, long createdAt) {
        this.Displayname = displayname;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public String getDisplayname() {
        return Displayname;
    }
    public String getEmail() {
        return Email;
    }
    public String getPhoneNumber() {
        return PhoneNumber;
    }
    public long getCreatedAt() {
        return createdAt;
    }
}
