package com.example.courseapp.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.courseapp.R;

public class ExamResultActivity extends AppCompatActivity {

    TextView scoreTextView, percentageTextView;
    Button backToCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result);

        scoreTextView = findViewById(R.id.scoreTextView);
        percentageTextView = findViewById(R.id.percentageTextView);
        backToCourseButton = findViewById(R.id.backToCourseButton);

        // Lấy dữ liệu kết quả từ Intent
        int score = getIntent().getIntExtra("SCORE", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 0);
        double percentage = getIntent().getDoubleExtra("PERCENTAGE", 0.0);

        // Hiển thị kết quả
        scoreTextView.setText(String.format("Điểm số: %d / %d", score, totalQuestions));
        percentageTextView.setText(String.format("Tỷ lệ: %.1f%%", percentage));

        // Nút quay lại
        backToCourseButton.setOnClickListener(v -> {
            // Đóng Activity này và quay lại màn hình trước đó (CourseDetailActivity)
            finish();
        });
    }
}