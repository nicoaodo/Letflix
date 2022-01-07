package com.example.letflix;

import com.example.letflix.model.MovieData;
import com.example.letflix.model.TheLoai;
import com.example.letflix.model.Trending;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("index")
    Call<List<MovieData>> getListMovie(@Query("page") String page);

    @GET("index")
    Call<TheLoai> getTheLoai(@Query("page") String page);

    @GET("index")
    Call<List<Trending>> getTrending(@Query("page") String page);

}
