package com.example.letflix;

import android.os.Bundle;
import android.widget.Toast;

import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayVideoActivity extends YouTubeBaseActivity {
    public static MovieData movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        String api_key = DATAMAIN.APIYT;
        String idVideo = movie.url.split("v=")[1];

        // Get reference to the view of Video player
        YouTubePlayerView ytPlayer = findViewById(R.id.ytPlayer);
        ytPlayer.initialize(
                api_key,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(
                            YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(idVideo);
                        youTubePlayer.play();
                        youTubePlayer.setFullscreen(true);
                        youTubePlayer.setShowFullscreenButton(false);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult
                                                                youTubeInitializationResult) {
                        Toast.makeText(getApplicationContext(), "Không có kết nối", Toast.LENGTH_SHORT).show();
                    }


                });


    }

    private String getTime(int duration) {
        int seconds = (int) Math.floor((duration / 1000) % 60);
        int minutes = (int) Math.floor((duration / (1000 * 60)) % 60);
        int hours = (int) Math.floor((duration / (1000 * 60 * 60)) % 24);

        String h, m, s;

        if (hours < 10) {
            h = "0" + hours;
        } else {
            h = ""+hours;
        }

        if (minutes < 10) {
            m = "0" + minutes;
        } else {
            m = ""+minutes;
        }

        if (seconds < 10) {
            s = "0" + seconds;
        } else {
            s = ""+seconds;
        }


        return "" + h + ":" + m + ":" + s;
    }
}