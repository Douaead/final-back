package com.scrapy.pfe.spring.entities;

public class Maroc {
    private String title;
    private String description;
    private String city;
    private String salary;

    private String time;
    private String link;


    public Maroc(String title, String description, String city,String salary , String time, String link) {
        this.title = title;
        this.description= description;

        this.city = city;
        this.salary = salary;
        this.time=time;

        this.link = link;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
