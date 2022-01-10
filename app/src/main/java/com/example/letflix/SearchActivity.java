package com.example.letflix;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letflix.adapter.MovieAdapter;
import com.example.letflix.adapter.MovieItemClickListener;
import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.MovieScore;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.TimerTask;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class SearchActivity extends AppCompatActivity implements MovieItemClickListener {

    private Context context;
    private TextView stringSearch, stringMessage;
    private int scoreFind = 60;

    private RecyclerView MoviesRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        stringSearch = findViewById(R.id.tbxSearch);
        stringMessage = findViewById(R.id.tbxMessage);

        MoviesRV = findViewById(R.id.Rv_movies);

        if(DATAMAIN.listFind != null){
            if(DATAMAIN.listFind.size() ==0)
                stringMessage.setText("Not found for \""+DATAMAIN.stringSearch+"\"");
            stringSearch.setText(DATAMAIN.stringSearch);
            // list movie nay hien ra het data movie
            MovieAdapter movieAdapter = new MovieAdapter(this,DATAMAIN.listFind, this);
            MoviesRV.setAdapter(movieAdapter);
            MoviesRV.setLayoutManager(new GridLayoutManager(this, 3));
        }

        //search click listener

        ImageView btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                search();
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
    }


    @Override
    public void onMovieClick(MovieData movie, ImageView movieImageView) {
        // here we send movie information to detail activity
        // also we ll create the transition animation between the two activity

        Intent intent = new Intent(this,MovieDetailActivity.class);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this,
                movieImageView,"sharedName");

        startActivity(intent,options.toBundle());
        MovieDetailActivity.indexGet = movie;


        Toast.makeText(this,"item clicked : " + movie.name,Toast.LENGTH_LONG).show();
        // it works great
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void search(){
        ArrayList<MovieScore> listGet = new ArrayList<MovieScore>();

        if (stringSearch.getText().toString().length() == 0)
            return;
        DATAMAIN.stringSearch = stringSearch.getText().toString();
        for (int i = 0; i < DATAMAIN.movies.size(); i++) {
            int scoreName = FuzzySearch.tokenSortPartialRatio(stringSearch.getText().toString(), DATAMAIN.movies.get(i).name);
            int scoreDirector = FuzzySearch.tokenSortPartialRatio(stringSearch.getText().toString(), DATAMAIN.movies.get(i).director);

            if (scoreName < scoreFind && scoreDirector < scoreFind)
                continue;

            if (scoreName < scoreDirector) scoreName = scoreDirector;
            MovieScore movieGet = new MovieScore(DATAMAIN.movies.get(i), scoreName);
            listGet.add(movieGet);
        }

        Collections.sort(listGet, new Comparator<MovieScore>() {
            public int compare(MovieScore i1, MovieScore i2) {
                return i1.score - i2.score;
            }
        }.reversed());
        DATAMAIN.listFind = listGet;

        for (int i = 0; i < DATAMAIN.listFind.size(); i++) {
            Log.d("dataGet", DATAMAIN.listFind.get(i).movie.name+"|" + DATAMAIN.listFind.get(i).score);
        }
        startActivity(new Intent(context, SearchActivity.class));
    }



    private void hideNavigationBar(){
        //Hide nav gesture
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }
}

