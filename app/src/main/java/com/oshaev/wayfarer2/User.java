package com.oshaev.wayfarer2;

import java.util.ArrayList;

public class User {
    private String name;
    private String surname;
    private String email;
    private String imgUrl;
    private String Uid;
    private String userKey;
    private ArrayList<String> userPhotosList;
    private ArrayList<Country> userCountries;

    public User() {
        userPhotosList = new ArrayList<>();

        userCountries = new ArrayList<>();
    }

    public User(String name, String surname, String email, String imgUrl, String userKey, ArrayList<String> userPhotosList, ArrayList<Country> userCountries) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.imgUrl = imgUrl;
        this.userKey = userKey;
        this.userPhotosList = userPhotosList;
        this.userCountries = userCountries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public ArrayList<String> getUserPhotosList() {
        return userPhotosList;
    }

    public void setUserPhotosList(ArrayList<String> userPhotosList) {
        this.userPhotosList = userPhotosList;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public ArrayList<Country> getUserCountries() {
        return userCountries;
    }

    public void setUserCountries(ArrayList<Country> userCountries) {
        this.userCountries = userCountries;
    }
}
