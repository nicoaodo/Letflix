package com.example.letflix;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.letflix.adapter.MovieAdapter;
import com.example.letflix.adapter.MovieItemClickListener;
import com.example.letflix.adapter.SliderPagerAdapter;
import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.Slide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardActivity extends AppCompatActivity implements MovieItemClickListener {
    WebView testWebView;
    EditText secretCodeBox;
    Button joinBtn, shareBtn;

    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView MoviesRV ;

    private RelativeLayout playTGT, infoSlide;
    private CardView playAlone;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sliderpager = findViewById(R.id.slider_pager) ;
        indicator = findViewById(R.id.indicator);
        MoviesRV = findViewById(R.id.Rv_movies);
        context = this;

      // list slide: list trending se tu chay tren trang chu
        if(DATAMAIN.treding.size() != 0){
            SliderPagerAdapter adapter = new SliderPagerAdapter(this,DATAMAIN.treding);
            sliderpager.setAdapter(adapter);
        }

        // setup timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new DashboardActivity.SliderTimer(),4000,6000);
        indicator.setupWithViewPager(sliderpager,true);

//        //slide button
//        playTGT = sliderpager.findViewById(R.id.playtgt_fab);
//        playAlone = sliderpager.findViewById(R.id.play_fab);
//        infoSlide = sliderpager.findViewById(R.id.info_fab);
//
//        //onclick Info slide:
//        infoSlide.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                startActivity(new Intent(context, MovieDetailActivity.class));
//                MovieDetailActivity.indexGet = DATAMAIN.movies.get(DATAMAIN.treding.get(sliderpager.getCurrentItem()).value);
//            }
//        });

        // list movie nay hien ra het data movie
        MovieAdapter movieAdapter = new MovieAdapter(this,DATAMAIN.movies, this);
        MoviesRV.setAdapter(movieAdapter);
        MoviesRV.setLayoutManager(new GridLayoutManager(this, 3));

        //logo click listener
        ImageView btnLogo = findViewById(R.id.btnHome);
        btnLogo.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivity(new Intent(context, DashboardActivity.class));
            }
        });

        //search click listener
        ImageView btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivity(new Intent(context, SearchActivity.class));
            }
        });


        //bottom view click listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.itemHome:
                        startActivity(new Intent(context, DashboardActivity.class));
                        break;
                    case R.id.itemRap:
                        Toast.makeText(context, "Rạp", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.itemLogout:
                        startActivity(new Intent(context, LoginActivity.class));
                        break;
                }
                return false;
            }
        });

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


        Toast.makeText(this,"item clicked : " + movie.name,Toast.LENGTH_LONG).show();
    }


    class SliderTimer extends TimerTask {
        @Override
        public void run() {

            DashboardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (sliderpager.getCurrentItem() < DATAMAIN.treding.size()-1) {
                        sliderpager.setCurrentItem(sliderpager.getCurrentItem()+1);
                    }
                    else
                        sliderpager.setCurrentItem(0);


                }
            });


        }
    }
}