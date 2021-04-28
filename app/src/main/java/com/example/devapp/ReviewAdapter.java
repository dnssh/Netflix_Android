package com.example.devapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devapp.ui.dashboard.SearchAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.util.logging.Level.parse;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    private Context context;
    private JSONArray items;

    public ReviewAdapter(JSONArray items, Context context) {
        this.items = items;
        this.context=context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.review_card,parent,false);
        return new ReviewAdapter.ReviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {

        try {
            String name = items.getJSONObject(position).getString("author");
            String date=items.getJSONObject(position).getString("created_at");
            String rating=items.getJSONObject(position).getJSONObject("author_details").getString("rating");
            String content = items.getJSONObject(position).getString("content");

//            SimpleDateFormat sdfr = new SimpleDateFormat("E, MMM dd yyyy");
//            Date newdate=sdfr.parse(date );
//            String fdate=sdfr.format(newdate);

            String ln2="by "+name+" on "+date.substring(0,10);
            String ln1="";
            try {
                ln1 = (Integer.parseInt(rating) / 2) + "/5 ";
            }
            catch(NumberFormatException e)
            {
                System.out.print("NullPointerException Caught");
            }
            holder.tv1.setText(ln2);
            holder.tv2.setText(ln1);
            holder.tv3.setText(content);


            String finalLn = ln1;
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("card", "onClick: Clicked on an card");

                    Intent intent=new Intent(context.getApplicationContext(), ReviewPageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ln1", finalLn);
                    intent.putExtra("ln2",ln2);
                    intent.putExtra("ln3",content);

                    context.startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.length();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView tv1,tv2,tv3;
        String id, type, title, rating, date, imgurl;
        CardView cv;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1=(TextView) itemView.findViewById(R.id.name);
            tv2=(TextView) itemView.findViewById(R.id.rating);
            tv3=(TextView) itemView.findViewById(R.id.content);
            cv=(CardView) itemView.findViewById(R.id.review_card);

        }

    }


}
