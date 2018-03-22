package com.example.yusuf.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class comment_screen extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
   TextView text;
    EditText edittext;
    Button yorum_gonder;
    String total="";
    String s="";
    String research="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_screen);

        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        Log.d("dene1","6");
        Intent search_world=getIntent();
        research=search_world.getStringExtra("place_name");
        Log.d("dene11",research);
        text=(TextView)findViewById(R.id.comment_text);
        edittext=(EditText)findViewById(R.id.comment_edittext);
        Log.d("dene1","5");
        final DatabaseReference myRef=FirebaseDatabase.getInstance().getReference().child("places").child(research).child("yorumlar");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total ="";
                Log.d("dene1","4");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    s=postSnapshot.child("comment").getValue().toString();
                    total = total+"\n\n"+s ;

                }
                text.setText(total);
                Log.d("dene1",total);
            }

            @Override
            public void onCancelled(DatabaseError error) {
           }
        });
        Log.d("dene1","7");

        yorum_gonder=(Button)findViewById(R.id.button5);




        yorum_gonder.setText(research);
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dene1","2");
                edittext.setText("");
            }
        });




            yorum_gonder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(user!=null){
                        Log.d("dene1","1");
                        DatabaseReference x=myRef.push();
                        x.child("comment").setValue(edittext.getText().toString());
                        x.child("userid").setValue("12");

                    }else{
                        startActivity(new Intent(comment_screen.this,LoginActivity.class));
                        finish();
                    }
                }
            });


    }
}
