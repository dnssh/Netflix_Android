package com.example.devapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.devapp.ui.dashboard.SearchAdapter;
import com.example.devapp.ui.home.HomeViewModel;
import com.example.devapp.ui.home.HorizontalAdapter;
import com.example.devapp.ui.notifications.BookmarkAdapter;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.devapp.Constants.baseurl;

public class Details extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    private ImageView iv1;
    private TextView tv1,tv3,tv5,tv7;
    private RequestQueue mQueue;
    private static JSONArray jsonArray;

    public String[] urls,names;
    String id,media,poster;
    List<ArrayList<String>> castlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        ImageView iv1 = (ImageView) findViewById(R.id.imageView);
        TextView tv1=(TextView) findViewById(R.id.textView);
        TextView tv3=(TextView) findViewById(R.id.textView3);
        TextView tv5=(TextView) findViewById(R.id.textView5);
        TextView tv7=(TextView) findViewById(R.id.textView7);
        ImageButton ib=(ImageButton) findViewById(R.id.addwatch);

        id= getIntent().getStringExtra("id");
        media=getIntent().getStringExtra("media");


        getDataV(id,media);
        getCast(id,media);
        getReviews(id,media);
        getPicks(id,media);
        getVideo(id,media);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark();
            }
        });

        this.getSupportActionBar().hide();

    }


    public void getDataV(String id, String media) {

        String url=baseurl+"/"+media+"details?id="+id;
        RequestQueue que = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, response -> {
            try {
                //Log.d("response", response.toString(4));
//                JSONArray respArray = response.getJSONArray("results");
//                jsonArray = new JSONArray();
//                for(int i = 0; i < 6; i++) {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("title", respArray.getJSONObject(i).getString("title"));
//                    jsonObject.put("poster_path", respArray.getJSONObject(i).getString("poster_path"));
//                    jsonArray.put(jsonObject);
//                }
                poster="https://image.tmdb.org/t/p/w780/"+response.getString("backdrop_path");
                String overview=response.getString("overview");
                String title=response.getString("title");
                String year=response.getString("release_date");
                String gstr="";
                jsonArray = new JSONArray();
                jsonArray=response.getJSONArray("genres");
                for(int i = 0; i < jsonArray.length(); i++) {
                    gstr+=jsonArray.getJSONObject(i).getString("name")+", ";
               }
                ImageView iv1 = (ImageView) findViewById(R.id.imageView);
                TextView tv1=(TextView) findViewById(R.id.textView);
                TextView tv3=(TextView) findViewById(R.id.textView3);
                TextView tv5=(TextView) findViewById(R.id.textView5);
                TextView tv7=(TextView) findViewById(R.id.textView7);
                tv1.setText(title);
                tv3.setText(overview);
                tv5.setText(year);
                tv7.setText(gstr);
                Log.d("genres",gstr);
                Log.d("poster",poster);
                Picasso.with(getApplicationContext()).load(poster).into(iv1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }

    private void addBookmark(){
        //items = new ArrayList<>();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("bookmarks", 0);
        SharedPreferences.Editor editor = pref.edit();
        //editor.clear();
        if(!checkBookmark()){
            List<String> list=new ArrayList<String>();
            list.add(media.substring(0,1));
            list.add(poster);
            editor.putString(id, String.valueOf(list));
            editor.commit();
            Log.d("stored",id+":"+String.valueOf(list));
        }
    }

    private void removeBookmark(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("bookmarks", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(id);
        editor.commit();
        Log.d("Removed",id);
    }

    private boolean checkBookmark(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("bookmarks", 0);
        boolean present=pref.contains(id);
        //Log.d("check", String.valueOf(present));
        return present;

    }


    public void getVideo(String id, String media){
        String url=baseurl+"/"+media+"video?id="+id;

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

//        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                String videoId = "S0Q4gqBUs7c";
//                youTubePlayer.loadVideo(videoId, 0);
//            }
//        });

        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonRequest=new JsonObjectRequest(Request.Method.GET, url, null, response -> {

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoId = null;
                    try {
                        videoId = response.getString("key");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    youTubePlayer.loadVideo(videoId, 0);
                }
            });
            //clist.setAdapter(new CastAdapter(response,getApplicationContext()));
            //Log.d("RespArray",response.toString(4));
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }


    public void getCast(String id, String media){
        String url=baseurl+"/"+media+"cast?id="+id;
        //Log.d("url cast",url);

        RecyclerView clist=(RecyclerView) findViewById(R.id.castlist);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        clist.setLayoutManager(gridLayoutManager);

        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonRequest=new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            clist.setAdapter(new CastAdapter(response,getApplicationContext()));
            //Log.d("RespArray",response.toString(4));
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }

    public void getReviews(String id, String media){
        String url=baseurl+"/"+media+"reviews?id="+id;
        //Log.d("url reviews",url);


        RecyclerView rlist=(RecyclerView) findViewById(R.id.reviewlist);
        rlist.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        //clist.setLayoutManager(gridLayoutManager);

        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonRequest=new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            rlist.setAdapter(new ReviewAdapter(response,getApplicationContext()));
            //Log.d("RespArray",response.toString(4));
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }


    public void getPicks(String id, String media){
        String url=baseurl+"/"+media+"recommended?id="+id;
        //Log.d("url reviews",url);

        RecyclerView plist=(RecyclerView) findViewById(R.id.pickslist);
        plist.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonRequest=new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            plist.setAdapter(new PicksAdapter(media,response,getApplicationContext()));
            //Log.d("RespArray",response.toString(4));
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }



}