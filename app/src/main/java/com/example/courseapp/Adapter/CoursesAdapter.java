package com.example.courseapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.courseapp.R;
import com.example.courseapp.model.Course;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private final Context context;
    private List<Course> courseList;
    private final OnItemClickListener listener;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.bind(context, course, listener);
    }

    @Override
    public int getItemCount() {
        return courseList != null ? courseList.size() : 0;
    }

    public void updateList(List<Course> newList) {
        this.courseList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder
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

        public void bind(Context context, final Course course, final OnItemClickListener listener) {
            courseTitleTextView.setText(course.getTitle());
            courseDescriptionTextView.setText(course.getDescription());

            String img = "http://10.0.2.2:5000";
            Glide.with(context)
                    .load(img + course.getImageUrl())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(courseImageView);

            itemView.setOnClickListener(v -> listener.onItemClick(course));
        }
    }
}
