package com.example.courseapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.courseapp.R;
import com.example.courseapp.model.Question;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private Context context;
    private List<Question> questionList;

    public QuestionsAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.bind(question);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    // Phương thức để lấy lại danh sách câu hỏi với câu trả lời đã chọn
    public List<Question> getAnsweredQuestions() {
        return this.questionList;
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionContentTextView;
        RadioGroup answersRadioGroup;
        RadioButton answerA, answerB, answerC, answerD;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionContentTextView = itemView.findViewById(R.id.questionContentTextView);
            answersRadioGroup = itemView.findViewById(R.id.answersRadioGroup);
            answerA = itemView.findViewById(R.id.answerA);
            answerB = itemView.findViewById(R.id.answerB);
            answerC = itemView.findViewById(R.id.answerC);
            answerD = itemView.findViewById(R.id.answerD);
        }

        public void bind(final Question question) {
            questionContentTextView.setText(getAdapterPosition() + 1 + ". " + question.getContent());

            // Giả sử câu trả lời nằm trong một List<String> trong model Question
            List<String> answers = question.getAnswers();
            answerA.setText(answers.get(0));
            answerB.setText(answers.get(1));
            answerC.setText(answers.get(2));
            answerD.setText(answers.get(3));

            // Xóa listener cũ để tránh lỗi khi tái sử dụng ViewHolder
            answersRadioGroup.setOnCheckedChangeListener(null);

            // Khôi phục lựa chọn của người dùng nếu có
            answersRadioGroup.clearCheck();
            if (question.getUserAnswer() != null) {
                switch (question.getUserAnswer()) {
                    case "A": answersRadioGroup.check(R.id.answerA); break;
                    case "B": answersRadioGroup.check(R.id.answerB); break;
                    case "C": answersRadioGroup.check(R.id.answerC); break;
                    case "D": answersRadioGroup.check(R.id.answerD); break;
                }
            }

            // Lắng nghe sự kiện khi người dùng chọn một đáp án
            answersRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.answerA) {
                        question.setUserAnswer("A");
                    } else if (checkedId == R.id.answerB) {
                        question.setUserAnswer("B");
                    } else if (checkedId == R.id.answerC) {
                        question.setUserAnswer("C");
                    } else if (checkedId == R.id.answerD) {
                        question.setUserAnswer("D");
                    }
                }
            });
        }
    }
}
