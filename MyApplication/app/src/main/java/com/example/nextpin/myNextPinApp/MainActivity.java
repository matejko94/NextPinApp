package com.example.nextpin.myNextPinApp;


import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.support.design.widget.FloatingActionButton;

import com.example.nextpin.myNextPinApp.preference.SharedPrefUtils;

import net.nextpin.geolib.NextPin;
import net.nextpin.geolib.NextPinListener;
import net.nextpin.geolib.types.GeoActivity;
import net.nextpin.geolib.types.GeoPrediction;

import java.util.Calendar;
import java.util.List;
public class MainActivity extends AppCompatActivity implements NextPinListener,NextPin.GeoActivityReceiver, NextPin.GeoPredictionReceiver {

    String nextPinToken;
    TextView tvPermissionsProblem;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //request for permission which app needs it
        tvPermissionsProblem = (TextView)findViewById(R.id.tvPermissionsProblem);
        tvPermissionsProblem.setMovementMethod(LinkMovementMethod.getInstance());
        tvPermissionsProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextPin.requestPermissions(MainActivity.this);
            }
        });


        NextPin nextPin= NextPin.getNextPinInstance(this);
        nextPinToken = SharedPrefUtils.getString(getApplicationContext(),
                PreferencesFragment.PREF_TOKEN);

        //if you want to track cordinates/accelerometer and other things you can
        nextPin.start();
        //if you dont define unique token for specific person/object you cant get data
        nextPin.setToken(nextPinToken);
        //just show notification if user shut down gps
        nextPin.setNotificationGpsOff(true, R.mipmap.ic_launcher, MainActivity.class);
        //add standard listeners
        nextPin.addListener(this);

        //

        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        //example how to call Activities for specific day it is possible also get activity for more than one days
        nextPin.getGeoActivities(this,start);




        //It is posiible to get prediction for next place for specific stay point
        // nextPin.getGeoPrediction();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PrefsActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //check whether all permissions are intact
        String missingPermissionsStr = NextPin.checkForMissingPermissions(MainActivity.this);
        if (!"".equals(missingPermissionsStr)){
            Log.d(TAG,"Problem with permissions!");
            tvPermissionsProblem.setVisibility(View.VISIBLE);
            missingPermissionsStr+= " <a href=\"#\">Click Here</a>";
            tvPermissionsProblem.setText(Html.fromHtml(missingPermissionsStr));
        }
        else{
            //if the permissions are ok now, remove this view.
            tvPermissionsProblem.setVisibility(View.GONE);
        }
    }

    @Override
    public void receiveGeoActivities(List<GeoActivity> list) {

        //activity for specific day
        if(list!=null) {
            TextView textView = (TextView) findViewById(R.id.testView);
            String test = "";
            for (GeoActivity geoActivity : list)
                test = test + geoActivity.getName() + " " + geoActivity.getDuration() + " ";
            textView.setText(test);
        }
        //for specific stay point you can call next place prediction
//        NextPin.getNextPinInstance(MainActivity.this).getGeoPrediction(this, list.get(0).getLocationId());

    }





    @Override
    public void onLocalLocationDetected(Location location) {
        //last detected location in library
    }

    @Override
    public void onDBChangeDetected(int i) {

    }

    @Override
    public void onLastSentLocation(Location location) {

        //last location which was send on server

    }

    @Override
    public void onWakeUp() {

        //library wakes to retrieve new data.


    }

    @Override
    public void onLastRejectedLocation(Location location) {
        //last rejected location

    }

    @Override
    public void onNewStayPoint() {
        //maybe you need to update list of stay points
    }

    @Override
    public void onEditStaypPoint() {

    }

    @Override
    public void receiveGeoPrediction(List<GeoPrediction> list) {
        //implement what you want to do with prediction for specific activity
    }
}
