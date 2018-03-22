package com.example.yusuf.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements View.OnClickListener {



    private static final String TAG = "ViewDatabase";

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;


    private TextView profilName;
    private TextView profilSurname;
    private TextView profilEmail;
    private TextView profilNickname;
    private Button logOutButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        firebaseAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        DatabaseReference myRef1=FirebaseDatabase.getInstance().getReference().child("users");

        profilName = (TextView)findViewById(R.id.profil_name);
        profilSurname = (TextView)findViewById(R.id.profil_surname);
        profilEmail = (TextView)findViewById(R.id.profil_email);
        profilNickname = (TextView)findViewById(R.id.profil_nickname);
        logOutButton = (Button)findViewById(R.id.log_out_button);




        if(firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){

                   // startActivity(new Intent(getApplicationContext(),MainActivity.class));

                }
                else{


                }

            }
        };

        if(firebaseAuth.getCurrentUser()!=null) {

            userID = user.getUid();
            profilEmail.setText("Welcome " + user.getEmail());


            final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    User user = dataSnapshot.getValue(User.class);

                    profilName.setText(user.getUsername());
                    profilNickname.setText(user.getNickname());
                    profilSurname.setText(user.getUsersurname());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        logOutButton.setOnClickListener(this);

    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }


    
    @Override
    public void onClick(View view) {
        if(view == logOutButton){
            Toast.makeText(Profile.this, "Loging out succesful.",
                    Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

    }
}
