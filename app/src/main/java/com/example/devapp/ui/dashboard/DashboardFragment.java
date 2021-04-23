package com.example.devapp.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.example.devapp.Movie;
import com.example.devapp.R;
import com.example.devapp.ui.home.HorizontalAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private static String TAG = "search", BOOKMARK = "bookmarks", KEYWORD = "query";
    boolean loading = false;
    private static JSONArray searchResults;
    private View root;

    public String[] titles=new String[20];
    public String[] dates=new String[20];
    private String keyword="";

    public String id,type,title,date,rating,imgurl;
    ArrayList<Movie> items =new ArrayList<>();
//    List<NewsArticle> articles;
//    HomePageAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);

        Log.d("finalList11", String.valueOf(items.size()));


        //getSearchResults("Avenger");
        RecyclerView list=(RecyclerView) root.findViewById(R.id.searchlist);
        list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //list.setAdapter(new SearchAdapter(titles,dates,getContext()));
        Log.d("finalList", String.valueOf(items.size()));
        list.setAdapter(new SearchAdapter(items,getContext()));


        EditText myTextBox = root.findViewById(R.id.search_view);
        myTextBox.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                keyword=myTextBox.getText().toString();
                Log.d("keyword",keyword);
                getSearchResults(keyword);
                //TextView myOutputBox = (TextView) findViewById(R.id.myOutputBox);
                //myOutputBox.setText(s);
            }
        });



        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    private void displayResults(JSONArray searchResults){
        Log.d("finally here",String.valueOf(searchResults.length()));
    }

//    private void displayResults(){
//        RecyclerView rv = findViewById(R.id.search_results);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        rv.setLayoutManager(llm);
//        articles = new ArrayList<>();
//        if(searchResults.length() == 0){
//            findViewById(R.id.no_results).setVisibility(View.VISIBLE);
//            return ;
//        }
//        for(int i=0; i < searchResults.length(); i++){
//            try {
//                JSONObject article = searchResults.getJSONObject(i);
//                String title = article.getString("webTitle");
//                String articleId = article.getString("id");
//                String section = article.getString("sectionName");
//                String imageUrl;
//                try{
//                    imageUrl = article.getJSONObject("blocks").getJSONObject("main")
//                            .getJSONArray("elements").getJSONObject(0)
//                            .getJSONArray("assets").getJSONObject(0)
//                            .getString("file");
//                }
//                catch (Exception e){
//                    imageUrl = Constants.DEFAULT_IMAGE;
//                }
//                String webUrl = article.getString("webUrl");
//                ZonedDateTime publicationDate = ZonedDateTime.
//                        parse(article.getString("webPublicationDate")).
//                        withZoneSameLocal(ZoneId.of("GMT"));
//                articles.add(new NewsArticle(title,section, imageUrl,
//                        articleId, publicationDate, webUrl));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        adapter = new HomePageAdapter(articles, this);
//        rv.setAdapter(adapter);
//        adapter.setClickListener(this);
//    }
//
    private  void displayResults() throws JSONException {
        Log.d("Display size : ", String.valueOf(searchResults.length()));
        RecyclerView rv = root.findViewById(R.id.searchlist);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        //items = new ArrayList<>();
        items.clear();

        for (int i=0;i<searchResults.length();i++){
            id=searchResults.getJSONObject(i).getString("id");

            imgurl="https://image.tmdb.org/t/p/w780/"+searchResults.getJSONObject(i).getString("backdrop_path");
            if(imgurl==null){imgurl="zxy";}
            rating=searchResults.getJSONObject(i).getString("vote_average");

            type=searchResults.getJSONObject(i).getString("media_type");

            if(type.equals("movie")){
                title=searchResults.getJSONObject(i).getString("title");
                date=searchResults.getJSONObject(i).getString("release_date");
            }
            else{
                title=searchResults.getJSONObject(i).getString("name");
                date=searchResults.getJSONObject(i).getString("first_air_date");
            }


//            title=searchResults.getJSONObject(i).getString("title");
//            if(title=="" || title == null){title=searchResults.getJSONObject(i).getString("name");}
//
//            date=searchResults.getJSONObject(i).getString("release_date");
//            if(date==null){date=searchResults.getJSONObject(i).getString("first_air_date");}


            Log.d("ids", " id:"+id+" media:"+type+" title:"+title+" imgurl:"+imgurl+" date:"+date+" rating:"+rating);

            Movie mv=new Movie(id,type,imgurl,title,date,rating);
            mv.toMyString();
            items.add(mv);
        }

        Log.d("list length", String.valueOf(items.size()));
        //Log.d("List",items.toString());

//        for (int i=0;i<Math.min(searchResults.length(),20);i++){
//            titles[i]=searchResults.getJSONObject(i).getString("title");
//            dates[i]=searchResults.getJSONObject(i).getString("release_date");
//        }
//        if(searchResults.length() == 0){
//            root.findViewById(R.id.no_results).setVisibility(View.VISIBLE);
//            return ;
//        }
    }
    private void getSearchResults(final String query) {
        String url = "https://angulardev-309412.ue.r.appspot.com/search?query="+query;
        RequestQueue que = Volley.newRequestQueue(getContext());
        //JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, response -> {
//            try {
                searchResults = response;
            try {
                displayResults();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//                searchResults = response.getJSONObject("response").getJSONArray("results");
                //loading = true;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }, error -> Log.e(TAG, error.toString()));
        que.add(jsonRequest);
    }



}