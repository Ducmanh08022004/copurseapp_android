package com.example.courseapp.model;

public class Video {

    private int videoId;
    private String title;
    private String duration; // Ví dụ: "10:35"
    private String url;      // URL để stream video

    private int courseId;

    public Video(int id, String title, String duration, String url, int courseId) {
        this.videoId = id;
        this.title = title;
        this.duration = duration;
        this.url = url;
        this.courseId = courseId;
    }

    public int getId() {
        return videoId;
    }

    public void setId(int id) {
        this.videoId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}