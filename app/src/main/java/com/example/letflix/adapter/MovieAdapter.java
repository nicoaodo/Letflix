package com.example.letflix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.letflix.R;
import com.example.letflix.SearchActivity;
import com.example.letflix.model.Movie;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.MovieScore;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    Context context ;
    List<MovieData> mData;
    MovieItemClickListener movieItemClickListener;


    public MovieAdapter(Context context, List<MovieData> mData, MovieItemClickListener listener) {
        this.context = context;
        this.mData = mData;
        movieItemClickListener = listener;
    }

    public MovieAdapter(SearchActivity context, List<MovieScore> listFind, SearchActivity listener) {
        this.context = context;

        ArrayList<MovieData> mDataGet = new ArrayList<MovieData>();
        for(int i=0;i<listFind.size();i++){
            mDataGet.add(listFind.get(i).movie);
        }
        this.mData = mDataGet;
        movieItemClickListener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.item_movie,viewGroup,false);
        return new MyViewHolder(view);


        }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.TvTitle.setText(mData.get(i).name);
        Glide.with(myViewHolder.TvTitle.getContext()).load(mData.get(i).rawImg).into(myViewHolder.ImgMovie);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView TvTitle;
        private ImageView ImgMovie;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            TvTitle = itemView.findViewById(R.id.item_movie_title);
            ImgMovie = itemView.findViewById(R.id.item_movie_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    movieItemClickListener.onMovieClick(mData.get(getAdapterPosition()),ImgMovie);

                }
            });

        }
    }
}
