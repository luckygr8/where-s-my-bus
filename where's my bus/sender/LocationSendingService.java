package com.jmit.wmbsender;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class LocationSendingService extends Service implements LocationListener {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private LocationManager locationManager;
    private Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference activity_state;
    DatabaseReference LatLng_of_bus;
    private String BUS_ID;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * @author
         * lakshay dutta
         *
         * This method gets called as soon as the service is initiated by the android Operationg System.
         */

        /**
         * checks if the location permissions are granted by the user.
         */
        this.context = MainActivity.context;
        locationManager = (LocationManager) this.context.getSystemService(context.LOCATION_SERVICE);


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this.context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Values.MIN_TIME, Values.MIN_DIST, LocationSendingService.this);


        Toast.makeText(context, "service started", Toast.LENGTH_LONG).show();

        /**
         * Initializing firebase services
         */

        firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference("LatLng");
        databaseReference = firebaseDatabase.getReference("buses");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * enables the app to run in the background.
         */
        String bus = intent.getStringExtra("bus");
        BUS_ID = bus;
        databaseReference.child(bus).child("LatLng").child("Latitude").setValue("");
        databaseReference.child(bus).child("LatLng").child("longitude").setValue("");
        activity_state = databaseReference.child(bus).child("state");
        databaseReference.child(bus).child("name").setValue(bus);

        activity_state.setValue("active");


        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Where's my bus -- open beta 1.0.0")
                .setContentText(input)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        activity_state.setValue("started");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Values.MIN_TIME, Values.MIN_DIST, LocationSendingService.this);

        return START_STICKY;

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        String str = (location.getLatitude()) + "\n" + (location.getLongitude());
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

        databaseReference.child(BUS_ID).child("Latitude").setValue(location.getLatitude());
        databaseReference.child(BUS_ID).child("longitude").setValue(location.getLongitude());

        databaseReference.child(BUS_ID).child("LatLng").child("Latitude").setValue(location.getLatitude());
        databaseReference.child(BUS_ID).child("LatLng").child("longitude").setValue(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        /**
         * ask for turning on location access if denied by the user
         */

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        //activity_state.setValue("destroyed");

        super.onDestroy();
        locationManager = null;
        databaseReference.child(BUS_ID).removeValue();
        stopForeground(true);
        stopSelf();

    }

}

/*private void startForeground()
    {
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Music player implemented by foreground service.");
        bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.");

        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        builder.setLargeIcon(largeIconBitmap);

        builder.setPriority(Notification.PRIORITY_MAX);

        builder.setFullScreenIntent(pendingIntent, true);

        Notification notification = builder.build();
        startForeground(1,notification);
    }*/
