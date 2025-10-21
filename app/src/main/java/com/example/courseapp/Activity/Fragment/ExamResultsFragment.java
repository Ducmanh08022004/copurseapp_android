package com.example.courseapp.Activity.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseapp.Adapter.UserExamAdapter;
import com.example.courseapp.R;
import com.example.courseapp.api.ApiService;
import com.example.courseapp.api.RetrofitClient;
import com.example.courseapp.model.UserExam;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamResultsFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exam_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadExamResults();
    }

    private void loadExamResults() {
        String token = "Bearer " + getTokenFromStorage(); // Lấy token từ SharedPreferences

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getMyExamResults(token).enqueue(new Callback<List<UserExam>>() {
            @Override
            public void onResponse(Call<List<UserExam>> call, Response<List<UserExam>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerView.setAdapter(new UserExamAdapter(getContext(), response.body()));
                } else {
                    Toast.makeText(getContext(), "Không có kết quả nào.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserExam>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getTokenFromStorage() {
        SharedPreferences prefs = getActivity().getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        String token = prefs.getString("USER_TOKEN", null);
        return token;
    }
}
