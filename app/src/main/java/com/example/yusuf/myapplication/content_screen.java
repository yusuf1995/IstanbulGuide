package com.example.yusuf.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class content_screen extends AppCompatActivity {

        ImageAdapter adapter;
        ViewPager viewPager;
        TextView text;
        Button yorum,oyver;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String research="";
        String s;
        int[] imgs = {2130837671,2130837671,2130837671,2130837671};
        int i;
        int adapter_i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_screen);
        viewPager = (ViewPager)findViewById(R.id.view_pager);



        Intent search_world=getIntent();
        research=search_world.getStringExtra("place_name");



        yorum =(Button)findViewById(R.id.button2);
        text=(TextView)findViewById(R.id.content_inf_text);
        oyver = (Button) findViewById(R.id.give_vote);

        //yorum.setText(research);
        Log.d("content","1");
        yorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent top_1_ = new Intent(content_screen.this,comment_screen.class);
                top_1_.putExtra("place_name",research);
                Log.d("content r",research);
                startActivity(top_1_);
            }
        });

        oyver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent give_vote_ = new Intent(content_screen.this,vote_screen.class);
                give_vote_.putExtra("place_name",research);
                startActivity(give_vote_);

            }
        });



    DatabaseReference myRef1=FirebaseDatabase.getInstance().getReference().child("places");
    myRef1.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            DataSnapshot datatext;
            datatext=dataSnapshot.child(research).child("description");
            String s= datatext.getValue().toString();
            text.setText(s);
            i = 0;
            dataSnapshot=dataSnapshot.child(research).child("images_name");
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                s="";
                s=postSnapshot.child("name").getValue().toString();
                int resID = getResources().getIdentifier(s , "drawable", getPackageName());

                imgs[i]=resID;
                s="";
                i++;
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
        }
    }
    );
            adapter = new ImageAdapter(this,imgs);
            viewPager.setAdapter(adapter);

    }

}

