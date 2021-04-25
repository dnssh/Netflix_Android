package com.example.devapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.devapp.ui.home.SliderAdapter;
import com.example.devapp.ui.home.SliderData;
import com.example.devapp.ui.notifications.BookmarkAdapter;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder>  {

    private JSONArray items;
    private Context context;


    // Constructor
    public CastAdapter(JSONArray items,Context context) {
        this.items = items;
        this.context=context;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.cast_card,parent,false);
        return new CastAdapter.CastViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastViewHolder holder, int position) {

        try {
            String url = "https://image.tmdb.org/t/p/w780/"+items.getJSONObject(position).getString("profile_path");
            String name=items.getJSONObject(position).getString("name");
            Log.d("casting",url+":"+name);
            holder.tv1.setText(name);
            Picasso.with(context).load(url).into(holder.iv);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return items.length();
    }


    public class CastViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv1;
        String id, typ, imgurl;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.img);
            tv1=(TextView) itemView.findViewById(R.id.text1);

        }

    }
}

//}
