package com.example.courseapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.courseapp.R;
import com.example.courseapp.model.Exam;
import java.util.List;

public class ExamsAdapter extends RecyclerView.Adapter<ExamsAdapter.ExamViewHolder> {

    private Context context;
    private List<Exam> examList;
    private OnItemClickListener listener;

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Exam exam);
    }

    public ExamsAdapter(Context context, List<Exam> examList, OnItemClickListener listener) {
        this.context = context;
        this.examList = examList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        // Lấy dữ liệu và gán vào view
        Exam exam = examList.get(position);
        holder.bind(exam, listener);
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    // Lớp ViewHolder chứa các thành phần UI của một item
    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView examTitleTextView;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ view từ item_exam.xml
            examTitleTextView = itemView.findViewById(R.id.examTitleTextView);
        }

        public void bind(final Exam exam, final OnItemClickListener listener) {
            // Gán dữ liệu
            examTitleTextView.setText(exam.getTitle());

            // Gán sự kiện click cho toàn bộ item
            itemView.setOnClickListener(v -> listener.onItemClick(exam));
        }
    }
}