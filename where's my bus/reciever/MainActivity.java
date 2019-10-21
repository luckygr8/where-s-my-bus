package com.jmit.wmbreciever;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progress;
    private RecyclerView recyclerView;
    private LinkedList<Object> buses_list = new LinkedList<>();

    private FirebaseDatabase database;
    private DatabaseReference list_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();

    }

    private void Init() {
        recyclerView = findViewById(R.id.recyclerview);
        progress = findViewById(R.id.progress);

        database = FirebaseDatabase.getInstance();
        list_reference = database.getReference("buses");

        final RecyclerAdapter adapter = new RecyclerAdapter(MainActivity.this,buses_list,recyclerView);
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lmg);
        recyclerView.setAdapter(adapter);

        list_reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                progress.setVisibility(View.VISIBLE);

                Map<String, Object> mp = (Map<String, Object>) dataSnapshot.getValue();
                double Latitude = (double) mp.get("Latitude");
                double longitude = (double) mp.get("longitude");
                String status = (String) mp.get("state");
                String name = (String) mp.get("name");

                if(name==null || status==null)
                    return;

                Bus bus = new Bus();
                bus.setLatitude(Latitude);
                bus.setLongitude(longitude);
                bus.setStatus(status);
                bus.setName(name);

                buses_list.add(bus);

                System.out.println(bus.toString());

                progress.setVisibility(View.GONE);

                adapter.AddIntoBusesList(bus);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        });
    }
}
