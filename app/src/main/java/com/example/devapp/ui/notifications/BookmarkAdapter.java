package com.example.devapp.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devapp.Details;
import com.example.devapp.Movie;
import com.example.devapp.R;
import com.example.devapp.ui.dashboard.SearchAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {


    private Context context;
    //private String[] items;
    private String[] ids;
    private String mediatype;
    private List<String> items;
    private Map<String,?> keys;
    private List<ArrayList<String>> watchlist;

    public BookmarkAdapter(List watchlist,Context context) {
        this.watchlist=watchlist;
        this.context = context;
    }

    @NonNull
    @Override
    public BookmarkAdapter.BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.bookmark_card,parent,false);
        return new BookmarkAdapter.BookmarkViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.BookmarkViewHolder holder, int position) {
        String id=watchlist.get(position).get(0);
        String typ=watchlist.get(position).get(1);
        //int lgt=watchlist.get(position).get(2).length();
        //String imgurl=watchlist.get(position).get(2).substring(0,lgt-1);
        String imgurl=watchlist.get(position).get(2).trim();
        Log.d("image url",imgurl);

        holder.tv1.setText(typ);
        Picasso.with(context).load(imgurl).into(holder.iv);


        holder.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = context.getSharedPreferences("bookmarks", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove(id);
                editor.commit();
                Log.d("Removed",id);

            }
        });



    }

    @Override
    public int getItemCount() {
        return watchlist.size();
    }


    public class BookmarkViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv1;
        ImageButton ib;
        String id, typ, imgurl;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.cardimage);
            tv1=(TextView) itemView.findViewById(R.id.type);
            ib=(ImageButton) itemView.findViewById(R.id.minuswatch);
        }

    }
}
