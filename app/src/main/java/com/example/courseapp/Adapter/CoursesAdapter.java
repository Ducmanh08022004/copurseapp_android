package com.example.courseapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.courseapp.R;
import com.example.courseapp.model.Course; // Bạn cần tạo lớp model Course.java
import java.util.List;


public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private Context context;
    private List<Course> courseList;
    private OnItemClickListener listener;

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Course course);
    }

    public CoursesAdapter(Context context, List<Course> courseList, OnItemClickListener listener) {
        this.context = context;
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ file XML
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        // Lấy dữ liệu từ danh sách và gán vào các view
        Course course = courseList.get(position);
        holder.bind(course, listener);
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng item trong danh sách
        return courseList.size();
    }

    // Lớp ViewHolder chứa các thành phần UI của một item
    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImageView;
        TextView courseTitleTextView;
        TextView courseDescriptionTextView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImageView = itemView.findViewById(R.id.courseImageView);
            courseTitleTextView = itemView.findViewById(R.id.courseTitleTextView);
            courseDescriptionTextView = itemView.findViewById(R.id.courseDescriptionTextView);
        }

        public void bind(final Course course, final OnItemClickListener listener) {
            courseTitleTextView.setText(course.getTitle());
            courseDescriptionTextView.setText(course.getDescription());

            // Dùng thư viện Glide hoặc Picasso để tải ảnh từ URL
            // Glide.with(itemView.getContext()).load(course.getImageUrl()).into(courseImageView);

            // Gán sự kiện click cho toàn bộ item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(course);
                }
            });
        }
    }
}