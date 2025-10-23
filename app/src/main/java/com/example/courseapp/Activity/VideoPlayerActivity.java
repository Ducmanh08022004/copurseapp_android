package com.example.courseapp.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.common.MediaItem;
import androidx.media3.ui.PlayerView;
import androidx.media3.common.Player;

import com.example.courseapp.R;
import com.example.courseapp.api.RetrofitClient;
import com.example.courseapp.model.response.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;
    private String fullUrl;
    private int videoId;
    private boolean progressUpdated = false; // cờ để tránh gửi nhiều lần
    private String authToken;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.playerView);

        // Nhận dữ liệu từ Intent
        String videoUrl = getIntent().getStringExtra("VIDEO_URL");
        videoId = getIntent().getIntExtra("VIDEO_ID", -1);

        // Lấy token từ SharedPreferences
        authToken = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                .getString("USER_TOKEN", null);

        if (videoUrl != null && !videoUrl.isEmpty()) {
            String computerIp = "10.0.2.2";
            fullUrl = "http://" + computerIp + ":5000/" + videoUrl;
            initializePlayer();
        }
    }

    private void initializePlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(fullUrl));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        //  Thêm listener theo dõi tiến trình
        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    monitorProgress();
                }
            }
        });
    }

    // Theo dõi % xem video
    private void monitorProgress() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (player != null && player.getDuration() > 0) {
                    long position = player.getCurrentPosition();
                    long duration = player.getDuration();
                    double percent = (position * 100.0 / duration);

                    if (percent >= 10 && !progressUpdated) {
                        progressUpdated = true;
                        markVideoAsCompleted();
                    }
                }

                // nếu video vẫn chưa kết thúc thì tiếp tục kiểm tra mỗi giây
                if (player != null && !progressUpdated) {
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    //  Gửi request cập nhật progress
    private void markVideoAsCompleted() {
        if (authToken == null || videoId == -1) return;
        String bearerToken = "Bearer " + authToken;
        RetrofitClient.getApiService()
                .markVideoCompleted(bearerToken, videoId)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> Toast.makeText(VideoPlayerActivity.this, "Đã cập nhật tiến độ!", Toast.LENGTH_SHORT).show());
                        } else {
                            Log.e("VideoProgress", "Failed: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.e("VideoProgress", "Error: " + t.getMessage());
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
