package com.example.courseapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.courseapp.Adapter.ExamsAdapter;
import com.example.courseapp.Adapter.VideosAdapter;
import com.example.courseapp.R;
import com.example.courseapp.api.RetrofitClient;
import com.example.courseapp.model.Course;
import com.example.courseapp.model.request.CreateOrderRequest;
import com.example.courseapp.model.Exam;
import com.example.courseapp.model.Order;
import com.example.courseapp.model.Video;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailActivity extends AppCompatActivity {

    ImageView detailCourseImageView;
    TextView detailCourseTitleTextView, detailCourseDescriptionTextView;
    RecyclerView videosRecyclerView, examsRecyclerView;
    Toolbar toolbar;
    ExtendedFloatingActionButton enrollButton;
    private int courseId;
    private String authToken;
    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // --- Ánh xạ các view ---
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        detailCourseImageView = findViewById(R.id.detailCourseImageView);
        detailCourseTitleTextView = findViewById(R.id.detailCourseTitleTextView);
        detailCourseDescriptionTextView = findViewById(R.id.detailCourseDescriptionTextView);
        videosRecyclerView = findViewById(R.id.videosRecyclerView);
        examsRecyclerView = findViewById(R.id.examsRecyclerView);
        enrollButton = findViewById(R.id.enrollButton);

        videosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        examsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- Lấy dữ liệu cần thiết ---
        SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        String token = prefs.getString("USER_TOKEN", null);
        if (token != null) {
            authToken = "Bearer " + token;
        }

        courseId = getIntent().getIntExtra("COURSE_ID", -1);
        if (courseId != -1) {
            fetchCourseDetails();
            fetchCourseVideos();
            fetchCourseExam();
        }

        enrollButton.setOnClickListener(v -> handleEnrollCourse());
    }

    private void handleEnrollCourse() {
        if (authToken == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để đăng ký", Toast.LENGTH_SHORT).show();
            return;
        }
        // Kiểm tra xem đã tải xong chi tiết khóa học chưa
        if (currentCourse == null) {
            Toast.makeText(this, "Đang tải dữ liệu khóa học, vui lòng thử lại sau giây lát", Toast.LENGTH_SHORT).show();
            return;
        }

        enrollButton.setEnabled(false);
        enrollButton.setText("Đang kiểm tra...");

        RetrofitClient.getApiService().getOrderByCourse(authToken, courseId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Order existingOrder = response.body();
                    if ("paid".equals(existingOrder.getStatus())) {
                        Toast.makeText(CourseDetailActivity.this, "Bạn đã mua khóa học này rồi!", Toast.LENGTH_LONG).show();
                        enrollButton.setText("Đã sở hữu");
                    } else {
                        Toast.makeText(CourseDetailActivity.this, "Đơn hàng đã tồn tại, chuyển đến thanh toán...", Toast.LENGTH_SHORT).show();
                        navigateToPayment(existingOrder);
                    }
                } else if (response.code() == 404) {
                    createNewOrder();
                } else {
                    Toast.makeText(CourseDetailActivity.this, "Lỗi khi kiểm tra đơn hàng", Toast.LENGTH_SHORT).show();
                    resetEnrollButton();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(CourseDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                resetEnrollButton();
            }
        });
    }

    private void createNewOrder() {
        CreateOrderRequest request = new CreateOrderRequest(courseId);
        RetrofitClient.getApiService().createOrder(authToken, request).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    navigateToPayment(response.body());
                } else {
                    Toast.makeText(CourseDetailActivity.this, "Tạo đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                    resetEnrollButton();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(CourseDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                resetEnrollButton();
            }
        });
    }

    private void navigateToPayment(Order order) {
        if (currentCourse == null) {
            Toast.makeText(this, "Lỗi: Không có thông tin giá tiền khóa học.", Toast.LENGTH_SHORT).show();
            resetEnrollButton();
            return;
        }
        Intent intent = new Intent(CourseDetailActivity.this, PaymentActivity.class);
        intent.putExtra("ORDER_ID", order.getOrderId());
        intent.putExtra("AMOUNT", currentCourse.getPrice());
        startActivity(intent);
        resetEnrollButton();
    }

    private void resetEnrollButton() {
        enrollButton.setEnabled(true);
        enrollButton.setText("Đăng ký học / Thanh toán");
    }


    private void fetchCourseDetails() {
        RetrofitClient.getApiService().getCourseDetails(courseId).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Lưu lại thông tin khóa học vào biến thành viên
                    currentCourse = response.body();

                    detailCourseTitleTextView.setText(currentCourse.getTitle());
                    detailCourseDescriptionTextView.setText(currentCourse.getDescription());
                } else {
                    Toast.makeText(CourseDetailActivity.this, "Không tìm thấy khóa học", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Toast.makeText(CourseDetailActivity.this, "Lỗi tải chi tiết khóa học", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCourseExam() {
        if (authToken == null) return;
        RetrofitClient.getApiService().getExamsForCourse(authToken,courseId).enqueue(new Callback<List<Exam>>() {
            @Override
            public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ExamsAdapter adapter = new ExamsAdapter(CourseDetailActivity.this, response.body(), exam -> {
                        Intent intent = new Intent(CourseDetailActivity.this, TakeExamActivity.class);
                        intent.putExtra("EXAM_ID", exam.getExamId());
                        intent.putExtra("EXAM_TITLE", exam.getTitle());
                        startActivity(intent);
                    });
                    examsRecyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Exam>> call, Throwable t) {
                Log.e("FETCH_EXAM", "Lỗi tải danh sách bài kiểm tra", t);
            }
        });
    }

    private void fetchCourseVideos() {
        if (authToken == null) return;

        RetrofitClient.getApiService().getVideosForCourse(authToken, courseId).enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VideosAdapter adapter = new VideosAdapter(CourseDetailActivity.this, response.body(), video -> {
                        Intent intent = new Intent(CourseDetailActivity.this, VideoPlayerActivity.class);
                        intent.putExtra("VIDEO_URL", video.getUrl());
                        intent.putExtra("VIDEO_ID", video.getId());
                        startActivity(intent);
                    });
                    videosRecyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e("FETCH_VIDEOS", "Lỗi tải danh sách video", t);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

