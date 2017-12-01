package com.example.dellcorei3.storemanagement.store.manager.models;

/**
 * Created by DELL on 11/10/2017.
 */

public class StoreInfomation {
    public String name,address,title,email,phone,duration,website;

    public StoreInfomation(String name, String address, String title, String email, String phone, String duration, String website) {
        this.name = name;
        this.address = address;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.duration = duration;
        this.website = website;
    }

    public StoreInfomation() {
    }
}
