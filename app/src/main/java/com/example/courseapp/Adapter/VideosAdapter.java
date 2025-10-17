package com.example.courseapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.courseapp.R;
import com.example.courseapp.model.Video;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private Context context;
    private List<Video> videoList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Video video);
    }

    public VideosAdapter(Context context, List<Video> videoList, OnItemClickListener listener) {
        this.context = context;
        this.videoList = videoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.bind(video, listener);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView videoTitleTextView;
        TextView videoDurationTextView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTitleTextView = itemView.findViewById(R.id.videoTitleTextView);
            videoDurationTextView = itemView.findViewById(R.id.videoDurationTextView);
        }

        public void bind(final Video video, final OnItemClickListener listener) {
            videoTitleTextView.setText(video.getTitle());
            videoDurationTextView.setText(video.getDuration());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(video);
                }
            });
        }
    }
}
