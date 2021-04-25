package com.example.devapp.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home1);



        //urls = new String[6];
        //loadData();
        getSlider(root);
        getData1(root);
        getData2(root);

//        try {
//            //popularmovies();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


//    public void getDataV(View root) {
//        //String url="https://api.themoviedb.org/3/movie/popular?api_key=d2494ce0da2dfa43a10b12b5456f65d2&language=enUS&page=1";
//        String url=baseurl+"/popular";
//
//        RequestQueue que = Volley.newRequestQueue(getContext());
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
//                null, response -> {
//            try {
//                //Log.d("response", response.toString(4));
//                JSONArray respArray = response.getJSONArray("results");
//                jsonArray = new JSONArray();
//                for(int i = 0; i < 6; i++) {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("id", respArray.getJSONObject(i).getString("id"));
//                    jsonObject.put("poster_path", respArray.getJSONObject(i).getString("poster_path"));
//                    jsonArray.put(jsonObject);
//                }
//
//                for(int i=0;i<6;i++){
//                    ids[i]=respArray.getJSONObject(i).getString("id");
//                    urls[i]="https://image.tmdb.org/t/p/w780/"+respArray.getJSONObject(i).getString("poster_path");
//                }
//
////                popularmovies();
//                RecyclerView list=(RecyclerView) root.findViewById(R.id.list);
//                list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
//                Log.d("Sending to adapter", ids[0]+ " " + ids[1] + " " + ids[2] + " " +  ids[3] + " "+ ids[4] + " "+ids[5]);
//                list.setAdapter(new HorizontalAdapter(ids,urls,getContext()));
//
//                //Log.d("RespArray",jsonArray.toString(4));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }, error -> Log.e("Error", error.toString()));
//        que.add(jsonRequest);
//        //Log.d("stored",urls[0]);
//    }

    public void getData1(View root){
        String url=baseurl+"/popularmovies";
        Log.d("api",url);

        RequestQueue que = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonRequest=new JsonArrayRequest(Request.Method.GET, url, null, response -> {
        //JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                RecyclerView list=(RecyclerView) root.findViewById(R.id.list);
                list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                list.setAdapter(new HorizontalAdapter("movie",response,getContext()));
                Log.d("RespArray",response.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }

    public void getData2(View root){
        String url=baseurl+"/topratedmovies";
        Log.d("api",url);

        RequestQueue que = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonRequest=new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            //JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                RecyclerView list=(RecyclerView) root.findViewById(R.id.list2);
                list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                list.setAdapter(new HorizontalAdapter("movie",response,getContext()));
                Log.d("RespArray",response.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
    }

    public void getSlider(View root){



        String url=baseurl+"/currentlyplaying";
        Log.d("api",url);

        RequestQueue que = Volley.newRequestQueue(getContext());
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        JsonArrayRequest jsonRequest=new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            //JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                for(int i=0;i<6;i++){
                    String lnk="https://image.tmdb.org/t/p/w780/"+response.getJSONObject(i).getString("poster_path");

                    sliderDataArrayList.add(new SliderData(lnk));
                }
//                RecyclerView list=(RecyclerView) root.findViewById(R.id.list2);
//                list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
//                list.setAdapter(new HorizontalAdapter("movie",response,getContext()));


                SliderView sliderView = root.findViewById(R.id.slider);
//                sliderDataArrayList.add(new SliderData("https://image.tmdb.org/t/p/w780//2xmx8oPlbDaxTjHsIOZvOt5L3aJ.jpg"));
//                sliderDataArrayList.add(new SliderData("https://image.tmdb.org/t/p/w780//279yOM4OQREL36B3SECnRxoB4MZ.jpg"));
//                sliderDataArrayList.add(new SliderData("https://image.tmdb.org/t/p/w780//2CAL2433ZeIihfX1Hb2139CX0pW.jpg"));

                SliderAdapter adapter = new SliderAdapter(getContext(), sliderDataArrayList);
                sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                sliderView.setSliderAdapter(adapter);
                sliderView.setScrollTimeInSec(3);
                sliderView.setAutoCycle(true);
                sliderView.startAutoCycle();

                Log.d("RespArray",response.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);


    }




}