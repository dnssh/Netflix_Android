package com.example.devapp.ui.notifications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devapp.Movie;
import com.example.devapp.R;
import com.example.devapp.ui.dashboard.SearchAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private static String TAG = "Bookmarks";
    private List<Movie> items;

    //List<ArrayList<String>> watchlist = new ArrayList<ArrayList<String>>();
    List<ArrayList<String>> watchlist;
    ArrayList<String> item;
    //ArrayList<String> item = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);

        parsedata();

        //ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.watchlist,R.id.textView,StringArray);


        RecyclerView list=(RecyclerView) root.findViewById(R.id.watchlist);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        list.setLayoutManager(gridLayoutManager);


        //GridView list= (GridView) root.findViewById(R.id.watchlist);
        //list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        Log.d("finalList", String.valueOf(watchlist.size()));
        list.setAdapter(new BookmarkAdapter(watchlist,getContext()));



        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });



        //watchlist = new ArrayList<>();
        //item=new ArrayList<>();


//        SharedPreferences pref = getContext().getSharedPreferences("bookmarks", 0);
//        //Map<String,?> keys = pref.getAll();
//        Map<String,?> keys = pref.getAll();
//        for(Map.Entry<String,?> entry : keys.entrySet()) {
//            Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
//            //arr=entry.getValue();
//            item=new ArrayList<>();
//            item.add(entry.getKey());
//            //List<String>=(List<String>) entry.getValue()
//            String typ=((String)entry.getValue()).substring(1,2);
//            if(typ.equals("m")){
//                item.add("movie");
//            }
//            else{
//                item.add("tv");
//            }
//            item.add(((String)entry.getValue()).substring(3,-1));
//            Log.d("main data", String.valueOf(item));
//
//            watchlist.add(item);

            //String value = item.add(entry.getValue().toString());
            //String[] strArr = value

//            try {
//
//                items.add(Movie.toNewsArticle(entry.getValue().toString()));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        //}
//        if(checkEmptyList()) {
//            return root;
//        }
//        displayArticles(root);
        return root;
    }

    private void parsedata(){
        watchlist = new ArrayList<>();
        SharedPreferences pref = getContext().getSharedPreferences("bookmarks", 0);
        //Map<String,?> keys = pref.getAll();
        Map<String,?> keys = pref.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            //arr=entry.getValue();
            item = new ArrayList<>();
            item.add(entry.getKey());
            //List<String>=(List<String>) entry.getValue()
            String typ = ((String) entry.getValue()).substring(1, 2);
            if (typ.equals("m")) {
                item.add("movie");
            } else {
                item.add("tv");
            }
            int lgt=((String) entry.getValue()).length();
            item.add(((String) entry.getValue()).substring(3,lgt-1));
            Log.d("main data", String.valueOf(item));

            watchlist.add(item);
        }
    }


    //view =  inflater.inflate(R.layout.fragment_bookmarks, container, false);

}