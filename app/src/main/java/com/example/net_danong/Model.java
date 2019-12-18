package com.example.net_danong;

import com.google.firebase.storage.StorageReference;

public class Model {

    private String image;
    private String title;
    private String desc;

    public Model(String image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
