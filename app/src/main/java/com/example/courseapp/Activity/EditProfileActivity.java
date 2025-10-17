package com.example.courseapp.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.courseapp.R;
import com.example.courseapp.api.ApiService;
import com.example.courseapp.api.RetrofitClient;
import com.example.courseapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText usernameEditText, fullNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button saveButton;
    private Toolbar toolbar;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chỉnh sửa hồ sơ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usernameEditText = findViewById(R.id.usernameEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        saveButton = findViewById(R.id.saveButton);

        currentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        if (currentUser != null) {
            usernameEditText.setText(currentUser.getUsername());
            fullNameEditText.setText(currentUser.getFullName());
            emailEditText.setText(currentUser.getEmail());
        } else {
            Toast.makeText(this, "Lỗi: Không có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        saveButton.setOnClickListener(v -> handleSaveChanges());
    }

    private void handleSaveChanges() {
        String newFullName = fullNameEditText.getText().toString().trim();
        String newEmail = emailEditText.getText().toString().trim();
        String newPassword = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (newFullName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Họ tên và Email không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUserPayload = new User();
        updatedUserPayload.setFullName(newFullName);
        updatedUserPayload.setEmail(newEmail);

        if (!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newPassword.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }
            updatedUserPayload.setPassword(newPassword);
        }

        SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        String token = prefs.getString("USER_TOKEN", null);
        String authToken = "Bearer " + token;

        ApiService apiService = RetrofitClient.getApiService();
        apiService.updateUser(authToken, currentUser.getId(), updatedUserPayload).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

