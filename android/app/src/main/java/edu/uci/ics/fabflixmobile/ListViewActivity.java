package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
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

public class ListViewActivity extends Activity {


    private String url;
    private String url2;
    private String content;
    private String MovieID;
    final ArrayList<Movie> movies = new ArrayList<>();
    private Button prev;
    private Button next;

    private MovieListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        //this should be retrieved from the database and the backend server
        staticMovie.setId("");

//        url = "https://10.0.2.2:8443/cs122b_spring20_project3_war/api/";
//        url2 = "https://10.0.2.2:8443/cs122b_spring20_project3_war/api/"; //

        url = "https://ec2-3-136-17-82.us-east-2.compute.amazonaws.com:8443/cs122b-spring20-project4/api/";
        url2 = "https://ec2-3-136-17-82.us-east-2.compute.amazonaws.com:8443/cs122b-spring20-project4/api/";


        content = searchContent.getContent();


        String temp_response = staticMovie.getRes();
        //parse JSON obj
        try {
            JSONArray jsonarray = new JSONArray(temp_response);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String name = jsonobject.getString("movie_title");
                String year = jsonobject.getString("movie_year");
                String director = jsonobject.getString("movie_director");
                String gen1 = jsonobject.getString("gen1");
                String gen2 = jsonobject.getString("gen2");
                String gen3 = jsonobject.getString("gen3");
                String star1 = jsonobject.getString("star1");
                String star2 = jsonobject.getString("star2");
                String star3 = jsonobject.getString("star3");
                String id = jsonobject.getString("movie_id");
                String star = star1;
                String genres = gen1;
                String rating = jsonobject.getString("movie_rating");

                //start edit star and genres
                if(!gen2.equals(""))
                    genres += "," + gen2;
                if(!gen3.equals(""))
                    genres += "," + gen3;
                if(!star2.equals(""))
                    star += star2;
                if(!star3.equals(""))
                    star += star3;

                //add to list
                movies.add(new Movie(name, year,director,genres,star,id,rating));

            }
            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }



        adapter = new MovieListViewAdapter(movies, this);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);







        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movies.get(position);
                staticMovie.setId(movie.getId());
                MovieID = movie.getId();
                SingleMovie();

//                String message = String.format("Clicked on position: %d, name: %s, %s", position, movie.getName(), movie.getYear());
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                //test
//                Movie movie = movies.get(position);
//                movies.clear();
//                movies.add(movie);
//                adapter.notifyDataSetChanged();

            }
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent searchActivity = new Intent(ListViewActivity.this, search.class);
//                startActivity(searchActivity);
                prevPage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPage();

            }
        });

    }

    public void searchTitle() {

        // Use the same network queue across our application
        //Toast.makeText(getApplicationContext(), "start function", Toast.LENGTH_SHORT).show();
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        //request type is POST
        final StringRequest searchRequest = new StringRequest(Request.Method.GET, url+"movie?mode=1&sort=1&num=20&title=" + content, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "start onRespons", Toast.LENGTH_SHORT).show();
                //TODO should parse the json response to redirect to appropriate functions.
                Log.d("search.success", response);
                String temp_response = response;

                if(response != null)
                {
                    //parse json object
                    JSONObject jObject = null;
                    String status = null;
                    String errormessage = null;
                    try {
                        JSONArray jsonarray = new JSONArray(temp_response);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String name = jsonobject.getString("movie_title");
                            String year = jsonobject.getString("movie_year");
                            String director = jsonobject.getString("movie_director");
                            String gen1 = jsonobject.getString("gen1");
                            String gen2 = jsonobject.getString("gen2");
                            String gen3 = jsonobject.getString("gen3");
                            String star1 = jsonobject.getString("star1");
                            String star2 = jsonobject.getString("star2");
                            String star3 = jsonobject.getString("star3");
                            String id = jsonobject.getString("movie_id");
                            String star = star1;
                            String genres = gen1;
                            String rating = jsonobject.getString("movie_rating");

                            //start edit star and genres
                            if(!gen2.equals(""))
                                genres += "," + gen2;
                            if(!gen3.equals(""))
                                genres += "," + gen3;
                            if(!star2.equals(""))
                                star += star2;
                            if(!star3.equals(""))
                                star += star3;

                            //add to list
                            movies.add(new Movie(name, year,director,genres,star,id,rating));

                        }
                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


                }
                else
                    Toast.makeText(getApplicationContext(), "is null", Toast.LENGTH_SHORT).show();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("search.error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<>();
//                params.put("title", "the");
//                params.put("mobile", "yes");
                params.put("modes", "1");
//                params.put("sort", "1");
//                params.put("num", "20");
                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent

        queue.add(searchRequest);

    }

    public void prevPage() {

        // Use the same network queue across our application
        //Toast.makeText(getApplicationContext(), "start function", Toast.LENGTH_SHORT).show();
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        //request type is POST
        final StringRequest prevRequest = new StringRequest(Request.Method.GET, url+"movie?mode=3&page=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "start onRespons", Toast.LENGTH_SHORT).show();
                //TODO should parse the json response to redirect to appropriate functions.
                Log.d("prev.success", response);
                String temp_response = response;
                movies.clear();
                if(response != null)
                {
                    //parse json object
                    JSONObject jObject = null;
                    String status = null;
                    String errormessage = null;
                    try {
                        JSONArray jsonarray = new JSONArray(temp_response);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String name = jsonobject.getString("movie_title");
                            String year = jsonobject.getString("movie_year");
                            String director = jsonobject.getString("movie_director");
                            String gen1 = jsonobject.getString("gen1");
                            String gen2 = jsonobject.getString("gen2");
                            String gen3 = jsonobject.getString("gen3");
                            String star1 = jsonobject.getString("star1");
                            String star2 = jsonobject.getString("star2");
                            String star3 = jsonobject.getString("star3");
                            String id = jsonobject.getString("movie_id");
                            String star = star1;
                            String genres = gen1;
                            String rating = jsonobject.getString("movie_rating");

                            //start edit star and genres
                            if(!gen2.equals(""))
                                genres += "," + gen2;
                            if(!gen3.equals(""))
                                genres += "," + gen3;
                            if(!star2.equals(""))
                                star += star2;
                            if(!star3.equals(""))
                                star += star3;

                            //add to list
                            movies.add(new Movie(name, year,director,genres,star,id,rating));

                        }
                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


                }
                else
                    Toast.makeText(getApplicationContext(), "is null", Toast.LENGTH_SHORT).show();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("prev.error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<>();
//                params.put("title", "the");
//                params.put("mobile", "yes");
                params.put("modes", "1");
//                params.put("sort", "1");
//                params.put("num", "20");
                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent

        queue.add(prevRequest);

    }

    public void nextPage() {

        // Use the same network queue across our application
        //Toast.makeText(getApplicationContext(), "start function", Toast.LENGTH_SHORT).show();
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        //request type is POST
        final StringRequest nextRequest = new StringRequest(Request.Method.GET, url+"movie?mode=3&page=2", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "start onRespons", Toast.LENGTH_SHORT).show();
                //TODO should parse the json response to redirect to appropriate functions.
                Log.d("next.success", response);
                String temp_response = response;
                movies.clear();
                if(response != null)
                {
                    //parse json object
                    JSONObject jObject = null;
                    String status = null;
                    String errormessage = null;
                    try {
                        JSONArray jsonarray = new JSONArray(temp_response);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String name = jsonobject.getString("movie_title");
                            String year = jsonobject.getString("movie_year");
                            String director = jsonobject.getString("movie_director");
                            String gen1 = jsonobject.getString("gen1");
                            String gen2 = jsonobject.getString("gen2");
                            String gen3 = jsonobject.getString("gen3");
                            String star1 = jsonobject.getString("star1");
                            String star2 = jsonobject.getString("star2");
                            String star3 = jsonobject.getString("star3");
                            String id = jsonobject.getString("movie_id");
                            String star = star1;
                            String genres = gen1;
                            String rating = jsonobject.getString("movie_rating");

                            //start edit star and genres
                            if(!gen2.equals(""))
                                genres += "," + gen2;
                            if(!gen3.equals(""))
                                genres += "," + gen3;
                            if(!star2.equals(""))
                                star += star2;
                            if(!star3.equals(""))
                                star += star3;

                            //add to list
                            movies.add(new Movie(name, year,director,genres,star,id,rating));

                        }

                        adapter.notifyDataSetChanged();
                        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


                }
                else
                    Toast.makeText(getApplicationContext(), "is null", Toast.LENGTH_SHORT).show();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("next.error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<>();
//                params.put("title", "the");
//                params.put("mobile", "yes");
                params.put("modes", "1");
//                params.put("sort", "1");
//                params.put("num", "20");
                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent

        queue.add(nextRequest);

    }

    public void SingleMovie() {

        // Use the same network queue across our application
        //Toast.makeText(getApplicationContext(), "start function", Toast.LENGTH_SHORT).show();
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        //request type is POST
        final StringRequest searchMRequest = new StringRequest(Request.Method.GET, url2+"single-movie?id=" + MovieID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "start onRespons", Toast.LENGTH_SHORT).show();
                //TODO should parse the json response to redirect to appropriate functions.
                String temp_response = response;
                staticMovie.setRes(temp_response);
                Intent singleMovie = new Intent(ListViewActivity.this, singlemovie.class);
                startActivity(singleMovie);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("searchM.error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<>();
//                params.put("title", "the");
//                params.put("mobile", "yes");
                params.put("modes", "1");
//                params.put("sort", "1");
//                params.put("num", "20");
                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(searchMRequest);
    }
}