package com.example.devapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
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
    String id,media,poster,title;
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
        ImageView ib=(ImageView) findViewById(R.id.addwatch);
        ImageView ir=(ImageView) findViewById(R.id.removewatch);
        ImageView fb=(ImageView) findViewById(R.id.fb);
        ImageView twt=(ImageView) findViewById(R.id.twitter);

        id= getIntent().getStringExtra("id");
        media=getIntent().getStringExtra("media");

        ProgressBar spinner;
        TextView ft;
        spinner = findViewById(R.id.progressBar1);
        ft=findViewById(R.id.fetching_text);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinner.setVisibility(View.GONE);
                ft.setVisibility(View.GONE);
            }
        }, 5000);


//        SharedPreferences pref = getApplicationContext().getSharedPreferences("bookmarks", 0);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.remove("wl");
//        editor.commit();

        //dummyadd();
        checkBookmark();
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

        ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeBookmark();
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url2 = "https://facebook.com/sharer/sharer.php?u=https://www.themoviedb.org/"+media+"/"+id;
                Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
                i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i2.setPackage("com.android.chrome");
                try {
                    getBaseContext().startActivity(i2);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getBaseContext(), "unable to open chrome", Toast.LENGTH_SHORT).show();
                    i2.setPackage(null);
                    getBaseContext().startActivity(i2);
                }
            }
        });

        twt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url3 = "https://twitter.com/intent/tweet?text=https://www.themoviedb.org/"+media+"/"+id;
                Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url3));
                i3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i3.setPackage("com.android.chrome");
                try {
                    getBaseContext().startActivity(i3);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getBaseContext(), "unable to open chrome", Toast.LENGTH_SHORT).show();
                    i3.setPackage(null);
                    getBaseContext().startActivity(i3);
                }
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
                this.poster="https://image.tmdb.org/t/p/w780/"+response.getString("backdrop_path");
                String overview,year;

                if(media.equals("movie")) {
                    title = response.getString("title");
                    year=response.getString("release_date");
                }
                else{
                    title = response.getString("name");
                    year=response.getString("first_air_date");
                }

                overview = response.getString("overview");
                String gstr="";
                jsonArray = new JSONArray();
                jsonArray=response.getJSONArray("genres");
                for(int i = 0; i < jsonArray.length(); i++) {
                    gstr+=jsonArray.getJSONObject(i).getString("name")+", ";
               }
                if(jsonArray.length()==0){
                    TextView tv4=(TextView) findViewById(R.id.textView4);
                    tv4.setVisibility(View.INVISIBLE);
                }
                ImageView iv1 = (ImageView) findViewById(R.id.imageView);
                TextView tv1=(TextView) findViewById(R.id.textView);
                TextView tv3=(TextView) findViewById(R.id.textView3);
                TextView tv5=(TextView) findViewById(R.id.textView5);
                TextView tv7=(TextView) findViewById(R.id.textView7);
                tv1.setText(title);
                tv3.setText(overview);
                tv5.setText(gstr);
                tv7.setText(year.substring(0,4));

                if(year.isEmpty()){
                    TextView tv6=(TextView) findViewById(R.id.textView6);
                    tv6.setVisibility(View.INVISIBLE);
                }

                if(overview.isEmpty()){
                    TextView tv2=(TextView) findViewById(R.id.textView2);
                    tv2.setVisibility(View.INVISIBLE);
                }

                Log.d("genres",gstr);
                Log.d("poster",poster);
                //Picasso.with(getApplicationContext()).load(poster).into(iv1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }


    private boolean checkBookmark(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("bookmarks", 0);
        String wtchlst=pref.getString("wl","");
        String chk=media.substring(0,1)+id;

        Boolean present=wtchlst.contains(chk);

        ImageView ib=(ImageView) findViewById(R.id.addwatch);
        ImageView ir=(ImageView) findViewById(R.id.removewatch);

        Log.d("check", String.valueOf(present));
        if(present==true){
            ib.setVisibility(View.INVISIBLE);
            ir.setVisibility(View.VISIBLE);
        }
        else{
            ib.setVisibility(View.VISIBLE);
            ir.setVisibility(View.INVISIBLE);
        }
        return present;
    }

    private void dummyadd(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("bookmarks", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("wl", "m399566");
    }

    private void addBookmark(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("bookmarks", 0);
        SharedPreferences.Editor editor = pref.edit();
        String newitem=media.substring(0,1)+id+",";
        String wtchlst= pref.getString("wl","");
        wtchlst=newitem+wtchlst;

        if(!checkBookmark()) {
            editor.putString("wl", wtchlst);
            //editor.putString("wl", "m399566");
            editor.commit();
            Log.d("stored", wtchlst);
            Toast.makeText(getApplicationContext(), title +" was added to Watchlist", Toast.LENGTH_SHORT).show();
            checkBookmark();
        }
    }

    private void removeBookmark(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("bookmarks", 0);
        SharedPreferences.Editor editor = pref.edit();
        String wtchlst= pref.getString("wl",null);
        String rmv=media.substring(0,1)+id+",";

        Log.d("remove logic",rmv);

        wtchlst=wtchlst.replace(rmv,"");

//        List<String> allitems = Arrays.asList(wtchlst.split(","));
//        Log.d("List Format:", String.valueOf(allitems));
//
//        allitems.remove(rmv);
//        wtchlst= TextUtils.join(",",allitems);
        Log.d("Modified sp string",wtchlst);
        editor.putString("wl", wtchlst);
        editor.commit();
        ImageView ib=(ImageView) findViewById(R.id.addwatch);
        ImageView ir=(ImageView) findViewById(R.id.removewatch);
        ib.setVisibility(View.VISIBLE);
        ir.setVisibility(View.INVISIBLE);
        //checkBookmark();

        Toast.makeText(getApplicationContext(), title+" was removed from Watchlist", Toast.LENGTH_SHORT).show();
        Log.d("Removed",id);
    }

    private void addBookmarkOld(){
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
            Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();

            ImageView ib=(ImageView) findViewById(R.id.addwatch);
            ImageView ir=(ImageView) findViewById(R.id.removewatch);

                 ir.setVisibility(View.VISIBLE);

        }
    }

    private void removeBookmarkOld(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("bookmarks", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(id);
        editor.commit();
        ImageView ib=(ImageView) findViewById(R.id.addwatch);
        ImageView ir=(ImageView) findViewById(R.id.removewatch);

        ib.setVisibility(View.VISIBLE);
        ir.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "Removed from watchlist", Toast.LENGTH_SHORT).show();
        Log.d("Removed",id);
    }

    private boolean checkBookmarkOld(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("bookmarks", 0);
        boolean present=pref.contains(id);

        ImageView ib=(ImageView) findViewById(R.id.addwatch);
        ImageView ir=(ImageView) findViewById(R.id.removewatch);

        //Log.d("check", String.valueOf(present));
        if(present==true){
            ib.setVisibility(View.INVISIBLE);
            ir.setVisibility(View.VISIBLE);
        }
        else{
            ib.setVisibility(View.VISIBLE);
            ir.setVisibility(View.INVISIBLE);
        }
        return present;

    }


    public void getVideo(String id, String media){
        String url=baseurl+"/"+media+"video?id="+id;

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

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
                    youTubePlayer.cueVideo(videoId, 0);
                    //youTubePlayer.loadVideo(videoId, 0);
                }
            });
            //clist.setAdapter(new CastAdapter(response,getApplicationContext()));
            //Log.d("RespArray",response.toString(4));
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }


    public void getVideo3(String id, String media){
        String url=baseurl+"/"+media+"video?id="+id;

        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonRequest=new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            String vid="";
            try {
                vid = response.getString("key");
            }catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("VideoId",vid+" @#23456");
            if(!vid.isEmpty()){
                Log.d("VideoId",vid);
                YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
                getLifecycle().addObserver(youTubePlayerView);
                String finalVideoId = vid;
                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(finalVideoId, 0);
                    }
                });
            }else{
//            catch (JSONException e) {
                Log.d("Inside catch",this.poster);
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                Picasso.with(getApplicationContext()).load(this.poster).into(iv);
                Toast.makeText(getApplicationContext(), "Showing Image" + poster, Toast.LENGTH_SHORT).show();
                YouTubePlayerView yt = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
                yt.setVisibility(View.INVISIBLE);
            }
//                e.printStackTrace();
//            }

        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }

    public void getVideo2(String id, String media){
        String url=baseurl+"/"+media+"video?id="+id;

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonRequest=new JsonObjectRequest(Request.Method.GET, url, null, response -> {

            try {
                String videoId = response.getString("key");
                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(videoId, 0);
                    }
                });
            } catch (JSONException e) {
                Log.d("Inside catch", String.valueOf(e));
                ImageView iv =(ImageView) findViewById(R.id.imageView);
                Picasso.with(getApplicationContext()).load(poster).into(iv);
                Toast.makeText(getApplicationContext(), "Showing Image", Toast.LENGTH_SHORT).show();
                YouTubePlayerView yt=(YouTubePlayerView)findViewById(R.id.youtube_player_view);
                yt.setVisibility(View.INVISIBLE);
                e.printStackTrace();
            }
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
            if(response.length()==0){
                TextView ct=(TextView)findViewById(R.id.textView8);
                ct.setVisibility(View.INVISIBLE);
            }
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

            if(response.length()==0){
                TextView rvt=(TextView)findViewById(R.id.rvtext);
                rvt.setVisibility(View.INVISIBLE);
            }
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

            if(response==null||response.length()==0){
                TextView rvt=(TextView)findViewById(R.id.picktext);
                RecyclerView rvc=findViewById(R.id.pickslist);
                rvc.setVisibility(View.INVISIBLE);
                rvt.setVisibility(View.INVISIBLE);
            }

            plist.setAdapter(new PicksAdapter(media,response,getApplicationContext()));
            //Log.d("RespArray",response.toString(4));
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }



}