package com.scrapy.pfe.spring.entities;

public class Job {
    private String title;
    private String description;
    private String location;
    private String date;
    private String link;

    public Job(String title, String description, String location,String date,String link) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.link = link;
    }

    // Getters et Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
