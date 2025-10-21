package com.example.courseapp.Activity.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.courseapp.Activity.CourseDetailActivity;
import com.example.courseapp.Adapter.CoursesAdapter;
import com.example.courseapp.api.ApiService;
import com.example.courseapp.R;
import com.example.courseapp.api.RetrofitClient;
import com.example.courseapp.model.Course;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesFragment extends Fragment {

    RecyclerView coursesRecyclerView;
    CoursesAdapter adapter;
    SearchView searchView;

    List<Course> fullCourseList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView);
        searchView = view.findViewById(R.id.searchView);

        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchCourses();

        // Lắng nghe khi người dùng nhập vào ô tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCourses(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCourses(newText);
                return true;
            }
        });
    }

    private void fetchCourses() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getAllCourses().enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                Log.d("API_RESPONSE", "onResponse: " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    fullCourseList = response.body();
                    adapter = new CoursesAdapter(getContext(), fullCourseList, course -> {
                        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                        intent.putExtra("COURSE_ID", course.getId());
                        startActivity(intent);
                    });
                    coursesRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "onFailure: ", t);
            }
        });
    }

    private void filterCourses(String query) {
        if (adapter == null || fullCourseList == null) return;

        List<Course> filteredList = new ArrayList<>();
        for (Course course : fullCourseList) {
            if (course.getTitle() != null &&
                    course.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(course);
            }
        }
        adapter.updateList(filteredList);
    }
}
