package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class search extends ActionBarActivity {
    private String url;
    private EditText content;
    private Button searchbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //url = "https://10.0.2.2:8443/cs122b_spring20_project3_war/api/";
        url = "https://ec2-3-136-17-82.us-east-2.compute.amazonaws.com:8443/cs122b-spring20-project4/api/";
        content = findViewById(R.id.search);
        searchbutton = findViewById(R.id.search_button);
        //init search content
        searchContent.setContent("");
        staticMovie.setId("");

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_content = content.getText().toString();


                if(search_content != null)
                    if(!search_content.equals(""))
                    {
                        //set global content
                        searchContent.setContent(search_content);
                        //search and jump to movie list
                        searchTitle();

                        String message = "You are searching " + search_content;
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Please enter the movie title", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Please enter the movie title", Toast.LENGTH_SHORT).show();
            }
        });

        content.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // start search
                    String search_content = content.getText().toString();


                    if(search_content != null)
                        if(!search_content.equals(""))
                        {
                            //set global content
                            searchContent.setContent(search_content);
                            //search and jump to movie list
                            searchTitle();

                            String message = "You are searching " + search_content;
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Please enter the movie title", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Please enter the movie title", Toast.LENGTH_SHORT).show();

                    return true;
                }
                return false;
            }
        });
    }

    public void searchTitle() {

        // Use the same network queue across our application
        //Toast.makeText(getApplicationContext(), "start function", Toast.LENGTH_SHORT).show();
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        //request type is POST
        final StringRequest searchRequest = new StringRequest(Request.Method.GET, url+"movie?mode=1&sort=1&num=20&title=" + content.getText().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "start onRespons", Toast.LENGTH_SHORT).show();
                //TODO should parse the json response to redirect to appropriate functions.
                Log.d("search.success", response);
                String temp_response = response;

                staticMovie.setRes(temp_response);
                Intent listPage = new Intent(search.this, ListViewActivity.class);
                startActivity(listPage);

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
}
