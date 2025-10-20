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
import com.example.courseapp.model.Order;

import java.util.List;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.MyCourseViewHolder> {

    private final Context context;
    private final List<Order> orderList;
    private final OnItemClickListener listener;

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public MyCoursesAdapter(Context context, List<Order> orderList, OnItemClickListener listener) {
        this.context = context;
        this.orderList = orderList;
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
        Order order = orderList.get(position);
        holder.bind(context, order, listener);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class MyCourseViewHolder extends RecyclerView.ViewHolder {
        ImageView myCourseImageView;
        TextView myCourseTitleTextView;

        public MyCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            myCourseImageView = itemView.findViewById(R.id.myCourseImageView);
            myCourseTitleTextView = itemView.findViewById(R.id.myCourseTitleTextView);
        }

        public void bind(Context context, final Order order, final OnItemClickListener listener) {
            if (order != null && order.getCourse() != null) {
                myCourseTitleTextView.setText(order.getCourse().getTitle());

                // 🖼️ Load ảnh bằng Glide
                Glide.with(context)
                        .load(order.getCourse().getImageUrl()) // URL ảnh của khóa học
                        .placeholder(R.drawable.loading)       // ảnh tạm khi đang tải
                        .error(R.drawable.error)               // ảnh hiển thị khi lỗi
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(myCourseImageView);
            }

            // Gán sự kiện click
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(order);
                }
            });
        }
    }
}
