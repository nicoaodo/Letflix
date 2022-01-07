package com.example.letflix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MovieDetailActivity extends AppCompatActivity {
    public static MovieData indexGet;
    private ImageView MovieThumbnailImg,MovieCoverImg;
    private TextView tv_title,tv_description, tv_theloai, tv_director;
    private FloatingActionButton play_fab;
    private Button share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // ini views
        iniViews();

        //share button action
        share = findViewById(R.id.btnShare);
        share.setOnClickListener(new View.OnClickListener() {
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


    }

    void iniViews() {
        play_fab = findViewById(R.id.play_fab);

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
        play_fab.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));

    }
}