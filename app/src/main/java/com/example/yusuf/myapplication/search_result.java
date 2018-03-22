package com.example.yusuf.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class search_result extends Activity {

    TextView title;
    String research="";
    String s,sd;
    int dene=0;
    DatabaseReference myRef;
    final ArrayList<Integer> list = new ArrayList<Integer>();
    final ArrayList<Integer> list2 = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        Intent search_world=getIntent();
        research=search_world.getStringExtra("what_search");

        title = (TextView)findViewById(R.id.search_result_title);
        title.setText("Result for"+" "+research);


        //generate list


        myRef= FirebaseDatabase.getInstance().getReference().child("places");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot datasnp=dataSnapshot;
                DataSnapshot datalist=dataSnapshot;
                for (DataSnapshot postSnapshot: datasnp.getChildren()) {

                    sd="";
                    String myParentNode = postSnapshot.getKey();
                    sd=postSnapshot.child("name").getValue().toString();
                    Boolean found;
                    found = sd.toLowerCase().contains(research);
                    if (found){
                        int foo = Integer.parseInt(myParentNode);
                        String slist=dataSnapshot.child(foo+"").child("images_name").child("1").child("name").getValue().toString();
                        int resID = getResources().getIdentifier(slist , "drawable", getPackageName());
                        list.add(resID);
                        Log.d("suan yukleme",""+resID);
                        list2.add(foo);
                        dene=1;
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        Log.d("suan yukleme","gogoggogo");
        search_listview adapter = new search_listview(list,list2,this);




        ListView lView = (ListView)findViewById(R.id.search_result_listview);
        //Log.e("tag", "geldi4");
        lView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("suan yukleme","ggggggggg");
    }
}
