package com.example.devapp;

import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private JSONArray jsonArray;

    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        ImageView imageview1=findViewById(R.id.imageButton1);
        ImageView imageview2=findViewById(R.id.imageButton2);
        ImageView imageview3=findViewById(R.id.imageButton3);
        ImageView imageview4=findViewById(R.id.imageButton4);
        ImageView imageview5=findViewById(R.id.imageButton5);
        ImageView imageview6=findViewById(R.id.imageButton6);
        //Log.d("jj",""+imageview);
//        mQueue= Volley.newRequestQueue(this);
//        getDataV();


        //loadData();
    }

    private void loadData(){
        String url="https://image.tmdb.org/t/p/w780/inJjDhCjfhh3RtrJWBmmDqeuSYC.jpg";
        Picasso.with(this).load(url).into(imageview);

//        Glide.with(this)
//                .load(url)
//                .placeholder(R.drawable.movie_placeholder)
//                .into(imageview);
    }

    private void getDataV() {
        String url="https://api.themoviedb.org/3/movie/popular?api_key=d2494ce0da2dfa43a10b12b5456f65d2&language=enUS&page=1";
        RequestQueue que = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, response -> {
            try {
                Log.d("here","hi");
                Log.d("response", response.toString(4));
                JSONArray respArray = response.getJSONArray("results");
                jsonArray = new JSONArray();
                for(int i = 0; i < 6; i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title", respArray.getJSONObject(i).getString("title"));
                    jsonObject.put("poster_path", respArray.getJSONObject(i).getString("poster_path"));
                    jsonArray.put(jsonObject);
                }
                Log.d("RespArray",jsonArray.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }

    private void getData(){
        RequestQueue que = Volley.newRequestQueue(getBaseContext());
        Log.d("Inside","ABC");
        String url="https://api.themoviedb.org/3/movie/popular?api_key=d2494ce0da2dfa43a10b12b5456f65d2&language=enUS&page=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray=response.getJSONArray("results");
                            Log.d("here", "Hi");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject item=jsonArray.getJSONObject(i);
                                String pth=item.getString("poster_path");
                                System.out.println(pth);
                                Log.d("PosterPath",pth);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

}