package com.example.courseapp.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Course {

    private int courseId;
    private String title;
    private String description;

    private BigDecimal price;

    public int getId() {
        return courseId;
    }

    public void setId(int id) {
        this.courseId = id;
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
