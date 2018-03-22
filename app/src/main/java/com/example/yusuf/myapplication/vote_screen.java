package com.example.yusuf.myapplication;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class vote_screen extends AppCompatActivity {

    private ImageView myimage;
    private static SeekBar vote_seekbar;
    private static TextView vote_text;
    Button usevote;
    String s;
    private static TextView currentpoint;
    int progress_value=0;
    String placeid="";
    String newvote;
    int lastvote;
    String about;
    int imgid;
    String imgname;

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_screen);

        Intent search_world=getIntent();
        placeid=search_world.getStringExtra("place_name");


        myimage = (ImageView)findViewById(R.id.place_image);

        DatabaseReference for_imgview = FirebaseDatabase.getInstance().getReference().child("places").child(placeid);
        for_imgview.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imgname = dataSnapshot.child("images_name").child("1").child("name").getValue().toString();
                imgid = getResources().getIdentifier(imgname,"drawable", getPackageName());
                myimage.setImageResource(imgid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        currentpoint = (TextView)findViewById(R.id.current_point);

        seekbaraction();




        final DatabaseReference writeData = FirebaseDatabase.getInstance().getReference().child("places").child(placeid).child("point");
        DatabaseReference myRef=FirebaseDatabase.getInstance().getReference().child("places");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                s = dataSnapshot.child(placeid).child("point").getValue().toString();
                about = dataSnapshot.child(placeid).child("name").getValue().toString();
                currentpoint.setText("Total Point of " +  about + " : "+ s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        usevote = (Button)findViewById(R.id.use_vote);
        usevote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentvote = Integer.parseInt(s);

                lastvote = currentvote + progress_value + 1;

                newvote = Integer.toString(lastvote);

                //newvote = "" + currentvote;

                writeData.setValue(newvote);

                //newvote = "" + (progress_value+1);

                //currentpoint.setText("You gave : " + newvote);

                Toast.makeText(vote_screen.this,"Oyunu kullandın",Toast.LENGTH_LONG).show();

            }
        });


    }

    public void seekbaraction(){
        vote_seekbar = (SeekBar)findViewById(R.id.vote_bar);
        vote_text = (TextView)findViewById(R.id.current_vote);
        vote_text.setText("Your vote : " + (vote_seekbar.getProgress()+1) + " / " + (vote_seekbar.getMax()+1));

        vote_seekbar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {


                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        progress_value = progress;
                        vote_text.setText("Your vote : " + (progress+1) + " / " + (vote_seekbar.getMax()+1));


                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {



                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        vote_text.setText("Your vote : " + (progress_value+1) + " / " + (vote_seekbar.getMax()+1));


                    }
                }
        );

    }


}
