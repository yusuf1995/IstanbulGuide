package com.example.yusuf.myapplication;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class closest_place extends FragmentActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback {

    private GoogleMap mMap;

    String[][] coordinates = new String[100][2];
    String[] closest_distance = new String[100];
    String about;

    int deneme2;

    private ImageButton selected_plc;
    private Button button;
    private TextView textView,deneme;
    private LocationManager locationManager;
    private LocationListener listener;
    //public ImageButton contentbutton;
    private StorageReference mStorageRef;
    File localFile1 = null;

    double myLongitude,myLatitude;

    public int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closest_place);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        selected_plc = (ImageButton) findViewById(R.id.selected_place);
       // textView = (TextView) findViewById(R.id.mycoordinates);
        button = (Button) findViewById(R.id.gps_goster);
        deneme = (TextView) findViewById(R.id.deneme1);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //textView.setText("\n " + location.getLongitude() + " " + location.getLatitude());

                myLongitude = location.getLongitude();
                myLatitude = location.getLatitude();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }

    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won'textView execute IF permissions are not allowed, because in the line above there is return statement.

        locationManager.requestLocationUpdates("gps", 5000, 0, listener);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (!(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

            mMap.setMyLocationEnabled(true);

        }


        DatabaseReference myRef1=FirebaseDatabase.getInstance().getReference().child("places");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter = 0;

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    coordinates[counter][0]= postSnapshot.child("cordination").child("x").getValue().toString();
                    coordinates[counter][1]= postSnapshot.child("cordination").child("y").getValue().toString();
                    about = postSnapshot.child("name").getValue().toString();


                    LatLng place = new LatLng((Double.parseDouble(coordinates[counter][0])),(Double.parseDouble(coordinates[counter][1])));
                    mMap.addMarker(new MarkerOptions().position(place).title(about)).setTag(counter+1);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,10));


                    closest_distance[counter] = Double.toString(Math.sqrt(Math.pow((Double.parseDouble(coordinates[counter][0])-myLatitude),2)+Math.pow((Double.parseDouble(coordinates[counter][1])-myLongitude),2)));


                    counter++;
                }

                //deneme.setText(Integer.toString(getClosestPlace(closest_distance,counter)));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference closest_referance = FirebaseDatabase.getInstance().getReference().child("places");
                        closest_referance.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String plc = dataSnapshot.child(Integer.toString(getClosestPlace(closest_distance,counter)+1)).child("name").getValue().toString();
                                deneme.setText(plc);
                                String pid = Integer.toString(getClosestPlace(closest_distance,counter)+1);

                                //selected_plc.setImageResource(getResources().getIdentifier(dataSnapshot.child(pid).child("images_name").child("1").child("name").getValue().toString(),"drawable", getPackageName()));
                                selected_plc.setImageResource(R.drawable.sultan_ahmet);
                                selected_plc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deneme.setText("s");
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        LatLng mylat = marker.getPosition();
        //deneme.setText(Double.toString(mylat.latitude));
        //deneme.setText(Integer.toString((Integer) marker.getTag()));

        DatabaseReference markerreferance = FirebaseDatabase.getInstance().getReference().child("places").child(Integer.toString((Integer) marker.getTag()));
        markerreferance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deneme.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return false;
    }

    public static int getClosestPlace(String[] array,int limit){

        int address_of_min=0;
        double minvalue = Double.parseDouble(array[0]);

        for(int i = 0; i < limit; i++){

            if((Double.parseDouble(array[i]))<(minvalue)){
                minvalue = Double.parseDouble(array[i]);
                address_of_min = i;
            }

        }
        return address_of_min;

    }
}
