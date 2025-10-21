package com.example.courseapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseapp.R;
import com.example.courseapp.model.UserExam;

import java.text.DecimalFormat;
import java.util.List;

public class UserExamAdapter extends RecyclerView.Adapter<UserExamAdapter.ViewHolder> {

    private final Context context;
    private final List<UserExam> examList;

    public UserExamAdapter(Context context, List<UserExam> examList) {
        this.context = context;
        this.examList = examList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_exam, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserExam exam = examList.get(position);
        holder.bind(exam);
    }

    @Override
    public int getItemCount() {
        return examList != null ? examList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvExamTitle, tvScore, tvPercent, tvTotalQuestions, tvCourseTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExamTitle = itemView.findViewById(R.id.tvExamTitle);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvPercent = itemView.findViewById(R.id.tvPercent);
            tvTotalQuestions = itemView.findViewById(R.id.tvTotalQuestions);
            tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
        }

        public void bind(UserExam exam) {
            DecimalFormat df = new DecimalFormat("#.##");
            if (exam.getExam() != null && exam.getExam().getTitle() != null) {
                tvExamTitle.setText("Bài thi: " + exam.getExam().getTitle());
            } else {
                tvExamTitle.setText("Bài thi #" + exam.getExamId());
            }
            tvCourseTitle.setText("Khóa học: " + exam.getExam().getCourse().getTitle());
            tvScore.setText("Điểm: " + exam.getScore());
            tvTotalQuestions.setText("Số câu: " + exam.getTotalQuestions());
            tvPercent.setText("Tỷ lệ đúng: " + df.format(exam.getPercent()) + "%");
        }
    }
}
