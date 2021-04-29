package com.example.devapp.ui.home;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.devapp.Details;
import com.example.devapp.Movie;
import com.example.devapp.R;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.devapp.Constants.baseurl;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RequestQueue mQueue;
    private static JSONArray jsonArray;
    List<Movie> items;

    public String[] urls=new String[6];
    public String[] ids=new String[6];
    private String media,id,poster;

    RelativeLayout rel,loader;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home1);

        rel = root.findViewById(R.id.rel);
        loader = root.findViewById(R.id.loader);


        TextView tv1=(TextView) root.findViewById(R.id.movietab);
        TextView tv2=(TextView) root.findViewById(R.id.tvtab);
        TextView th3=(TextView) root.findViewById(R.id.text_home3);
        media="movie";

        ProgressBar spinner;
        TextView ft;
        spinner =  root.findViewById(R.id.progressBar1);
        ft=root.findViewById(R.id.fetching_text);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rel.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
            }
        }, 5000);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(media.equals("tv")){
                    media="movie";
                    Log.d("changed to",media);
                    getSlider(root);
                    getData1(root,media);
                    getData2(root);
                }

            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(media.equals("movie")){
                    media="tv";
                    Log.d("changed to",media);
                    getSlider(root);
                    getData1(root,media);
                    getData2(root);
                }
            }
        });

        th3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url3 = "https://www.themoviedb.org/";
                Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url3));
                i3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i3.setPackage("com.android.chrome");
                try {
                    getActivity().startActivity(i3);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "unable to open chrome", Toast.LENGTH_SHORT).show();
                    i3.setPackage(null);
                    getActivity().startActivity(i3);
                }
            }
        });


        getSlider(root);
        getData1(root,"movie");
        getData2(root);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    public void getData1(View root, String media){
        String url=baseurl+"/popular"+media;
        //Log.d("api",url);

        RequestQueue que = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonRequest=new JsonArrayRequest(Request.Method.GET, url, null, response -> {
        //JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            RecyclerView list=(RecyclerView) root.findViewById(R.id.list);
            list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
            list.setAdapter(new HorizontalAdapter(media,response,getContext()));
            //Log.d("RespArray",response.toString(4));
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }

    public void getData2(View root){
        String url=baseurl+"/toprated"+media;
        //Log.d("api",url);

        RequestQueue que = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonRequest=new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            //JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            RecyclerView list=(RecyclerView) root.findViewById(R.id.list2);
            list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
            list.setAdapter(new HorizontalAdapter(media,response,getContext()));
            //Log.d("RespArray",response.toString(4));
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
//        rel.setVisibility(View.VISIBLE);
//        loader.setVisibility(View.GONE);
    }

    public void getSlider(View root){
        String url;

        if(media.equals("movie")){
            url=baseurl+"/currentlyplaying";
        }
        else{
            url=baseurl+"/trendingtv";
        }

        //Log.d("api",url);
        RequestQueue que = Volley.newRequestQueue(getContext());
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        JsonArrayRequest jsonRequest=new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            //JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                for(int i=0;i<6;i++){
                    String lnk="https://image.tmdb.org/t/p/w780/"+response.getJSONObject(i).getString("poster_path");
                    String id=response.getJSONObject(i).getString("id");


                    sliderDataArrayList.add(new SliderData(id,media,lnk));
                }

                SliderView sliderView = root.findViewById(R.id.slider);
                SliderAdapter adapter = new SliderAdapter(getContext(), sliderDataArrayList);
                sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                sliderView.setSliderAdapter(adapter);
                sliderView.setScrollTimeInSec(3);
                sliderView.setAutoCycle(true);
                sliderView.startAutoCycle();

                //Log.d("RespArray",response.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);

    }




}