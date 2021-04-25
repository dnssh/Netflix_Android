package com.example.devapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ReviewPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewpage);



        String ln1= getIntent().getStringExtra("ln1");
        String ln2=getIntent().getStringExtra("ln2");
        String content= getIntent().getStringExtra("ln3");
        //Log.d("content",content);

        TextView tv1=(TextView) findViewById(R.id.tv1);
        TextView tv2=(TextView) findViewById(R.id.tv2);
        TextView tv3=(TextView) findViewById(R.id.tv3);

        tv1.setText(ln1);
        tv2.setText(ln2);
        tv3.setText(content);

        this.getSupportActionBar().hide();
    }
}