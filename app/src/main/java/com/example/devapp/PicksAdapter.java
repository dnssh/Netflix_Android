package com.example.devapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;


public class PicksAdapter extends RecyclerView.Adapter<PicksAdapter.PicksViewHolder>{

    private Context context;
    private String mtype;
    private JSONArray response;

    public PicksAdapter(String mtype,JSONArray response, Context context) {
        this.mtype=mtype;
        this.response=response;
        this.context=context;
    }

    @NonNull
    @Override
    public PicksAdapter.PicksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        View view=inflater.inflate(R.layout.picks_card,parent,false);
        context = view.getContext();
        return new PicksAdapter.PicksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicksAdapter.PicksViewHolder holder, int position) {

        try {
            String id=response.getJSONObject(position).getString("id");
            String imgurl="https://image.tmdb.org/t/p/w780/"+response.getJSONObject(position).getString("poster_path");
            Picasso.with(context).load(imgurl).into(holder.iv);


            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Details.class);
                    intent.putExtra("id",id);
                    intent.putExtra("media",mtype);
                    context.startActivity(intent);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class PicksViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;

        public PicksViewHolder(@NonNull View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.imageitem);

        }

    }
}
