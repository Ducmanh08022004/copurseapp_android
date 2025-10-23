package com.example.courseapp.model;

public class Progress {
    private int courseId;
    private String title;
    private double videoPercent;
    private double examPercent;
    private double totalPercent;
    private String imageUrl; // ✅ thêm trường ảnh bìa khóa học

    public Progress() {}

    // Constructor đầy đủ
    public Progress(int courseId, String title, double videoPercent, double examPercent, double totalPercent, String imageUrl) {
        this.courseId = courseId;
        this.title = title;
        this.videoPercent = videoPercent;
        this.examPercent = examPercent;
        this.totalPercent = totalPercent;
        this.imageUrl = imageUrl;
    }

    // Getter & Setter
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVideoPercent() {
        return videoPercent;
    }

    public void setVideoPercent(double videoPercent) {
        this.videoPercent = videoPercent;
    }

    public double getExamPercent() {
        return examPercent;
    }

    public void setExamPercent(double examPercent) {
        this.examPercent = examPercent;
    }

    public double getTotalPercent() {
        return totalPercent;
    }

    public void setTotalPercent(double totalPercent) {
        this.totalPercent = totalPercent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
