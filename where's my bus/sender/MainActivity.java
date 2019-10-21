package com.jmit.wmbsender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    private Button btnStartService, btnStopService, test;
    private Spinner busSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("WMB -- open beta 1.0.0");
        context = this;

        Init();

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (busSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(MainActivity.this, "please select a bus", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String bus = (String) busSpinner.getSelectedItem();
                    startService(bus);
                }
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });

        /**
         * @author
         * lakshay dutta
         *
         * This code starts a new background service. Note that services can be killed by the operating system
         */

        /*Intent intent = new Intent(MainActivity.this , LocationSendingService.class);
        startService(intent);*/

    }

    public void Init() {
        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);
        busSpinner = findViewById(R.id.busSpinner);
        test = findViewById(R.id.test);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                StringRequest request = new StringRequest(Request.Method.GET, Values.TEST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                queue.add(request);
            }

        });

        String[] buses = getResources().getStringArray(R.array.buses);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buses);
        busSpinner.setAdapter(adapter);

    }

    public void startService(String bus) {
        Intent serviceIntent = new Intent(this, LocationSendingService.class);
        serviceIntent.putExtra("inputExtra", "Thank you for helping for the beta release");
        serviceIntent.putExtra("bus", bus);

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, LocationSendingService.class);
        stopService(serviceIntent);
        busSpinner.setSelection(0);
        //finish();
    }
}
