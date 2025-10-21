package com.example.courseapp.Activity;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.courseapp.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoPlayerActivity extends AppCompatActivity {


    PlayerView playerView;
    ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);


        playerView = findViewById(R.id.playerView);

        // Lấy URL video từ Intent
        String videoUrl = getIntent().getStringExtra("VIDEO_URL");

        if (videoUrl != null && !videoUrl.isEmpty()) {
            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);



            String computerIp = "10.0.2.2";
            String fullUrl = "http://" + computerIp + ":5000/" + videoUrl;

            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(fullUrl));
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        }
    }

    // Giải phóng player khi không dùng nữa
    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}