package com.example.letflix;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.PostResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    public static MovieData indexGet;
    private ImageView MovieThumbnailImg,MovieCoverImg;
    private TextView tv_title,tv_description, tv_theloai, tv_director;
    private ImageView btnBackHome;
    private RelativeLayout playtgt_fab, share_fab;
    private CardView  play_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // ini views
        iniViews();

        btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MovieDetailActivity.this, DashboardActivity.class));
            }
        });

        //logo click listener
        ImageView btnLogo = findViewById(R.id.btnHome);
        btnLogo.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivity(new Intent(MovieDetailActivity.this, DashboardActivity.class));

            }
        });

    }

    void iniViews() {
        play_fab = findViewById(R.id.play_fab);
        playtgt_fab = findViewById(R.id.playtgt_fab);
        tv_title = findViewById(R.id.detail_movie_title);
        tv_description = findViewById(R.id.detail_movie_desc);
        tv_theloai = findViewById(R.id.theloai);
        tv_director = findViewById(R.id.director);
        MovieThumbnailImg = findViewById(R.id.detail_movie_img);
        MovieCoverImg = findViewById(R.id.detail_movie_cover);

        tv_title.setText(indexGet.name);
        tv_director.setText(indexGet.director + " - " + indexGet.release);
        tv_description = findViewById(R.id.detail_movie_desc);
        String typeGet = "Thể Loại: ";
        for (int i=0;i<indexGet.type.size();i++){
            if(i != 0)
                typeGet += ", ";
            typeGet += DATAMAIN.theloai.get(indexGet.type.get(i));
        }
        tv_theloai.setText(typeGet);
        tv_description.setText(indexGet.description);
        //getSupportActionBar().setTitle(indexGet.name);

        Glide.with(this).load(indexGet.rawImg).into(MovieThumbnailImg);
        Glide.with(this).load(indexGet.rawImg).into(MovieCoverImg);


        // setup animation
        MovieCoverImg.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));
        //play_fab.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));

        //share button action
        share_fab = findViewById(R.id.share_fab);
        share_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareSub = "Mời bạn xem.";
                String shareBody = indexGet.name + " trên Letflix.\nĐến xem ngay: http://www.letflix.com/movie/"+ indexGet._id;
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share"));
            }
        });


        //play button action
        play_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MovieDetailActivity.this, PlayVideoActivity.class));
                PlayVideoActivity.movie = indexGet;
            }
        });

        playtgt_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //req create room
                APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
                Call<PostResponse> call = methods.createRoom();
                call.enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        Log.d("dataGet", response.body().message + " Room");
                        startActivity(new Intent(MovieDetailActivity.this, PlayVideoTGTActivity.class));
                        PlayVideoTGTActivity.code = response.body().message;
                        PlayVideoTGTActivity.isStart = true;
                        PlayVideoTGTActivity.url = indexGet.url;
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {
                        Log.d("dataGet", t.getMessage());
                    }
                });


            }
        });

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