package com.jmit.wmbreciever;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference ;

    private static Double LATITUDE=30.13350021;
    private static Double LONGITUDE=77.29640954;

    private TextView coordniates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /**
         * @author
         * lakshay dutta
         *
         * connecting to firebase service to read the cordinate data
         *
         * @see
         * #map
         */
        String name = getIntent().getStringExtra("name");

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("buses").child(name).child("LatLng");

        coordniates = findViewById(R.id.coordinates);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    LATITUDE = (Double) dataSnapshot.child("Latitude").getValue();
                    LONGITUDE = (Double) dataSnapshot.child("longitude").getValue();
                    refresh();

                    coordniates.setText("Coordniates : "+LATITUDE+" | "+LONGITUDE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                /*com.jmit.wmbreciever.LatLng LL = (com.jmit.wmbreciever.LatLng) dataSnapshot.getValue();

                LATITUDE = LL.getLatitude();
                LONGITUDE = LL.getLongitude();
                coordniates.setText("Coordniates : "+LATITUDE+" | "+LONGITUDE);*/

                /*Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String lat = map.get("Latitude").toString();
                String lon = map.get("longitude").toString();

                System.out.println(dataSnapshot.getValue()+"lat");

                /*if(!lat.isEmpty() && !lon.isEmpty())
                {
                    LATITUDE = Double.parseDouble(lat);
                    LONGITUDE = Double.parseDouble(lon);
                    refresh();

                    coordniates.setText("Coordniates : "+LATITUDE+" | "+LONGITUDE);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }


    /**
     * #map
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Add a marker to the location and move the camera
                refresh(LATITUDE, LONGITUDE);

                // repeat after N seconds
                handler.post(this);//for 5 seconds
            }
        };
        handler.post(runnable);*/
    }
    public void refresh() {
        LatLng loc = new LatLng(LATITUDE, LONGITUDE);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(loc).title("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        float zoomLevel = 18.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomLevel));
        // mMap.setMyLocationEnabled(true);
    }
}

/** @igonore
 * Calculated values used in testing
 * //LATTTUDE += 900.009009009009009009009009009009e-6;// for 100 meters
 */
