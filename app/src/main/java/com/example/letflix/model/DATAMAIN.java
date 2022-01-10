package com.example.letflix.model;

import android.content.Context;

import com.example.letflix.User;

import java.util.List;

public class DATAMAIN {
    public static String BASE_URL = "http://20.192.4.125:3003/";

    public static List<MovieData> movies;
    public static List<String> theloai;
    public static List<Trending> treding;

    public static String APIYT = "AIzaSyBolhBkPwQuZHgY_jOnX6KsZa39DT_Fe30";

    public static TypeLink typeLink = TypeLink.none;
    public static String valueLink;

    public static String CACHEACCOUNT = "cacheAccount.txt";

    public static List<MovieScore> listFind;
    public static String stringSearch;
    public static User userLogin;
    public static Context contextCache;
}

