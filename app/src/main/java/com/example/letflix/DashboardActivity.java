package com.example.letflix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.adapter.MovieAdapter;
import com.example.letflix.adapter.MovieItemClickListener;
import com.example.letflix.adapter.SliderPagerAdapter;
import com.example.letflix.model.Movie;
import com.example.letflix.model.Slide;
import com.example.letflix.model.TheLoai;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements MovieItemClickListener {
    WebView testWebView;
    EditText secretCodeBox;
    Button joinBtn, shareBtn;
    private List<Slide> lstSlides;
    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView MoviesRV ;
    private RecyclerView MoviesRV1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sliderpager = findViewById(R.id.slider_pager) ;
        indicator = findViewById(R.id.indicator);
        MoviesRV = findViewById(R.id.Rv_movies);


      // list slide: list trending se tu chay tren trang chu
        if(DATAMAIN.treding.size() != 0){
            SliderPagerAdapter adapter = new SliderPagerAdapter(this,DATAMAIN.treding);
            sliderpager.setAdapter(adapter);
        }

        // setup timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new DashboardActivity.SliderTimer(),4000,6000);
        indicator.setupWithViewPager(sliderpager,true);

        // list movie nay hien ra het data movie
        MovieAdapter movieAdapter = new MovieAdapter(this,DATAMAIN.movies, this);
        MoviesRV.setAdapter(movieAdapter);
        MoviesRV.setLayoutManager(new GridLayoutManager(this, 3));

//        testWebView = findViewById(R.id.testWebView);
//        testWebView.loadUrl("https://www.youtube.com/embed/DIfYVUgndJo");
//        testWebView.getSettings().setJavaScriptEnabled(true);
//        testWebView.setWebChromeClient (new WebChromeClient());

//        secretCodeBox = findViewById(R.id.codeBox);
//        joinBtn = findViewById(R.id.joinBtn);
//        shareBtn = findViewById(R.id.shareBtn);

//        URL serverURL;
//
//
//        try {
//            serverURL = new URL("https://meet.jit.si");
//            JitsiMeetConferenceOptions defaultOptions =
//                    new JitsiMeetConferenceOptions.Builder()
//                            .setServerURL(serverURL)
//                            .setWelcomePageEnabled(false)
//                            .build();
//            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }





//        joinBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                        .setRoom(secretCodeBox.getText().toString())
//                        .setWelcomePageEnabled(false)
//                        .build();
//
//                JitsiMeetActivity.launch(DashboardActivity.this, options);
//            }
//        });
    }
    @Override
    public void onMovieClick(MovieData movie, ImageView movieImageView) {
        // here we send movie information to detail activity
        // also we ll create the transition animation between the two activity

        Intent intent = new Intent(this,MovieDetailActivity.class);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DashboardActivity.this,
                movieImageView,"sharedName");

        startActivity(intent,options.toBundle());
        MovieDetailActivity.indexGet = movie;


        // i l make a simple test to see if the click works

        Toast.makeText(this,"item clicked : " + movie.name,Toast.LENGTH_LONG).show();
        // it works great


    }



    class SliderTimer extends TimerTask {


        @Override
        public void run() {

            DashboardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderpager.getCurrentItem()<DATAMAIN.treding.size()-1) {
                        sliderpager.setCurrentItem(sliderpager.getCurrentItem()+1);
                    }
                    else
                        sliderpager.setCurrentItem(0);
                }
            });


        }
    }
}