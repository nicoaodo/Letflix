package com.example.letflix;

import com.example.letflix.model.MovieData;
import com.example.letflix.model.PostResponse;
import com.example.letflix.model.TheLoai;
import com.example.letflix.model.Trending;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("index")
    Call<List<MovieData>> getListMovie(@Query("page") String page);

    @GET("index")
    Call<TheLoai> getTheLoai(@Query("page") String page);

    @GET("index")
    Call<List<Trending>> getTrending(@Query("page") String page);

    @POST("import/users")
    Call<PostResponse> setUser(@Body User body);

}
