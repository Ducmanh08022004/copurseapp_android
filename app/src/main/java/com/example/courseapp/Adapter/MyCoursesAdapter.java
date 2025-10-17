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
import com.example.courseapp.model.Order;
import java.util.List;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.MyCourseViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnItemClickListener listener;

    //Interface để xử lý sự kiện click
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
        // Truyền listener vào hàm bind
        holder.bind(order, listener);
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

        public void bind(final Order order, final OnItemClickListener listener) {
            if (order != null && order.getCourse() != null) {
                myCourseTitleTextView.setText(order.getCourse().getTitle());
            }

            // Gán sự kiện click cho toàn bộ item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(order);
                }
            });
        }
    }
}

