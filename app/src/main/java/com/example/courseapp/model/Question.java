package com.example.courseapp.model;

import java.util.List;

public class Question {

    private int questionId;
    private String content; // Nội dung câu hỏi
    private List<String> answer; // Danh sách các lựa chọn, ví dụ: ["A. ...", "B. ..."]
    private String correctAnswer; // Đáp án đúng, ví dụ: "A"

    // Trường này không có trong JSON từ API, dùng để lưu lựa chọn của người dùng trên app
    private String userAnswer;

    public Question(int questionId, String content, List<String> answer, String correctAnswer) {
        this.questionId = questionId;
        this.content = content;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
        this.userAnswer = null; // Ban đầu người dùng chưa trả lời
    }

    // Getters and Setters
    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getAnswers() {
        return answer;
    }

    public void setAnswers(List<String> answers) {
        this.answer = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
