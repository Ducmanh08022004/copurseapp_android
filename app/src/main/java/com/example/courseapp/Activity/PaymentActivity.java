package com.example.courseapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.courseapp.R;
import com.example.courseapp.api.ApiService;
import com.example.courseapp.api.RetrofitClient;
import com.example.courseapp.model.request.CreatePaymentRequest;
import com.example.courseapp.model.response.PaymentResponse;

// THÊM CÁC IMPORT NÀY
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private TextView amountTextView, orderIdTextView;
    private Button confirmPaymentButton;
    private int orderId;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amountTextView = findViewById(R.id.amountTextView);
        orderIdTextView = findViewById(R.id.orderIdTextView);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        // Lấy token
        SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        String token = prefs.getString("USER_TOKEN", null);
        if (token != null) {
            authToken = "Bearer " + token;
        } else {
            // Nếu không có token, không thể thanh toán -> quay về login
            Toast.makeText(this, "Phiên đăng nhập hết hạn, vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Lấy dữ liệu từ Intent
        orderId = getIntent().getIntExtra("ORDER_ID", -1);

        // NHẬN DỮ LIỆU AMOUNT DƯỚI DẠNG BigDecimal
        BigDecimal amount = null;
        try {
            // Lấy đối tượng Serializable và ép kiểu về BigDecimal
            amount = (BigDecimal) getIntent().getSerializableExtra("AMOUNT");
        } catch (ClassCastException e) {
            Log.e("PaymentActivity", "Lỗi ép kiểu Amount sang BigDecimal", e);
        }

        // Kiểm tra dữ liệu hợp lệ
        if (orderId == -1 || amount == null) {
            Toast.makeText(this, "Lỗi: Thiếu thông tin đơn hàng hoặc giá tiền.", Toast.LENGTH_LONG).show();
            finish(); // Đóng Activity nếu thiếu thông tin
            return;
        }

        // Hiển thị thông tin
        orderIdTextView.setText(String.format("Mã đơn hàng: #%d", orderId));

        // ĐỊNH DẠNG SỐ TIỀN THEO KIỂU VNĐ
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        amountTextView.setText(currencyFormatter.format(amount)); // Định dạng BigDecimal

        // Gán sự kiện cho nút xác nhận
        confirmPaymentButton.setOnClickListener(v -> handlePayment());
    }

    private void handlePayment() {
        // Vô hiệu hóa nút để tránh nhấn nhiều lần
        confirmPaymentButton.setEnabled(false);
        confirmPaymentButton.setText("Đang xử lý...");

        // Tạo request body. Giả lập phương thức là "momo"
        CreatePaymentRequest request = new CreatePaymentRequest(orderId, "momo");

        RetrofitClient.getApiService().createPayment(authToken, request).enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(PaymentActivity.this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();

                    // Chuyển về màn hình chính, xóa các màn hình trung gian (CourseDetail, Payment)
                    Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                    // Xóa tất cả activity phía trên MainActivity và đưa MainActivity lên đầu
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Đóng PaymentActivity
                } else {
                    // Xử lý lỗi từ server (ví dụ: đơn hàng đã thanh toán, không tìm thấy đơn hàng)
                    String errorMessage = "Thanh toán thất bại";
                    if (response.code() == 400) {
                        errorMessage = "Đơn hàng này đã được thanh toán.";
                    } else if (response.code() == 404) {
                        errorMessage = "Không tìm thấy đơn hàng để thanh toán.";
                    }
                    Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    // Kích hoạt lại nút sau khi có lỗi
                    confirmPaymentButton.setEnabled(true);
                    confirmPaymentButton.setText("Xác nhận thanh toán");
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("PAYMENT_ERROR", "API call failed", t);
                // Kích hoạt lại nút sau khi có lỗi
                confirmPaymentButton.setEnabled(true);
                confirmPaymentButton.setText("Xác nhận thanh toán");
            }
        });
    }
}