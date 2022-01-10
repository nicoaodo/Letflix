package com.example.letflix;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.TheLoai;
import com.example.letflix.model.Trending;
import com.example.letflix.model.TypeLink;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {
    private static boolean isLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        DATAMAIN.contextCache = this;
        //Hide nav gesture
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);


        ///Dynamic link
        Uri uri = getIntent().getData();
        if (uri != null) {

            String url = uri.toString();
            url = url.split("http://www.letflix.com/")[1];

            if (url.split("/").length == 2) {
                String type = url.split("/")[0];
                String value = url.split("/")[1];
//                Toast.makeText(this, "Code invite: "+ type+"|"+value,Toast.LENGTH_LONG).show();
                if (type.toLowerCase().equals("invite"))
                    Toast.makeText(this, "Code invite: " + value, Toast.LENGTH_LONG).show();
                if (type.toLowerCase().equals("movie")) {
                    //set movie khi mà người dùng click vào link share phim từ người khác
                    //này sẽ tự động mở tới detail bộ phim luôn nhưng tại vì chưa xong cái keep me login nên phải hiện cái login nữa mới bay thẳng vào detail
                    DATAMAIN.typeLink = TypeLink.movie;
                    DATAMAIN.valueLink = value;
                }
            }
        }

        //loading list movie
        APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
        Call<List<MovieData>> call = methods.getListMovie("home");
        call.enqueue(new Callback<List<MovieData>>() {
            @Override
            public void onResponse(Call<List<MovieData>> call, Response<List<MovieData>> response) {
                DATAMAIN.movies = response.body();
                Log.d("dataGet", DATAMAIN.movies.size() + " home");
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
                Log.d("dataGet", DATAMAIN.theloai.size() + " theloai");
            }

            @Override
            public void onFailure(Call<TheLoai> call, Throwable t) {
                Log.d("dataGet", t.getMessage());
            }
        });

        //loading trending
        Call<List<Trending>> callTrending = methods.getTrending("trending");
        callTrending.enqueue(new Callback<List<Trending>>() {
            @Override
            public void onResponse(Call<List<Trending>> call, Response<List<Trending>> response) {
                DATAMAIN.treding = response.body();
                Log.d("dataGet", DATAMAIN.treding.size() + " trending");
            }

            @Override
            public void onFailure(Call<List<Trending>> call, Throwable t) {
                Log.d("dataGet", t.getMessage());
            }
        });

        Gson gson = new Gson();
        String readText = GetCacheDir.readAllCachedText(DATAMAIN.contextCache,DATAMAIN.CACHEACCOUNT);
        if(readText != "" && readText != null){
            User userLoad = gson.fromJson(readText , User.class);
            DATAMAIN.userLogin = userLoad;
            isLogin = true;
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLogin)
                    startActivity(new Intent(LoadingActivity.this, DashboardActivity.class));
                else
                    startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
            }
        }, 3000);
    }
}