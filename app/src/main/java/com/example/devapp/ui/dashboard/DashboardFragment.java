package com.example.devapp.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public String[] title=new String[20];
    public String[] dates=new String[20];
//    List<NewsArticle> articles;
//    HomePageAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);


        getSearchResults("dev");
        RecyclerView list=(RecyclerView) root.findViewById(R.id.searchlist);
        list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        list.setAdapter(new SearchAdapter(title,dates,getContext()));


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
        Log.d("Display results : ", String.valueOf(searchResults.length()));
        RecyclerView rv = root.findViewById(R.id.searchlist);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        for (int i=0;i<20;i++){
            title[i]=searchResults.getJSONObject(i).getString("title");
            dates[i]=searchResults.getJSONObject(i).getString("release_date");
        }
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