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

public class singlemovieAdapter extends ArrayAdapter<Star> {
    private ArrayList<Star> stars;

    public singlemovieAdapter(ArrayList<Star> stars, Context context) {
        super(context, R.layout.row2, stars);
        this.stars = stars;
        Log.d("Finish single view","");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("StartGetView","");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row2, parent, false);

        Star star = stars.get(position);
        String name = star.getName();
        String count = star.getCount();
        String temp = "No. of movies: " + count;
        //Log.d("singleListview", name);

        TextView titleView = view.findViewById(R.id.Stitle);
        TextView subtitleView = view.findViewById(R.id.subtitle3);

        titleView.setText(star.getName());
        subtitleView.setText(temp);

        return view;
    }

}
