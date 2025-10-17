package com.example.courseapp.model;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Progress {
    @SerializedName("Course")
    private Course course;
    private int progressId;
    private int percentage;
    private Date creatAt;
    private Date updateAt;
    private int userId;
    private int courseId;


    public Progress(Course course, int progressId, int percentage, Date creatAt, Date updateAt, int userId, int courseId) {
        this.course = course;
        this.progressId = progressId;
        this.percentage = percentage;
        this.creatAt = creatAt;
        this.updateAt = updateAt;
        this.userId = userId;
        this.courseId = courseId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public Date getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(Date creatAt) {
        this.creatAt = creatAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
