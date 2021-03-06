package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MovieListViewAdapter extends ArrayAdapter<Movie> {
    private ArrayList<Movie> movies;

    public MovieListViewAdapter(ArrayList<Movie> movies, Context context) {
        super(context, R.layout.row, movies);
        this.movies = movies;
        Log.d("Finish movie list view","");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row, parent, false);

        Movie movie = movies.get(position);

        TextView titleView = view.findViewById(R.id.title);
        TextView subtitleView = view.findViewById(R.id.subtitle);
        TextView subtitleView2 = view.findViewById(R.id.subtitle2);

        //Log.d("MovieList.success", movie.getName());

        titleView.setText(movie.getName());
        subtitleView.setText(movie.getYear() + "    "+ movie.getDirector() + "    " + movie.getRating());// need to cast the year to a string to set the label
        subtitleView2.setText(movie.getStars() + "    "+ movie.getGenre());// need to cast the year to a string to set the label

        return view;
    }
}