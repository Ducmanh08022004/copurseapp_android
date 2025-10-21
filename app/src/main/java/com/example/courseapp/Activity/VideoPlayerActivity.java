package com.example.courseapp.Activity;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.courseapp.R;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.common.MediaItem;
import androidx.media3.ui.PlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;
    private String fullUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.playerView);

        // Lấy URL video từ Intent
        String videoUrl = getIntent().getStringExtra("VIDEO_URL");

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
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    // Tiếp tục phát khi quay lại
    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.play();
        }
    }

    // Giải phóng hoàn toàn khi Activity bị hủy
    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
