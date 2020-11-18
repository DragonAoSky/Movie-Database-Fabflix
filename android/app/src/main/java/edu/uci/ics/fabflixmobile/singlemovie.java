package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class singlemovie extends Activity {

    private String url;
    final ArrayList<Star> stars = new ArrayList<>();
    //private String movieID;

    private TextView Title;
    private TextView Year;
    private TextView Director;
    private TextView Rating;
    private TextView Genres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlemovie);

        Title = findViewById(R.id.MovieTitle);
        Year = findViewById(R.id.MovieYear);
        Director = findViewById(R.id.Moviedirector);
        Rating = findViewById(R.id.Movierating);
        Genres = findViewById(R.id.Moviegenre);

        //this should be retrieved from the database and the backend server

        //url = "https://10.0.2.2:8443/cs122b_spring20_project3_war/api/";
        url = "https://ec2-3-136-17-82.us-east-2.compute.amazonaws.com:8443/cs122b-spring20-project4/api/";


        //movieID = staticMovie.getId();
        //SingleMovie();
        String temp_response = staticMovie.getRes();

//        /Toast.makeText(getApplicationContext(), temp_response, Toast.LENGTH_SHORT).show();

        try {
            JSONArray jsonarray = new JSONArray(temp_response);
            for (int i = 0; i < jsonarray.length(); i++) {

                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if(i == 0)
                {
                    String name = jsonobject.getString("movieTitle");
                    String year = jsonobject.getString("movieYear");
                    String director = jsonobject.getString("movieDirector");
                    String gen1 = jsonobject.getString("gen1");
                    String gen2 = jsonobject.getString("gen2");
                    String gen3 = jsonobject.getString("gen3");
                    String id = jsonobject.getString("movieId");
                    String rating = jsonobject.getString("movieRating");
                    String genres = gen1;
                    if(!gen2.equals(""))
                        genres += "," + gen2;
                    if(!gen3.equals(""))
                        genres += "," + gen3;
//
//                                movie.setTitle(name);
//                                movie.setDirector(director);
//                                movie.setYear(year);
//                                movie.setRating(rating);
//                                movie.setGenres(genres);

                                Title.setText("Title: " + name);
                                Year.setText("Year: " + year);
                                Director.setText("Director: " + director);
                                Rating.setText("Rating: " + rating);
                                Genres.setText("Genres: " + genres);
                }
                else
                {
                    //Star s = new Star();
                    String starName = jsonobject.getString("starName");
                    String count = jsonobject.getString("count");
                    String starID = jsonobject.getString("starID");
//                                s.setCount(count);
//                                s.setId(starID);
//                                s.setName(starName);
                    //Toast.makeText(getApplicationContext(), "Star is " + s.toString(), Toast.LENGTH_SHORT).show();
                    //add to list
                    // movie.addStar(s);
                    Log.d("star.info", starName + " " + starID + " " + count);
                    stars.add(new Star(starName,starID,count));
                    //stars.add(new Star("The Terminal", "2004",""));
                }



            }
            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }






        singlemovieAdapter adapter2 = new singlemovieAdapter(stars, this);
        ListView listView2 = findViewById(R.id.starlist);
        listView2.setAdapter(adapter2);



    }




}