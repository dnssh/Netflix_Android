package com.example.devapp.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.devapp.Details;
import com.example.devapp.Movie;
import com.example.devapp.R;
import com.example.devapp.ui.dashboard.SearchAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.devapp.Constants.baseurl;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {


    private Context context;
    //private String[] items;
    private String[] ids;
    private String mediatype;
    private List<String> items;
    private Map<String,?> keys;
    private List<ArrayList<String>> watchlist;
    private List<String> allitems;

//    public BookmarkAdapter(List watchlist,Context context) {
//        this.watchlist=watchlist;
//        this.context = context;
//    }

    public BookmarkAdapter(List allitems,Context context) {
        this.allitems=allitems;
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
        String item=allitems.get(position);
        String media;

        String id=item.substring(1);
        String typ=item.substring(0,1);
        if(typ.equals("m")){
            media="movie";
        }
        else{
            media="tv";
        }


        String url=baseurl+"/"+media+"details?id="+id;
        RequestQueue que = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, response -> {
            try {
                String imgurl="https://image.tmdb.org/t/p/w780/"+response.getString("backdrop_path");
                imgurl=imgurl.trim();
                Picasso.with(context).load(imgurl).into(holder.iv);
                holder.tv1.setText(media);
                Log.d("image url",imgurl);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        que.add(jsonRequest);



        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Details.class);
                intent.putExtra("id",id);
                intent.putExtra("media",media);
                context.startActivity(intent);
            }
        });


        holder.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = context.getSharedPreferences("bookmarks", 0);
                SharedPreferences.Editor editor = pref.edit();


                String rmv=media.substring(0,1)+id;

                String wtchlst= pref.getString("wl",null);

                List<String> allitems = new ArrayList<>(Arrays.asList(wtchlst.split(",")));
//                List<String> allitems = Arrays.asList(wtchlst.split(","));
                Log.d("List Format:", String.valueOf(allitems));

                allitems.remove(rmv);
                wtchlst= TextUtils.join(",",allitems);
                Log.d("Modified sp string",wtchlst);
                editor.putString("wl", wtchlst);
                editor.commit();
                Toast.makeText(context, "Removed from watchlist", Toast.LENGTH_SHORT).show();
                Log.d("Removed",id);


                holder.cv.setVisibility(View.GONE);

                notifyDataSetChanged();

            }

        });
    }

    @Override
    public int getItemCount() {
        return allitems.size();
    }


    public class BookmarkViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv1;
        ImageView ib;
        String id, typ, imgurl;
       private CardView cv;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.cardimage);
            tv1=(TextView) itemView.findViewById(R.id.type);
            ib=(ImageView) itemView.findViewById(R.id.minuswatch);
            cv=(CardView) itemView.findViewById(R.id.recycler_card);



        }

    }
}
