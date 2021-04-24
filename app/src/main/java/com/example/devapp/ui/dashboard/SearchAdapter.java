package com.example.devapp.ui.dashboard;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devapp.Movie;
import com.example.devapp.R;
import com.example.devapp.ui.home.HorizontalAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final Context context;
    //private String[] items;
    private String[] ids;
    private String mediatype;
    private List<Movie> items;

//    public SearchAdapter(String[] ids, String[] items, Context context) {
//        this.ids=ids;
//        this.items = items;
//        this.context=context;
//    }

    public SearchAdapter(List items, Context context) {
        //this.ids=ids;
        this.items = items;
        this.context=context;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.search_card,parent,false);
        return new SearchAdapter.SearchViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {

        //holder.tv2.setText(items[position]);
        Movie item = items.get(position);

        holder.tv1.setText(item.id);
        holder.tv2.setText(item.title);
        Picasso.with(context).load(item.imgurl).into(holder.iv);

    }

    @Override
    public int getItemCount() {
        //return items.length;
        return items.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv1,tv2,tv3;
        SearchView sv;
        String id, type, title, rating, date, imgurl;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.cardimage);
            tv1=(TextView) itemView.findViewById(R.id.year);
            tv2=(TextView) itemView.findViewById(R.id.rating);
            tv3=(TextView) itemView.findViewById(R.id.title);
            //et=(SearchView) itemView.findViewById(R.id.search_view);
        }

    }
}
