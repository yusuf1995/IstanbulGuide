package com.example.yusuf.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.drive.query.Query;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {


    ImageButton top_1;
    ImageButton top_2;
    ImageButton top_3;
    Button search;
    EditText search_place;
    TextView title;
    String s,sd;
    int i=0;
    String place_name1;
    DatabaseReference myRef;
    ArrayList<Integer> list = new ArrayList<Integer>();
    ArrayList<Integer> list2 = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cls = new Intent(MainActivity.this,closest_place.class);
                startActivity(cls);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ////////////////////////////////////////////////////////////////////////Content icerik
        search_place =(EditText)findViewById(R.id.search_text);
        search = (Button) findViewById(R.id.search_button1);
        top_1 = (ImageButton)findViewById(R.id.top_1);
        top_2 = (ImageButton)findViewById(R.id.top_2);
        top_3 = (ImageButton)findViewById(R.id.top_3);
        title =(TextView)findViewById(R.id.content_title);
        top_1.setOnClickListener(this);
        top_2.setOnClickListener(this);
        top_3.setOnClickListener(this);
        search.setOnClickListener(this);
        search_place.setOnClickListener(this);
        /////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////

        myRef=FirebaseDatabase.getInstance().getReference().child("places");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot data;
                        data=dataSnapshot;
                        dataSnapshot=dataSnapshot.child("2").child("images_name");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    s="";
                    s=postSnapshot.child("name").getValue().toString();

                    if(i==0){
                        int resID = getResources().getIdentifier(s , "drawable", getPackageName());
                        top_1.setImageResource(resID);
                        place_name1="2";

                    }
                    if (i==1){
                        int resID = getResources().getIdentifier(s , "drawable", getPackageName());
                        top_2.setImageResource(resID);
                        place_name1="2";

                    }
                    if (i==2){
                        int resID = getResources().getIdentifier(s , "drawable", getPackageName());
                        top_3.setImageResource(resID);
                        place_name1="2";

                    }
                    i++;

                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        ////////////////////////////////////////////////////////////////////

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent top_1_ = new Intent(MainActivity.this,sign_up.class);
            startActivity(top_1_);
            return true;
        }
        if (id == R.id.login) {
            Intent top_1_ = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(top_1_);
            return true;
        }
        if (id == R.id.profil) {
            Intent top_1_ = new Intent(MainActivity.this,Profile.class);
            startActivity(top_1_);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.top_1:
                Intent top_1_ = new Intent(MainActivity.this,content_screen.class);
                top_1_.putExtra("place_name",place_name1);
                startActivity(top_1_);
                break;
            case R.id.top_2:
                Intent top_2_ = new Intent(MainActivity.this,content_screen.class);
                top_2_.putExtra("place_name",place_name1);
                startActivity(top_2_);
                break;
            case R.id.top_3:
                Intent top_3_ = new Intent(MainActivity.this,content_screen.class);
                top_3_.putExtra("place_name",place_name1);
                startActivity(top_3_);
                break;
            case R.id.search_button1:

                Intent search_rst = new Intent(MainActivity.this,search_result.class);
                place_name1=search_place.getText().toString();
                search_rst.putExtra("what_search",place_name1);
                startActivity(search_rst);
                break;
            case R.id.search_text:
                search_place.setText("");
                break;

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mosques) {
            Intent search_rst = new Intent(MainActivity.this,search_result.class);
            search_rst.putExtra("what_search","Mosques");
            startActivity(search_rst);


        } else if (id == R.id.parks) {

        } else if (id == R.id.church) {

        } else if (id == R.id.palaces) {

        }  else if (id == R.id.about_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
