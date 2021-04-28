package com.example.devapp.ui.home;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.devapp.Details;
import com.example.devapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder>{

    private Context context;
    private String[] items;
    private String[] ids;
    private String mtype;
    private JSONArray response;

    public HorizontalAdapter(String mtype,JSONArray response, Context context) {
//        this.ids=ids;
//        this.items = items;
        this.mtype=mtype;
        this.response=response;
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
        //Log.d("Slider ", position + " " + ids[position]);
        try {
            String id=response.getJSONObject(position).getString("id");
            String imgurl="https://image.tmdb.org/t/p/w780/"+response.getJSONObject(position).getString("poster_path");
            Log.d("image url",imgurl);
            Picasso.with(context).load(imgurl).into(holder.iv);


            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("click", "onClick: Clicked on an image");
                    Intent intent=new Intent(context, Details.class);
                    intent.putExtra("id",id);
                    intent.putExtra("media",mtype);

                    context.startActivity(intent);

                    //Toast.makeText(context, "clicked on an image", Toast.LENGTH_SHORT).show();
                }
            });


            holder.dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("menu", "onClick: Clicked on an menu pop up");
                    //Toast.makeText(context, "checking dots", Toast.LENGTH_SHORT).show();

                    PopupMenu popup = new PopupMenu(context, v);
                    popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());
//                    popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener);
//                    popup.inflate(R.menu.pop_up_menu);
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.openTMDB:
                                    Uri uri;
                                    String url = "https://www.themoviedb.org/"+mtype+"/"+id;
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setPackage("com.android.chrome");
                                    try {
                                        context.startActivity(i);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "unable to open chrome", Toast.LENGTH_SHORT).show();
                                        i.setPackage(null);
                                        context.startActivity(i);
                                    }
                                    return true;
                                case R.id.shareFB:
                                    //https://facebook.com/sharer/sharer.php?u=https://youtube.com/watch?v='+vidlink['key']}}
                                    String url2 = "https://facebook.com/sharer/sharer.php?u=https://www.themoviedb.org/"+mtype+"/"+id;
                                    Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
                                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i2.setPackage("com.android.chrome");
                                    try {
                                        context.startActivity(i2);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "unable to open chrome", Toast.LENGTH_SHORT).show();
                                        i2.setPackage(null);
                                        context.startActivity(i2);
                                    }
                                    return true;

                                case R.id.shareTwitter:
                                    //https://twitter.com/intent/tweet?text=Watch%20'+results['title']+'%0D%0Ahttps://youtube.com/watch?v='+vidlink['key']+'%0D%0A%23USC%20%23CSCI571%20%23FightOn
                                    String url3 = "https://twitter.com/intent/tweet?text=https://www.themoviedb.org/"+mtype+"/"+id;
                                    Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url3));
                                    i3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i3.setPackage("com.android.chrome");
                                    try {
                                        context.startActivity(i3);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "unable to open chrome", Toast.LENGTH_SHORT).show();
                                        i3.setPackage(null);
                                        context.startActivity(i3);
                                    }
                                    return true;


                                case R.id.addWatchlist:
                                    SharedPreferences pref =context.getSharedPreferences("bookmarks", 0);
                                    SharedPreferences.Editor editor = pref.edit();
                                    String wtchlst=pref.getString("wl","");
                                    String chk=mtype.substring(0,1)+id+",";
                                    if(!wtchlst.contains(chk)){
                                        wtchlst=chk+wtchlst;
                                        editor.putString("wl", wtchlst);
                                        editor.commit();
                                        Log.d("stored", wtchlst);
                                    }

                                    Toast.makeText(context, "Added to watchlist", Toast.LENGTH_SHORT).show();

//                                case R.id.addWatchlist:
//                                    SharedPreferences pref =context.getSharedPreferences("bookmarks", 0);
//                                    if(!pref.contains(id)){
//                                        SharedPreferences.Editor editor = pref.edit();
//                                        List<String> list=new ArrayList<String>();
//                                        list.add(mtype.substring(0,1));
//                                        list.add(imgurl);
//                                        editor.putString(id, String.valueOf(list));
//                                        editor.commit();
//                                        Log.d("stored",id+":"+String.valueOf(list));
//                                        Toast.makeText(context, "Added to Watchlist", Toast.LENGTH_SHORT).show();
                                    //}

                                default:
                                    return false;
                            }
                        }
                    });
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

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        ImageButton dots;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.imageitem);
            dots=(ImageButton) itemView.findViewById(R.id.dots);

        }

    }
}
