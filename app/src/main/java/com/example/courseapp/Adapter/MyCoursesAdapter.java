package com.example.courseapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.courseapp.R;
import com.example.courseapp.model.Progress;

import java.util.List;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.MyCourseViewHolder> {

    private final Context context;
    private final List<Progress> progressList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Progress progress);
    }

    public MyCoursesAdapter(Context context, List<Progress> progressList, OnItemClickListener listener) {
        this.context = context;
        this.progressList = progressList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_course, parent, false);
        return new MyCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCourseViewHolder holder, int position) {
        Progress progress = progressList.get(position);
        holder.bind(context, progress, listener);
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    public static class MyCourseViewHolder extends RecyclerView.ViewHolder {
        ImageView myCourseImageView;
        TextView myCourseTitleTextView, myCourseProgressTextView;
        ProgressBar myCourseProgressBar;

        public MyCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            myCourseImageView = itemView.findViewById(R.id.myCourseImageView);
            myCourseTitleTextView = itemView.findViewById(R.id.myCourseTitleTextView);
            myCourseProgressTextView = itemView.findViewById(R.id.myCourseProgressTextView);
            myCourseProgressBar = itemView.findViewById(R.id.myCourseProgressBar);
        }

        public void bind(Context context, final Progress progress, final OnItemClickListener listener) {
            myCourseTitleTextView.setText(progress.getTitle());
            int percent = (int) progress.getTotalPercent();
            myCourseProgressTextView.setText("Tiến độ: " + percent + "%");
            myCourseProgressBar.setProgress(percent);

            //  Hiển thị ảnh khóa học
            if (progress.getImageUrl() != null) {
                String fullUrl = "http://10.0.2.2:5000" + progress.getImageUrl();
                Glide.with(context)
                        .load(fullUrl)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(myCourseImageView);
            } else {
                myCourseImageView.setImageResource(R.drawable.placeholder_image);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(progress);
            });
        }
    }
}
