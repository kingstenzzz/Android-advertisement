package com.example.v310.adproject;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.vov.vitamio.widget.VideoView;

public class Video_Thread extends  Thread   {
    private  String uri="rtmp://192.168.145.135:2016/hls_alic/film";
    VideoView AD_Video;
    @Override
    public void run() {
        super.run();
        try{

            AD_Video.setVideoPath(uri);
            AD_Video.setOnPreparedListener(new io.vov.vitamio.MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(io.vov.vitamio.MediaPlayer mp) {
                    AD_Video.start();

                }
            });
        }catch (Exception e){
            e.printStackTrace();
         //   Toast.makeText(MainActivity.this,"视频播放出错",Toast.LENGTH_SHORT).show();
        }
    }
}
