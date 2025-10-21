package com.example.courseapp.Activity.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.courseapp.Activity.EditProfileActivity;
import com.example.courseapp.Activity.LoginActivity;
import com.example.courseapp.Activity.OrderHistoryActivity;
import com.example.courseapp.R;
import com.example.courseapp.api.ApiService;
import com.example.courseapp.api.RetrofitClient;
import com.example.courseapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ImageView avatarImageView;
    private TextView fullNameTextView, emailTextView;
    private Button logoutButton, editProfileButton, orderHistoryButton;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        avatarImageView = view.findViewById(R.id.avatarImageView);
        fullNameTextView = view.findViewById(R.id.fullNameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        orderHistoryButton = view.findViewById(R.id.orderHistoryButton);

        logoutButton.setOnClickListener(v -> handleLogout());

        orderHistoryButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), OrderHistoryActivity.class));
        });

        editProfileButton.setOnClickListener(v -> {
            if (currentUser != null) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("CURRENT_USER", currentUser);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Đang tải dữ liệu, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserProfile();
    }

    private void fetchUserProfile() {
        if (getActivity() == null) return;

        SharedPreferences prefs = getActivity().getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        String token = prefs.getString("USER_TOKEN", null);

        if (token == null) {
            handleLogout();
            return;
        }

        String authToken = "Bearer " + token;
        ApiService apiService = RetrofitClient.getApiService();

        apiService.getMyProfile(authToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    fullNameTextView.setText(currentUser.getFullName());
                    emailTextView.setText(currentUser.getEmail());
                } else if(isAdded()) {
                    Toast.makeText(getContext(), "Không thể tải thông tin hồ sơ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("PROFILE_FETCH_ERROR", "API call failed", t);
                }
            }
        });
    }

    private void handleLogout() {
        if (getActivity() == null) return;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("USER_TOKEN");
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}