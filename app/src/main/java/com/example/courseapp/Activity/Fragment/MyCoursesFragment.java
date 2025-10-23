package com.example.courseapp.Activity.Fragment;

import android.content.Context;
import android.content.Intent;
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

import com.example.courseapp.Activity.CourseDetailActivity;
import com.example.courseapp.Adapter.MyCoursesAdapter;
import com.example.courseapp.R;
import com.example.courseapp.api.ApiService;
import com.example.courseapp.api.RetrofitClient;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCoursesFragment extends Fragment {

    private RecyclerView myCoursesRecyclerView;
    private MyCoursesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout cho fragment này
        return inflater.inflate(R.layout.fragment_my_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myCoursesRecyclerView = view.findViewById(R.id.myCoursesRecyclerView);
        myCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        fetchMyPaidCourses();
    }


    private void fetchMyPaidCourses() {
        if (getActivity() == null) return;

        SharedPreferences prefs = getActivity().getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        String token = prefs.getString("USER_TOKEN", null);

        if (token == null) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }

        String authToken = "Bearer " + token;
        ApiService apiService = RetrofitClient.getApiService();

        // Lấy tiến độ học
        apiService.getMyCourseProgress(authToken).enqueue(new Callback<List<com.example.courseapp.model.Progress>>() {
            @Override
            public void onResponse(Call<List<com.example.courseapp.model.Progress>> call, Response<List<com.example.courseapp.model.Progress>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new MyCoursesAdapter(getContext(), response.body(), progress -> {
                        // Khi click vào khóa học → mở chi tiết
                        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                        intent.putExtra("COURSE_ID", progress.getCourseId());
                        startActivity(intent);
                    });
                    myCoursesRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Không tải được tiến độ khóa học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<com.example.courseapp.model.Progress>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

