package com.example.letflix;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.letflix.adapter.MovieAdapter;
import com.example.letflix.adapter.MovieItemClickListener;
import com.example.letflix.adapter.SliderPagerAdapter;
import com.example.letflix.model.CheckRoom;
import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.Slide;
import com.example.letflix.model.TypeLink;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

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

    private NestedScrollView sv;

    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView MoviesRV;

    private RelativeLayout playTGT, infoSlide;
    private CardView playAlone;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sliderpager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);
        MoviesRV = findViewById(R.id.Rv_movies);
//        sv = findViewById(R.id.scrollView);
//        sv.setNestedScrollingEnabled(false);
//        sv.setScrollY(0);
        context = this;
        // list slide: list trending se tu chay tren trang chu
        if (DATAMAIN.treding.size() != 0) {
            SliderPagerAdapter adapter = new SliderPagerAdapter(this, DATAMAIN.treding);
            sliderpager.setAdapter(adapter);
        }

        // setup timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        indicator.setupWithViewPager(sliderpager, true);


        // list movie nay hien ra het data movie
        MovieAdapter movieAdapter = new MovieAdapter(this, DATAMAIN.movies, this);
        MoviesRV.setAdapter(movieAdapter);
        MoviesRV.setLayoutManager(new GridLayoutManager(this, 3));

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
                switch (item.getItemId()) {
                    case R.id.itemHome:
                        //startActivity(new Intent(context, DashboardActivity.class));
                        break;
                    case R.id.itemRap:
                        startActivity(new Intent(context, TheaterActivity.class));
                        break;
                    case R.id.itemLogout:
                        boolean success = GetCacheDir.writeAllCachedText(DATAMAIN.contextCache, DATAMAIN.CACHEACCOUNT, "");
                        if(success)
                            startActivity(new Intent(context, LoginActivity.class));

                        break;
                }
                return false;
            }
        });

        if(DATAMAIN.typeLink == TypeLink.movie){
            MovieData movieGet = null;
            for (int i=0;i<DATAMAIN.movies.size();i++){
                if(DATAMAIN.movies.get(i)._id.equals(DATAMAIN.valueLink))
                    movieGet = DATAMAIN.movies.get(i);
            }
            if(movieGet != null){
                MovieDetailActivity.indexGet = movieGet;
                startActivity(new Intent(this, MovieDetailActivity.class));
                DATAMAIN.typeLink = TypeLink.none;
                DATAMAIN.valueLink = "";
                return;
            }
        }else if(DATAMAIN.typeLink == TypeLink.invite){
            //loading join room
            APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
            Call<CheckRoom> callJoin = methods.joinRoom(DATAMAIN.valueLink);
            callJoin.enqueue(new Callback<CheckRoom>() {
                @Override
                public void onResponse(Call<CheckRoom> call, Response<CheckRoom> response) {
                    Log.d("dataGet", response.body().status + " Room code");
                    if(response.body().status){
                        startActivity(new Intent(DashboardActivity.this, PlayVideoTGTActivity.class));
                        PlayVideoTGTActivity.isStart = false;
                        PlayVideoTGTActivity.code = DATAMAIN.valueLink.toUpperCase();
                    }else
                        Toast.makeText(DashboardActivity.this, "Code Invalid!", Toast.LENGTH_LONG).show();

                    DATAMAIN.typeLink = null;
                }

                @Override
                public void onFailure(Call<CheckRoom> call, Throwable t) {
                    Toast.makeText(DashboardActivity.this, "Code Invalid!", Toast.LENGTH_LONG).show();
                    Log.d("dataGet", t.getMessage());

                    DATAMAIN.typeLink = null;
                }
            });
        }
    }

    @Override
    public void onMovieClick(MovieData movie, ImageView movieImageView) {
        // here we send movie information to detail activity
        // also we ll create the transition animation between the two activity

        Intent intent = new Intent(this, MovieDetailActivity.class);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DashboardActivity.this,
                movieImageView, "sharedName");

        startActivity(intent, options.toBundle());
        MovieDetailActivity.indexGet = movie;
    }


    class SliderTimer extends TimerTask {
        @Override
        public void run() {

            DashboardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (sliderpager.getCurrentItem() < DATAMAIN.treding.size() - 1) {
                        sliderpager.setCurrentItem(sliderpager.getCurrentItem() + 1);
                    } else
                        sliderpager.setCurrentItem(0);

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

}

