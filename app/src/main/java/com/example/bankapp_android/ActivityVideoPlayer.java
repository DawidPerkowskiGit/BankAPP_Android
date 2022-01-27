package com.example.bankapp_android;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityVideoPlayer extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        /**
         *
         * Autor : https://www.tutlane.com/tutorial/android/android-video-player-with-examples
         */
        VideoView videoView =findViewById(R.id.videoViewVideoPlayer);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
}
