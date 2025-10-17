package com.example.courseapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.courseapp.R;
import com.example.courseapp.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView courseTitleTextView, orderDateTextView, orderStatusTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitleTextView = itemView.findViewById(R.id.courseTitleTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
        }

        public void bind(Order order) {
            if (order.getCourse() != null) {
                courseTitleTextView.setText(order.getCourse().getTitle());
            } else {
                courseTitleTextView.setText("Khóa học không xác định");
            }

            orderStatusTextView.setText(order.getStatus().equalsIgnoreCase("paid") ? "Đã thanh toán" : "Chờ thanh toán");
            orderDateTextView.setText(formatDate(String.valueOf(order.getCreatedAt())));
        }

        private String formatDate(String isoDate) {
            if (isoDate == null) return "Không rõ ngày";
            try {
                // Định dạng này khớp với `createdAt` của Sequelize
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = isoFormat.parse(isoDate);
                return "Ngày: " + targetFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return isoDate;
            }
        }
    }
}