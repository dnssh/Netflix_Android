package com.example.devapp.ui.home;

import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.devapp.Details;
import com.example.devapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ImageView imageview1,imageview2,imageview3,imageview4,imageview5,imageview6;
    private RequestQueue mQueue;
    private static JSONArray jsonArray;

    public String[] urls=new String[6];

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);


        imageview1=root.findViewById(R.id.imageButton1);
        imageview2=root.findViewById(R.id.imageButton2);
        imageview3=root.findViewById(R.id.imageButton3);
        imageview4=root.findViewById(R.id.imageButton4);
        imageview5=root.findViewById(R.id.imageButton5);
        imageview6=root.findViewById(R.id.imageButton6);

        Button btn9;

        //urls = new String[6];

        //loadData();
        getDataV();
        //Log.d("array here", String.valueOf(jsonArray));
        Log.d("ohhoh", String.valueOf(urls[0]));

        RecyclerView list=(RecyclerView) root.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        list.setAdapter(new HorizontalAdapter(urls,getContext()));

//        try {
//
//            //popularmovies();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        btn9= (Button)root.findViewById(R.id.btn9);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id="299534";
                String media="movie";
                Intent intent=new Intent(getActivity(), Details.class);
                intent.putExtra("id",id);
                intent.putExtra("media",media);

                startActivity(intent);
            }
        });


        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    public void getDataV() {
        String url="https://api.themoviedb.org/3/movie/popular?api_key=d2494ce0da2dfa43a10b12b5456f65d2&language=enUS&page=1";
        RequestQueue que = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, response -> {
            try {
                //Log.d("response", response.toString(4));
                JSONArray respArray = response.getJSONArray("results");
                jsonArray = new JSONArray();
                for(int i = 0; i < 6; i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", respArray.getJSONObject(i).getString("id"));
                    jsonObject.put("poster_path", respArray.getJSONObject(i).getString("poster_path"));
                    jsonArray.put(jsonObject);
                }
                urls[0]="https://image.tmdb.org/t/p/w780/"+respArray.getJSONObject(0).getString("poster_path");
                urls[1]="https://image.tmdb.org/t/p/w780/"+respArray.getJSONObject(1).getString("poster_path");
                urls[2]="https://image.tmdb.org/t/p/w780/"+respArray.getJSONObject(2).getString("poster_path");
                urls[3]="https://image.tmdb.org/t/p/w780/"+respArray.getJSONObject(3).getString("poster_path");
                urls[4]="https://image.tmdb.org/t/p/w780/"+respArray.getJSONObject(4).getString("poster_path");
                urls[5]="https://image.tmdb.org/t/p/w780/"+respArray.getJSONObject(5).getString("poster_path");
                popularmovies();


                //Log.d("RespArray",jsonArray.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);
        //Log.d("stored",urls[0]);
    }


    public void popularmovies() throws JSONException {
        //Log.d("array", String.valueOf(jsonArray.getJSONObject(0)));
//        String url1="https://image.tmdb.org/t/p/w780/"+jsonArray.getJSONObject(0).getString("poster_path");
//        String url2="https://image.tmdb.org/t/p/w780/"+jsonArray.getJSONObject(1).getString("poster_path");

        Picasso.with(imageview1.getContext()).load(urls[0]).into(imageview1);
        Picasso.with(imageview2.getContext()).load(urls[1]).into(imageview2);
        Picasso.with(imageview3.getContext()).load(urls[2]).into(imageview3);
        Picasso.with(imageview4.getContext()).load(urls[3]).into(imageview4);
        Picasso.with(imageview5.getContext()).load(urls[4]).into(imageview5);
        Picasso.with(imageview6.getContext()).load(urls[5]).into(imageview6);
    }

    private void loadData(){
        String url="https://image.tmdb.org/t/p/w780/inJjDhCjfhh3RtrJWBmmDqeuSYC.jpg";
        Picasso.with(imageview1.getContext()).load(url).into(imageview1);
    }

}