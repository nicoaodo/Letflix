package com.example.letflix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.TheLoai;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //loading list movie
        APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
        Call<List<MovieData>> call = methods.getListMovie("home");
        call.enqueue(new Callback<List<MovieData>>() {
            @Override
            public void onResponse(Call<List<MovieData>> call, Response<List<MovieData>> response) {
                DATAMAIN.movies = response.body();
                Log.d("dataGet", DATAMAIN.movies.size()+"");
            }

            @Override
            public void onFailure(Call<List<MovieData>> call, Throwable t) {
                Log.d("dataGet", t.getMessage());
            }
        });

        //loading theloai
        Call<List<String>> callTheLoai = methods.getTheLoai("theloai");
        callTheLoai.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                DATAMAIN.theloai = response.body();
                Log.d("dataGet", DATAMAIN.theloai.size()+"");
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d("dataGet", t.getMessage());
            }
        });

        //loading trending
        Call<List<Integer>> callTrending= methods.getTrending("trending");
        callTrending.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                DATAMAIN.treding = response.body();
                Log.d("dataGet", DATAMAIN.treding.size()+"");
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
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