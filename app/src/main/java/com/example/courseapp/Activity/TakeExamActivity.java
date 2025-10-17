package com.example.courseapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseapp.Adapter.QuestionsAdapter;
import com.example.courseapp.R;
import com.example.courseapp.api.RetrofitClient;
import com.example.courseapp.model.request.ExamSubmissionRequest;
import com.example.courseapp.model.Question;
import com.example.courseapp.model.UserExam;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeExamActivity extends AppCompatActivity {

    TextView examTitleTextView;
    RecyclerView questionsRecyclerView;
    Button submitExamButton;

    private int examId;
    private String authToken, titleExam;
    private QuestionsAdapter adapter; // Giữ tham chiếu đến adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_exam);

        // Ánh xạ các view
        examTitleTextView = findViewById(R.id.examTitleTextView);
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);
        submitExamButton = findViewById(R.id.submitExamButton);

        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy examId từ Intent
        examId = getIntent().getIntExtra("EXAM_ID", -1);
        titleExam = getIntent().getStringExtra("EXAM_TITLE");
        // Lấy token xác thực
        SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        String token = prefs.getString("USER_TOKEN", null);

        if (token == null) {
            Toast.makeText(this, "Lỗi xác thực. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        authToken = "Bearer " + token;

        // Tải câu hỏi nếu examId hợp lệ
        if (examId != -1) {
            fetchExamQuestions();
        }

        // Thiết lập sự kiện cho nút nộp bài
        submitExamButton.setOnClickListener(v -> handleSubmitExam());
    }

    private void fetchExamQuestions() {
        RetrofitClient.getApiService().getQuestionsForExam(authToken, examId).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Question> questions = response.body();

                    // Khởi tạo adapter (sử dụng constructor 2 tham số)
                    adapter = new QuestionsAdapter(TakeExamActivity.this, questions);

                    // Gắn adapter cho RecyclerView
                    questionsRecyclerView.setAdapter(adapter);

                    examTitleTextView.setText(titleExam);
                } else {
                    Toast.makeText(TakeExamActivity.this, "Không thể tải câu hỏi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Toast.makeText(TakeExamActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSubmitExam() {
        if (adapter == null) {
            Toast.makeText(this, "Chưa tải xong câu hỏi", Toast.LENGTH_SHORT).show();
            return;
        }

        //  Thu thập câu trả lời từ Adapter
        List<Question> answeredQuestions = adapter.getAnsweredQuestions();

        //  Tạo đối tượng Request Body
        List<ExamSubmissionRequest.Answer> answersForApi = new ArrayList<>();
        for (Question q : answeredQuestions) {
            answersForApi.add(new ExamSubmissionRequest.Answer(q.getQuestionId(), q.getUserAnswer()));
        }
        ExamSubmissionRequest submissionRequest = new ExamSubmissionRequest(examId, answersForApi);

        //  Gọi API để nộp bài
        RetrofitClient.getApiService().submitExam(authToken, submissionRequest).enqueue(new Callback<UserExam>() {
            @Override
            public void onResponse(Call<UserExam> call, Response<UserExam> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserExam result = response.body();

                    // 4. Chuyển sang màn hình ExamResultActivity và gửi kết quả
                    Intent intent = new Intent(TakeExamActivity.this, ExamResultActivity.class);
                    intent.putExtra("SCORE", result.getScore());
                    intent.putExtra("TOTAL_QUESTIONS", result.getTotalQuestions());
                    intent.putExtra("PERCENTAGE", result.getPercent());
                    startActivity(intent);
                    finish(); // Kết thúc màn hình làm bài, không cho quay lại
                } else {
                    Toast.makeText(TakeExamActivity.this, "Lỗi khi nộp bài", Toast.LENGTH_SHORT).show();
                    Log.e("SUBMIT_EXAM", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserExam> call, Throwable t) {
                Toast.makeText(TakeExamActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SUBMIT_EXAM", "onFailure: ", t);
            }
        });
    }
}