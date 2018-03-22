package com.example.yusuf.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Yusuf on 12.04.2017.
 */

public class search_listview extends BaseAdapter implements ListAdapter {
    private ArrayList<Integer> list = new ArrayList<Integer>();
    private ArrayList<Integer> list2 = new ArrayList<Integer>();
    private Context context;

    private StorageReference mStorageRef;
    File localFile1,localFile2,localFile3 = null;

    public search_listview(ArrayList<Integer> list,ArrayList<Integer> list2, Context context) {
        this.list = list;
        this.list2=list2;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.search_result, null);
        }

        //Handle TextView and display string from your list
        TextView title = (TextView)view.findViewById(R.id.search_place_name);
        title.setText(list.get(position)+"");

        //Handle buttons and add onClickListeners
        final ImageButton resim = (ImageButton)view.findViewById(R.id.search_resim);
        resim.setImageResource(list.get(position));

        resim.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent content = new Intent(context,content_screen.class);
                content.putExtra("place_name",""+list2.get(position));
                context.startActivity(content);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}