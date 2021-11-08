package com.example.footyapp;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoViewActivity extends AppCompatActivity {

    //   public static final String VIDEO_URL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    private ProgressDialog progressDialog;
    private VideoView videoView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = (VideoView) findViewById(R.id.videoView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Video Streaming");
        progressDialog.setMessage("Loading stream...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        progressDialog.show();

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);


        Uri uriVideo = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uriVideo);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                videoView.start();
            }
        });
    }
}
