package com.example.courseapp.model.request;

import java.util.List;

public class ExamSubmissionRequest {
    private int examId;
    private List<Answer> answers; // Danh sách câu trả lời

    public ExamSubmissionRequest(int examId, List<Answer> answers) {
        this.examId = examId;
        this.answers = answers;
    }

    // Lớp nội bộ để biểu diễn một câu trả lời
    public static class Answer {
        private int questionId;
        private String userAnswer; // Ví dụ: "A", "B"...

        public Answer(int questionId, String userAnswer) {
            this.questionId = questionId;
            this.userAnswer = userAnswer;
        }
    }
}
