package com.example.letflix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.TheLoai;
import com.example.letflix.model.Trending;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Uri uri = getIntent().getData();
        if(uri != null){
            String code = uri.toString();
            Toast.makeText(this, "Code invite: "+ code,Toast.LENGTH_LONG).show();
        }

        //loading list movie
        APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
        Call<List<MovieData>> call = methods.getListMovie("home");
        call.enqueue(new Callback<List<MovieData>>() {
            @Override
            public void onResponse(Call<List<MovieData>> call, Response<List<MovieData>> response) {
                DATAMAIN.movies = response.body();
                Log.d("dataGet", DATAMAIN.movies.size()+" home");
            }

            @Override
            public void onFailure(Call<List<MovieData>> call, Throwable t) {
                Log.d("dataGet", t.getMessage());
            }
        });

        //loading theloai
        Call<TheLoai> callTheLoai = methods.getTheLoai("theloai");
        callTheLoai.enqueue(new Callback<TheLoai>() {
            @Override
            public void onResponse(Call<TheLoai> call, Response<TheLoai> response) {
                DATAMAIN.theloai = response.body().type;
                Log.d("dataGet", DATAMAIN.theloai.size()+" theloai");
            }

            @Override
            public void onFailure(Call<TheLoai> call, Throwable t) {
                Log.d("dataGet", t.getMessage());
            }
        });

        //loading trending
        Call<List<Trending>> callTrending= methods.getTrending("trending");
        callTrending.enqueue(new Callback<List<Trending>>() {
            @Override
            public void onResponse(Call<List<Trending>> call, Response<List<Trending>> response) {
                DATAMAIN.treding =response.body();
                Log.d("dataGet", DATAMAIN.treding.size()+" trending");
            }

            @Override
            public void onFailure(Call<List<Trending>> call, Throwable t) {
                Log.d("dataGet", t.getMessage());
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
            }
    },3000);
    }
}