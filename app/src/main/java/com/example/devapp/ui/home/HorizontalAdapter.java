package com.example.devapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devapp.Details;
import com.example.devapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder>{

    private Context context;
    private String[] items;
    private String[] ids;
    private String mediatype;

    public HorizontalAdapter(String[] ids, String[] items, Context context) {
        this.ids=ids;
        this.items = items;
        this.context=context;
    }
//    public BookmarkPageAdapter(List<NewsArticle> articles, Context context, TextView textView){
//        this.articles = articles;
//        this.context = context;
//        this.no_bookmark = textView;
//    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        View view=inflater.inflate(R.layout.slideitem,parent,false);
        context = view.getContext();
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {

        //holder.txt.setText(items[position]);
        //Picasso.with(iv.getContext()).load(items[position]).into(iv);
        Log.d("Slider ", position + " " + ids[position]);
        Picasso.with(context).load(items[position]).into(holder.iv);


        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "onClick: Clicked on an image");
                Intent intent=new Intent(context, Details.class);
                intent.putExtra("id",ids[position]);
                intent.putExtra("media","movie");

                context.startActivity(intent);

                //Toast.makeText(context, "clicked on an image", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return ids.length;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.imageitem);

        }

    }
}
