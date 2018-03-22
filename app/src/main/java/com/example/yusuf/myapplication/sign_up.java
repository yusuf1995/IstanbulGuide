package com.example.yusuf.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.internal.GamesContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "EmailPassword";

    private EditText mNameField;
    private EditText mSurnameField;
    private EditText mNickField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button   mSignupButton;
    private String name;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private DatabaseReference mDatabase;

    private FirebaseAnalytics mFirebaseAnalytics;


    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mNameField = (EditText) findViewById(R.id.name);
        mSurnameField = (EditText) findViewById(R.id.surname);
        mNickField = (EditText) findViewById(R.id.nick);
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mSignupButton = (Button) findViewById(R.id.sign_up_button);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
      /*  if(mAuth.getCurrentUser() != null){
           // finish();
           // startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }*/


        mSignupButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v ){


                mAuth.createUserWithEmailAndPassword(mEmailField.getText().toString(),mPasswordField.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (!task.isSuccessful()){
                                    Toast.makeText(sign_up.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }else {

                                    onAuthSuccess(task.getResult().getUser());
                                    Toast.makeText(sign_up.this, "Authentication succesful.",
                                            Toast.LENGTH_SHORT).show();
                                    // finish();
                                    //  startActivity(new Intent(getApplicationContext(),sign_up.class));

                                }
                            }
                        });

            }
        });




        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // mFirebaseAnalytics.setUserProperty("favorite_food",name);





        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();



                if(user != null){

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                }

            }
        };

    }

    private void onAuthSuccess(FirebaseUser user) {
        String username =mNameField.getText().toString();
        String surname =mSurnameField.getText().toString();
        String nickname =mNickField.getText().toString();

        // Write new user
        writeNewUser(user.getUid(), username, surname, nickname, user.getEmail());

        // Go to MainActivity
        // startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }


    private void writeNewUser(String userId, String name, String surname ,String nickname, String email) {

        User user = new User(name, email,nickname,surname);

        mDatabase.child("users").child(userId).setValue(user);
    }


    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    public void onClick(View v ){

    }

}


