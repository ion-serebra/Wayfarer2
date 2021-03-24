package com.oshaev.wayfarer2;

public class Country {

    private String name;
    private boolean open;
    private String imgUrl;

    public Country() {
    }

    public Country(String name) {
        this.name = name;
    }

    public Country(String name, boolean isOpen) {
        this.name = name;
        this.open = isOpen;
    }

    public Country(String name, boolean isOpen, String imgUrl) {
        this.name = name;
        this.open = isOpen;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
