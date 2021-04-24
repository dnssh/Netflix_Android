package com.example.devapp;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.devapp.ui.home.HomeViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Details extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    private ImageView iv1;
    private TextView tv1,tv3,tv5,tv7;
    private RequestQueue mQueue;
    private static JSONArray jsonArray;

    public String[] urls,names;
    String id,media,poster;


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
        getcast(id,media);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark();
            }
        });


    }


    public void getDataV(String id, String media) {
//        String url="https://api.themoviedb.org/3/movie/299534?api_key=d2494ce0da2dfa43a10b12b5456f65d2&language=enUS&page=1";
        String url="https://api.themoviedb.org/3/"+media+"/"+id+"?api_key=d2494ce0da2dfa43a10b12b5456f65d2&language=enUS&page=1";
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
                poster="https://image.tmdb.org/t/p/w780/"+response.getString("poster_path");
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

                //Log.d("RespArray",jsonArray.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }

    public void getcast(String id, String media) {
        String url="https://api.themoviedb.org/3/"+media+"/"+id+"/credits?api_key=d2494ce0da2dfa43a10b12b5456f65d2&language=en-US&page=1";
        urls = new String[6];
        names= new String[6];
        //String url="https://api.themoviedb.org/3/movie/299534?api_key=d2494ce0da2dfa43a10b12b5456f65d2&language=enUS&page=1";
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

                JSONArray casts= response.getJSONArray("cast");
                Log.d("inside", casts.toString(4));
                urls[0]="https://image.tmdb.org/t/p/w780/"+casts.getJSONObject(0).getString("profile_path");
                Log.d("url0",urls[0]);
                urls[1]="https://image.tmdb.org/t/p/w780/"+casts.getJSONObject(1).getString("profile_path");
                urls[2]="https://image.tmdb.org/t/p/w780/"+casts.getJSONObject(2).getString("profile_path");
                urls[3]="https://image.tmdb.org/t/p/w780/"+casts.getJSONObject(3).getString("profile_path");
                urls[4]="https://image.tmdb.org/t/p/w780/"+casts.getJSONObject(4).getString("profile_path");
                urls[5]="https://image.tmdb.org/t/p/w780/"+casts.getJSONObject(5).getString("profile_path");

                ImageView iv2= (ImageView) findViewById(R.id.imageView2);
                ImageView iv3= (ImageView) findViewById(R.id.imageView3);
                ImageView iv4= (ImageView) findViewById(R.id.imageView4);
                ImageView iv5= (ImageView) findViewById(R.id.imageView5);
                ImageView iv6= (ImageView) findViewById(R.id.imageView6);
                ImageView iv7= (ImageView) findViewById(R.id.imageView7);

                Picasso.with(iv2.getContext()).load(urls[0]).into(iv2);
                Picasso.with(iv3.getContext()).load(urls[1]).into(iv3);
                Picasso.with(iv4.getContext()).load(urls[2]).into(iv4);
                Picasso.with(iv5.getContext()).load(urls[3]).into(iv5);
                Picasso.with(iv6.getContext()).load(urls[4]).into(iv6);
                Picasso.with(iv7.getContext()).load(urls[5]).into(iv7);

                names[0]=casts.getJSONObject(0).getString("name");
                names[1]=casts.getJSONObject(1).getString("name");
                names[2]=casts.getJSONObject(2).getString("name");
                names[3]=casts.getJSONObject(3).getString("name");
                names[4]=casts.getJSONObject(4).getString("name");
                names[5]=casts.getJSONObject(5).getString("name");

                TextView tv11=(TextView) findViewById(R.id.textView11);
                TextView tv12=(TextView) findViewById(R.id.textView12);
                TextView tv13=(TextView) findViewById(R.id.textView13);
                TextView tv21=(TextView) findViewById(R.id.textView21);
                TextView tv22=(TextView) findViewById(R.id.textView22);
                TextView tv23=(TextView) findViewById(R.id.textView23);

                tv11.setText(names[0]);
                tv12.setText(names[1]);
                tv13.setText(names[2]);
                tv21.setText(names[3]);
                tv22.setText(names[4]);
                tv23.setText(names[5]);

                Log.d("urls", String.valueOf(urls));
                Log.d("names", String.valueOf(names[0]));
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

        List<String> list=new ArrayList<String>();
        list.add(media.substring(0,1));
        list.add(poster);
        editor.putString(id, String.valueOf(list));
        editor.commit();
        Log.d("stored",id+":"+String.valueOf(list));

    }




}