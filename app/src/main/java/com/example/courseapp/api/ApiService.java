package com.example.courseapp.api;

import com.example.courseapp.model.Course;
import com.example.courseapp.model.Exam;
import com.example.courseapp.model.request.CreateOrderRequest;
import com.example.courseapp.model.request.CreatePaymentRequest;
import com.example.courseapp.model.request.LoginRequest;
import com.example.courseapp.model.response.LoginResponse;
import com.example.courseapp.model.Notification;
import com.example.courseapp.model.Order;
import com.example.courseapp.model.Progress;
import com.example.courseapp.model.Question;
import com.example.courseapp.model.request.RegisterRequest;
import com.example.courseapp.model.User;
import com.example.courseapp.model.UserExam;
import com.example.courseapp.model.Video;
import com.example.courseapp.model.request.ExamSubmissionRequest;
import com.example.courseapp.model.response.ApiResponse;
import com.example.courseapp.model.response.PaymentResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // ================== Authentication (auth.js) ==================
    @POST("auth/register")
    Call<User> registerUser(@Body RegisterRequest registerRequest);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);


    // ================== Courses (courses.js) ==================
    @GET("courses")
    Call<List<Course>> getAllCourses();

    @GET("courses/{id}")
    Call<Course> getCourseDetails(@Path("id") int courseId);


    // ================== Videos (video.js) ==================
    // Lấy video của khóa học (cần xác thực)
    @GET("videos/course/{courseId}")
    Call<List<Video>> getVideosForCourse(@Header("Authorization") String authToken, @Path("courseId") int courseId);

    @POST("videos/complete/{videoId}")
    Call<ApiResponse> markVideoCompleted(
            @Header("Authorization") String authToken,
            @Path("videoId") int videoId
    );

    // ================== Exams (exams.js & questions.js) ==================
    // Lấy danh sách bài thi của một khóa học
    @GET("exams/course/{courseId}")
    Call<List<Exam>> getExamsForCourse(@Header("Authorization") String authToken, @Path("courseId") int courseId);

    // Lấy danh sách câu hỏi của một bài thi
    @GET("questions/exam/{examId}")
    Call<List<Question>> getQuestionsForExam(@Header("Authorization") String authToken, @Path("examId") int examId);


    // ================== User Exam Submission (userExam.js) ==================
    // Nộp bài và nhận kết quả
    @POST("exam-users/submit")
    Call<UserExam> submitExam(@Header("Authorization") String authToken, @Body ExamSubmissionRequest submissionRequest);

    // Lấy lịch sử các bài thi đã làm
    @GET("exam-users")
    Call<List<UserExam>> getMyExamResults(@Header("Authorization") String authToken);


    // ================== Learning Progress (progress.js) ==================
    // Lấy tiến độ tất cả khóa học của tôi
    @GET("progress/my-courses")
    Call<List<Progress>> getMyCourseProgress(@Header("Authorization") String authToken);

    // Cập nhật tiến độ học
    @POST("progress")
    Call<Progress> updateProgress(@Header("Authorization") String authToken, @Body Progress progress);


    // ================== Orders & Payments (orders.js) ==================
    // Tạo đơn hàng mới
    @POST("orders")
    Call<Order> createOrder(@Header("Authorization") String authToken, @Body CreateOrderRequest orderRequest);
    // Lấy tất cả đơn hàng của tôi
    @GET("orders")
    Call<List<Order>> getMyOrders(@Header("Authorization") String authToken);

    @POST("payments")
    Call<PaymentResponse> createPayment(@Header("Authorization") String authToken, @Body CreatePaymentRequest paymentRequest);
    @GET("orders/course/{courseId}")
    Call<Order> getOrderByCourse(
            @Header("Authorization") String authToken,
            @Path("courseId") int courseId
    );
    @GET("orders/my-courses")
    Call<List<Order>> getMyPaidCourses(@Header("Authorization") String authToken);
    // ================== Notifications (notifications.js) ==================
    // Lấy tất cả thông báo
    @GET("notifications")
    Call<List<Notification>> getNotifications(@Header("Authorization") String authToken);

    // Đánh dấu một thông báo đã đọc
    @PUT("notifications/{id}/read")
    Call<Notification> markNotificationAsRead(@Header("Authorization") String authToken, @Path("id") int notificationId);

    // Đánh dấu tất cả đã đọc
    @PUT("notifications/mark-all-read")
    Call<ApiResponse> markAllNotificationsAsRead(@Header("Authorization") String authToken);
    // ================== Users (user.js) ==================
    @GET("users/me")
    Call<User> getMyProfile(@Header("Authorization") String authToken);
    @PUT("users/{id}")
    Call<User> updateUser(
            @Header("Authorization") String authToken,
            @Path("id") int userId,
            @Body User user
    );
}