package com.oshaev.wayfarer2;

public class Article {
    private String imgUrl;
    private String title;
    private String summary;
    private String paper;

    public Article() {
    }

    public Article(String imgUrl, String title, String summary, String paper) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.summary = summary;
        this.paper = paper;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }
}
