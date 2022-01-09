package com.example.letflix.model;

import com.example.letflix.model.MovieData;

public class MovieScore{
    public MovieData movie;
    public int score;

    public MovieScore(MovieData movie, int score) {
        this.movie = movie;
        this.score = score;
    }
}
